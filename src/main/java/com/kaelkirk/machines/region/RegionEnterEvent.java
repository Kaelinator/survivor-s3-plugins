package com.kaelkirk.machines.region;

import com.kaelkirk.Plugin;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class RegionEnterEvent implements Listener {

  private RegionQuery regionQuery;
  private DiscoverableRegion[] regions;
  
  public RegionEnterEvent() {

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    this.regionQuery = container.createQuery();

    regions = RegionConfig.getDiscoverableRegions();

    /* load whether the regions have been found */
    for (DiscoverableRegion region : regions) {
      if (region.getLocation().getBlock().getType() == region.getBlockType())
        region.setDiscovered(true);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    if (!RegionConfig.getOpsDiscoverRegions() && p.isOp())
      return;

    Location location = BukkitAdapter.adapt(p.getLocation());
    ApplicableRegionSet set = regionQuery.getApplicableRegions(location);

    for (ProtectedRegion applicableRegion : set) {
      for (DiscoverableRegion region : regions) 
        if (region.isDiscovered())
          continue;
        else if (applicableRegion.equals(region.getRegion()))
          triggerDiscover(region, p);
    }
  }

  /**
   * Makes a big deal out of discovering a region:
   * - Sets the DiscoverableRegion to discovered
   * - places the block with material type
   * - Sends a global message
   * - Spawns a firework at the player
   */
  private void triggerDiscover(DiscoverableRegion region, Player by) {

    region.setDiscovered(true);
    region.getLocation().getBlock().setType(region.getBlockType());
    Bukkit.broadcastMessage(region.getRegion().getFlag(Flags.GREET_TITLE) + 
      ChatColor.WHITE + " has been discovered by " + by.getDisplayName());

    spawnFirework(region.getLocation());
    spawnFirework(by.getLocation());
  }

  private void spawnFirework(org.bukkit.Location location) {
    Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
    FireworkMeta meta = fw.getFireworkMeta();

    meta.setPower(2);
    meta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).flicker(true).build());

    fw.setFireworkMeta(meta);
    fw.detonate();
  }
}