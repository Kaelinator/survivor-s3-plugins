package com.kaelkirk.machines.duels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class DuelScoreboard implements Listener {
  
  private Plugin plugin;
  private DuelMachine duelMachine;

  public DuelScoreboard(Plugin plugin, DuelMachine duelMachine) {
    this.plugin = plugin;
    this.duelMachine = duelMachine;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    Integer honor = duelMachine.getPlayerHonor(player);
    if (honor == null) {
      duelMachine.setPlayerHonor(player, plugin.getConfig().getInt("initialHonor"));
    }
  }
}