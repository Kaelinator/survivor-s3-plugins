package com.kaelkirk.machines.duels;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class DuelMachine {
  
  private Plugin plugin;
  private DuelState state;

  public DuelMachine(Plugin plugin) {
    this.plugin = plugin;
    
  }

  /**
   * Initiates new duel between p1 and p2
   * @param p1 - player to duel
   * @param p2 - player to duel
   */
  public boolean initiateNewDuel(Player p1, Player p2) {
    return false;
  }

  /**
   * Returns the honor value for player p, or null
   * if player p doesn't have an honor value
   * 
   * @param p the player to get honor from
   */
  public Integer getPlayerHonor(Player p) {
    PersistentDataContainer data = p.getPersistentDataContainer();
    return data.get(new NamespacedKey(plugin, "honor"), PersistentDataType.INTEGER);
  }

  /**
   * Sets the honor value for player p to newHonor,
   * storing it in PersistentDataContainer.
   * 
   * @param p the player ot set honor for
   * @param newHonor the honor to set p's honor value to
   */
  public void setPlayerHonor(Player p, int newHonor) {
    PersistentDataContainer data = p.getPersistentDataContainer();
    data.set(new NamespacedKey(plugin, "honor"), PersistentDataType.INTEGER, newHonor);
  }
}