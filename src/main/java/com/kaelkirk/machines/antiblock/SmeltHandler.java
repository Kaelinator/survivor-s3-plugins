package com.kaelkirk.machines.antiblock;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class SmeltHandler implements Listener {
  
  @EventHandler
  public void onSmelt(FurnaceSmeltEvent e) {
    if (e.getResult().getType().toString().endsWith("GLAZED_TERRACOTTA")) {
      e.getResult().setType(Material.BRICKS);
    }
  }

  @EventHandler
  public void onTrySmelt(FurnaceBurnEvent e) {
    Furnace f = (Furnace) e.getBlock().getState();
    if (f.getInventory().getSmelting().getType().toString().endsWith("_TERRACOTTA")) {
      e.setCancelled(true);
    }
  }
}