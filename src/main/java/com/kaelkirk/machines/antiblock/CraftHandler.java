package com.kaelkirk.machines.antiblock;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CraftHandler implements Listener {
  
  @EventHandler
  public void onCraft(PrepareItemCraftEvent e) {

    Recipe r = e.getRecipe();
    if (r == null)
      return;
    
    Material itemType = r.getResult().getType();
    switch (itemType) {
      case BONE_BLOCK:
      case NETHER_GOLD_ORE:
      case ANCIENT_DEBRIS:
      case DARK_PRISMARINE:
      case PURPUR_BLOCK:
      case WARPED_HYPHAE:
      case CUT_SANDSTONE:
      case PRISMARINE:
      case MAGMA_BLOCK:
      case EMERALD_BLOCK:
        e.getInventory().setResult(new ItemStack(Material.AIR));
        return;
      default: return;
    }
  }
}