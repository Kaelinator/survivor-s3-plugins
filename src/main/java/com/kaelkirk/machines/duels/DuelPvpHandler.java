package com.kaelkirk.machines.duels;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class DuelPvpHandler implements Listener {

  private Plugin plugin;

  public DuelPvpHandler(Plugin plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onDisallowedPVPEvent(DisallowedPVPEvent e) {
    if (DuelMachine.getState() != DuelState.INGAME)
      return;

    Player attacker = e.getAttacker();
    Player defender = e.getDefender();
    Player dueler = DuelMachine.getDueler();
    Player duelee = DuelMachine.getDuelee();
    

    if ((attacker.equals(dueler) || attacker.equals(duelee)) &&
      (defender.equals(dueler) || defender.equals(duelee)))
        e.setCancelled(true);
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    if (DuelMachine.getState() != DuelState.INGAME)
      return;
    
    Player p = e.getEntity();
    p.setMetadata("inDuelRegion", new FixedMetadataValue(plugin, false));
    Player dueler = DuelMachine.getDueler();
    Player duelee = DuelMachine.getDuelee();
    
    if (!p.equals(duelee) && !p.equals(dueler))
      return;
    
    DuelMachine.endMatch(p);
  }
}