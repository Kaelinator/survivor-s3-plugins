package com.kaelkirk.machines.duels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import com.kaelkirk.machines.duels.DuelMachine.DuelRequest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class DuelMachine implements Runnable {

  private static DuelMachine duelMachine = new DuelMachine();
  private ArrayList<DuelRequest> duelRequests;
  private Plugin plugin;
  private DuelState state;

  private DuelMachine() { }

  public static void init(Plugin plugin) {
    duelMachine.plugin = plugin;
    duelMachine.state = DuelState.IDLE;
    duelMachine.duelRequests = new ArrayList<DuelRequest>();
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, duelMachine, 0, 20);
  }

  /**
   * Initiates new duel between dueler and duelee
   * 
   * @param dueler - player to duel
   * @param duelee - player to duel
   */
  public static void initiateNewDuel(Player dueler, Player duelee) {
    sendRequestMessages(duelee, dueler);

    duelMachine.duelRequests.add(new DuelRequest(dueler, duelee, DuelConfig.getWait()));
  }

  /* Game loop */
  @Override
  public void run() {
    Iterator<DuelRequest> itr = duelMachine.duelRequests.iterator();

    while (itr.hasNext()) {
      DuelRequest req = itr.next();
      req.update();
      if (req.isExpired()) {
      }
    }
    
  }
  
  /**
   * Returns whether a new duel between dueler and duelee can occur
   * @param dueler - player to duel
   * @param duelee - player to duel
   */
  public static void canDuel(Player dueler, Player duelee) throws DuelMachineException {
    if (duelMachine.state != DuelState.IDLE)
      throw new DuelMachineException("Wait for the current duel to complete.");
    
    UUID dueleeUUID = duelee.getUniqueId();
    UUID duelerUUID = dueler.getUniqueId();
    for (DuelRequest d : duelMachine.duelRequests) {
      if (d.getDueler().getUniqueId() == duelerUUID)
        throw new DuelMachineException("Wait for your pending duel request to " + 
          duelee.getDisplayName() + ChatColor.WHITE + " to expire.");

      if (d.getDuelee().getUniqueId() == dueleeUUID)
        throw new DuelMachineException(duelee.getName() + ChatColor.WHITE + 
          " already has a pending duel request.");
    }
  }

  /**
   * Sends messages to player to initate duel
   */
  private static void sendRequestMessages(Player duelee, Player dueler) {
    double minutes = Math.floor(DuelConfig.getWait() / 6) / 10;
    dueler.sendMessage("Duel request sent to " + duelee.getDisplayName() + ChatColor.WHITE +
      ". They have " + minutes + " minutes to enter the arena.");

    duelee.sendMessage(dueler.getDisplayName() + ChatColor.WHITE + 
      " has challenged you to a duel. To accept, you have " +
      minutes + " minutes to enter the arena");
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
    return duelMachine.state;
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

  public static class DuelRequest {
    private Player duelee;
    private Player dueler;
    private int waitTime; // 1/20 of a second 

    public DuelRequest(Player dueler, Player duelee, int waitTime) {
      this.dueler = dueler;
      this.duelee = duelee;
      this.waitTime = waitTime;
    }

    public Player getDuelee() {
      return duelee;
    }

    public Player getDueler() {
      return dueler;
    }

    public void update() {
      waitTime--;
    }

    public boolean isExpired() {
      return waitTime <= 0;
    }
  }
}