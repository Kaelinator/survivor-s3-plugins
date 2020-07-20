package com.kaelkirk.machines.duels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import javax.activity.InvalidActivityException;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import static com.kaelkirk.machines.duels.EloCalculator.calculateElo;

public class DuelMachine implements Runnable {

  private static DuelMachine duelMachine = new DuelMachine();
  private ArrayList<DuelData> duelRequests;
  private Plugin plugin;
  private DuelData duel;

  private DuelMachine() { }

  public static void init(Plugin plugin) {
    duelMachine.plugin = plugin;
    duelMachine.duel = new DuelData();
    duelMachine.duelRequests = new ArrayList<DuelData>();
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, duelMachine, 0, 20);
  }

  /**
   * Initiates new duel between dueler and duelee
   * 
   * @param dueler - player to duel
   * @param duelee - player to duel
   */
  public static void requestDuel(Player dueler, Player duelee) {
    DuelData req = new DuelData(dueler, duelee);

    sendRequestMessages(req);

    duelMachine.duelRequests.add(req);
  }

  /* Game loop */
  @Override
  public void run() {
    Iterator<DuelData> itr = duelMachine.duelRequests.iterator();

    while (itr.hasNext()) {
      DuelData req = itr.next();
      req.update();
      if (req.isExpired() || !isInDuelRegion(req.getDueler())) {
        sendExpiredMessages(req);
        itr.remove();
        continue;
      }

      if (isInDuelRegion(req.getDuelee())) {
        // begin game
        sendPregameMessages(req);
        duelMachine.duel = req;
        duelMachine.duel.setState(DuelState.PREGAME);
        duelMachine.duelRequests.clear();
        break;
      }
    }

    int time = duelMachine.duel.getTime();
    Player duelee = duelMachine.duel.getDuelee();
    Player dueler = duelMachine.duel.getDueler();
    switch (duelMachine.duel.getState()) {
      case IDLE: break;

      case PREGAME:
        duelMachine.duel.update();

        if (!isInDuelRegion(duelee) || !isInDuelRegion(dueler)) {
          sendText(duelMachine.duel, "Duel cancelled");
          duelMachine.duel = new DuelData();
        }

        if (time <= 0) {
          duelMachine.duel.setState(DuelState.INGAME);
          sendText(duelMachine.duel, "Go!");
          kickOtherPlayersOut(duelMachine.duel);
        } else if (time % 10 == 0 || time < 5)
          sendText(duelMachine.duel, "Duel in " + time + "s");

        break;

      case INGAME:
        duelMachine.duel.update();

        if (!isInDuelRegion(duelee))
          endMatch(duelee);
        if (!isInDuelRegion(dueler))
          endMatch(dueler);

        if (time <= 0) {
          duelMachine.duel.setState(DuelState.POSTGAME);
          endMatch(null);
        }
        break;

      case POSTGAME:
        duelMachine.duel.update();

        if (time <= 0) {
          duelMachine.duel = new DuelData();
        }
        break;

      case WAITING:
        throw new NullPointerException("Duel shouldn't be in WAITING state");

      default:
        throw new NullPointerException("Invalid duel state");
    }
  }
  
  private void sendPregameMessages(DuelData req) {
    Player duelee = req.getDuelee();
    Player dueler = req.getDueler();
    duelee.sendMessage("You accepted " + dueler.getDisplayName() + ChatColor.WHITE +
      "'s duel request");
    dueler.sendMessage(duelee.getDisplayName() + ChatColor.WHITE + " has accepted your duel request");
  }

  /**
   * Remove all players not in the current duel to the teleport location for duel
   * region. Tells the player why
   */
  private void kickOtherPlayersOut(DuelData duel) {
    Location spectateLocation = BukkitAdapter.adapt(DuelConfig.getDuelRegion().getFlag(Flags.TELE_LOC));
    Bukkit.getOnlinePlayers().forEach((Player p) -> {
      if (isInDuelRegion(p) && !p.equals(duel.getDuelee()) && !p.equals(duel.getDueler())) {
        p.teleport(spectateLocation);
        p.sendMessage("You have been removed from the arena because a duel has begun.");
      }
    });
  }

  private boolean isInDuelRegion(Player p) {

    if (!p.hasMetadata("inDuelRegion"))
      return false;
    
    for (MetadataValue v : p.getMetadata("inDuelRegion"))
      if (v.getOwningPlugin().equals(plugin))
        return (boolean) v.value();
    
    return false;
  }
  
  public static void endMatch(Player loser) {
    duelMachine.duel.setState(DuelState.POSTGAME);

    int R_a = getPlayerHonor(getDueler());
    int R_b = getPlayerHonor(getDuelee());
    int N_a = R_a; // new honor a
    int N_b = R_b; // new honor b
    double S_a = 0.5;
    double S_b = 0.5;

    StringBuilder result = new StringBuilder();
    if (loser.equals(getDueler())) {
      S_a = 0;
      S_b = 1;
      int[] honorChange = calculateElo(R_a, R_b, S_a, S_b, DuelConfig.getX(), DuelConfig.getK());
      N_a = honorChange[0] + R_a;
      N_b = honorChange[1] + R_b;
      result.append(getDuelee().getDisplayName());
      result.append(ChatColor.GREEN);
      result.append(" " + N_b + " (+" + honorChange[1] + ")");
      result.append(ChatColor.WHITE);
      result.append(" has beaten ");
      result.append(getDueler().getDisplayName());
      result.append(ChatColor.RED);
      result.append(" " + N_a + " (" + honorChange[0] + ")");
      result.append(ChatColor.WHITE);
      result.append(" with " + getDuelee().getHealth() + "hp");

    } else if (loser.equals(getDuelee())) {
      S_a = 1;
      S_b = 0;

      int[] honorChange = calculateElo(R_a, R_b, S_a, S_b, DuelConfig.getX(), DuelConfig.getK());
      N_a = honorChange[0] + R_a;
      N_b = honorChange[1] + R_b;
      result.append(getDueler().getDisplayName());
      result.append(ChatColor.GREEN);
      result.append(" " + N_a + " (+" + honorChange[0] + ")");
      result.append(ChatColor.WHITE);
      result.append(" has beaten ");
      result.append(getDuelee().getDisplayName());
      result.append(ChatColor.RED);
      result.append(" " + N_b + " (" + honorChange[1] + ")");
      result.append(ChatColor.WHITE);
      result.append(" with " + getDuelee().getHealth() + "hp");

    } else {
      // draw
      int[] honorChange = calculateElo(R_a, R_b, S_a, S_b, DuelConfig.getX(), DuelConfig.getK());
      N_a = honorChange[0] + R_a;
      N_b = honorChange[1] + R_b;
      result.append(honorChange[0] >= 0 ? getDueler().getDisplayName() : getDuelee().getDisplayName());
      result.append(ChatColor.GREEN);
      result.append(" " + N_a + " (+" + honorChange[0] + ")");
      result.append(ChatColor.WHITE);
      result.append(" ran out the duel timer against ");
      result.append(honorChange[0] <= 0 ? getDueler().getDisplayName() : getDuelee().getDisplayName());
      result.append(ChatColor.RED);
      result.append(" " + N_b + " (" + honorChange[1] + ")");
    }

    setPlayerHonor(getDueler(), N_a);
    setPlayerHonor(getDuelee(), N_b);

    Bukkit.broadcastMessage(result.toString());
  }

  public static void onPlayerExitDuelRegion(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    for (DuelData req : duelMachine.duelRequests) {
      if (req.getDueler().equals(p)) {

        return;
      }
    }
  }

  public static void onPlayerEnterDuelRegion(PlayerMoveEvent e) {
    DuelState state = duelMachine.duel.getState();
    if (state == DuelState.IDLE || state == DuelState.WAITING)
      return;
    Player p = e.getPlayer();
    Player dueler = duelMachine.duel.getDueler();
    Player duelee = duelMachine.duel.getDuelee();
    
    if (p.equals(duelee) || p.equals(dueler))
      return;
    
    p.sendMessage("You may not enter " + DuelConfig.getDuelRegion().getFlag(Flags.GREET_TITLE) +
      ChatColor.WHITE + " while a duel is in progress.");
    Location spectateLocation = BukkitAdapter.adapt(DuelConfig.getDuelRegion().getFlag(Flags.TELE_LOC));
    p.teleport(spectateLocation);
  }

  /**
   * Sends text to players
   */
  private void sendText(DuelData duel, String text) {
    duel.getDueler().sendMessage(text);
    duel.getDuelee().sendMessage(text);
  }

  /**
   * Returns whether a new duel between dueler and duelee can occur
   * 
   * @param dueler - player to duel
   * @param duelee - player to duel
   */
  public static void canDuel(Player dueler, Player duelee) throws DuelMachineException {
    if (duelMachine.duel.getState() != DuelState.IDLE)
      throw new DuelMachineException("Wait for the current duel to complete.");
    
    UUID dueleeUUID = duelee.getUniqueId();
    UUID duelerUUID = dueler.getUniqueId();
    for (DuelData d : duelMachine.duelRequests) {
      if (d.getDueler().getUniqueId() == duelerUUID)
        throw new DuelMachineException("Wait for your pending duel request to " + 
          duelee.getDisplayName() + ChatColor.WHITE + " to expire.");

      if (d.getDuelee().getUniqueId() == dueleeUUID)
        throw new DuelMachineException(duelee.getName() + ChatColor.WHITE + 
          " already has a pending duel request.");
    }
  }

  /**
   * Sends messages to players to initate duel
   */
  private static void sendRequestMessages(DuelData req) {
    Player dueler = req.getDueler(), duelee = req.getDuelee();
    double minutes = Math.floor(DuelConfig.getWaitTime() / 6) / 10;

    dueler.sendMessage("Duel request sent to " + duelee.getDisplayName() + ChatColor.WHITE +
      ". They have " + minutes + " minutes to enter the arena.");

    duelee.sendMessage(dueler.getDisplayName() + ChatColor.WHITE + 
      " has challenged you to a duel. To accept, you have " +
      minutes + " minutes to enter the arena");
  }

  /**
   * Sends messages to players about expired duel request
   */
  private static void sendExpiredMessages(DuelData req) {
    Player dueler = req.getDueler(), duelee = req.getDuelee();

    dueler.sendMessage("Duel request to " + duelee.getDisplayName() + ChatColor.WHITE + " has expired.");

    duelee.sendMessage("Duel request from " + dueler.getDisplayName() + ChatColor.WHITE + " has expired.");
  }

  /**
   * Returns the honor value for player p, or null
   * if player p doesn't have an honor value
   * 
   * @param p the player to get honor from
   */
  public static Integer getPlayerHonor(Player p) {
    PersistentDataContainer data = p.getPersistentDataContainer();
    return data.get(new NamespacedKey(duelMachine.plugin, "honor"), PersistentDataType.INTEGER);
  }

  /**
   * Sets the honor value for player p to newHonor,
   * storing it in PersistentDataContainer.
   * 
   * @param p the player ot set honor for
   * @param newHonor the honor to set p's honor value to
   */
  public static void setPlayerHonor(Player p, int newHonor) {
    PersistentDataContainer data = p.getPersistentDataContainer();
    data.set(new NamespacedKey(duelMachine.plugin, "honor"), PersistentDataType.INTEGER, newHonor);
  }

  public static DuelState getState() {
    return duelMachine.duel.getState();
  }

  public static Player getDuelee() {
    return duelMachine.duel.getDuelee();
  }

  public static Player getDueler() {
    return duelMachine.duel.getDueler();
  }

  public static class DuelMachineException extends Throwable {
    private static final long serialVersionUID = 1L;
    private String message;

    public DuelMachineException(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  public static class DuelData {
    private Player duelee;
    private Player dueler;
    private DuelState state;
    private int time; // 1/20 of a second 

    public DuelData(Player dueler, Player duelee) {
      this.dueler = dueler;
      this.duelee = duelee;
      setState(DuelState.WAITING);
    }

    public DuelData() {
      this(null, null);
      setState(DuelState.IDLE);
    }

    public Player getDuelee() {
      return duelee;
    }

    public Player getDueler() {
      return dueler;
    }

    public DuelState getState() {
      return state;
    }

    public void update() {
      time--;
    }

    public int getTime() {
      return time;
    }

    public boolean isExpired() {
      return time <= 0;
    }
    
    /**
     * sets state and time according to state
     */
    public void setState(DuelState state) {
      switch (state) {
        case IDLE:
          time = 0;
          break;
        case INGAME:
          time = DuelConfig.getIngameTime();
          break;
        case POSTGAME:
          time = DuelConfig.getPostgameTime();
          break;
        case PREGAME:
          time = DuelConfig.getPregameTime();
          break;
        case WAITING:
          time = DuelConfig.getWaitTime();
          break;
        default:
          time = 0;
          throw new NullPointerException("Invalid DuelState in setState");
      }
      this.state = state;
    }
  }

}