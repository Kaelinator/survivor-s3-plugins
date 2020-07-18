package com.kaelkirk.machines.duels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class DuelScoreboard implements Listener {
  
  private DuelMachine duelMachine;

  public DuelScoreboard(DuelMachine duelMachine) {
    this.duelMachine = duelMachine;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    Integer honor = duelMachine.getPlayerHonor(player);
    if (honor == null) {
      duelMachine.setPlayerHonor(player, DuelConfig.getInitialHonor());
    }
    displayHonorScoreboard(player);
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
    score.setScore(duelMachine.getPlayerHonor(p));
    p.setScoreboard(board);
  }
}