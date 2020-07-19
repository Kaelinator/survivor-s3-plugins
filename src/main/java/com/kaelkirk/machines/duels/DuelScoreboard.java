package com.kaelkirk.machines.duels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class DuelScoreboard implements Listener {
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    Integer honor = DuelMachine.getPlayerHonor(player);
    if (honor == null) {
      DuelMachine.setPlayerHonor(player, DuelConfig.getInitialHonor());
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
    score.setScore(DuelMachine.getPlayerHonor(p));
    p.setScoreboard(board);
  }
}