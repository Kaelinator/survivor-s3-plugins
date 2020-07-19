package com.kaelkirk.machines.duels;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class DuelScoreboard implements Listener {

  HashMap<UUID, Integer> scheduledIds;
  private Plugin plugin;

  public DuelScoreboard(Plugin plugin) {
    scheduledIds = new HashMap<UUID, Integer>();
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    Integer honor = DuelMachine.getPlayerHonor(player);
    if (honor == null) {
      DuelMachine.setPlayerHonor(player, DuelConfig.getInitialHonor());
    }
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
      @Override
      public void run() {
        displayHonorScoreboard(player);
      }
    }, 0, 20);
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent e) {

    Player player = e.getPlayer();
    if (player == null)
      return;
    UUID id = player.getUniqueId();
    Integer scheduleId = scheduledIds.get(id);
    if (scheduleId == null)
      return;

    Bukkit.getScheduler().cancelTask(scheduledIds.get(id));
  }

  /**
   * Sets the honor's display to the sidebar for player p.
   * 
   * @param p the player to set the honor display for
   */
  private void displayHonorScoreboard(Player p) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
    Objective displayObj = board.registerNewObjective("display", "dummy", " ");
    displayObj.setDisplaySlot(DisplaySlot.SIDEBAR);
    Score score = displayObj.getScore("Honor:");
    score.setScore(DuelMachine.getPlayerHonor(p));
    p.setScoreboard(board);
  }
}