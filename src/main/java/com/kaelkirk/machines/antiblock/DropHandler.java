package com.kaelkirk.machines.antiblock;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class DropHandler implements Listener {

  @EventHandler
  public void onDrop(BlockDropItemEvent e) {
    removeBlocksFromList(e.getItems());
  }


  @EventHandler
  public void onBedExplode(BlockExplodeEvent e) {
    removeBlocksFromList(e.blockList());
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent e) {
    removeBlocksFromList(e.blockList());
  }

  private void removeBlocksFromList(List<?> blocks) {
    Iterator<?> itr = blocks.iterator();
    while (itr.hasNext()) {
      Object next = itr.next();
      Material type = (next instanceof Item)
        ? ((Item) next).getItemStack().getType()
        : ((Block) next).getType();
      switch (type) {
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
          itr.remove();
          break;
        default: break;
      }
    }
  }
}