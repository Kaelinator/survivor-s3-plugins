package com.kaelkirk.machines.region;

import com.kaelkirk.machines.duels.DuelConfig;
import com.kaelkirk.machines.duels.DuelMachine;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class RegionChangeHandler implements Listener {

  private RegionQuery regionQuery;
  private DiscoverableRegion[] regions;
  private Plugin plugin;

  public RegionChangeHandler(Plugin plugin) {
    this.plugin = plugin;

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

    Location location = BukkitAdapter.adapt(p.getLocation());
    ApplicableRegionSet set = regionQuery.getApplicableRegions(location);

    boolean inDuelRegion = false;
    for (ProtectedRegion applicableRegion : set) {
      if (applicableRegion.equals(DuelConfig.getDuelRegion()))
        inDuelRegion = true;
      for (DiscoverableRegion region : regions) 
        if (region.isDiscovered() && (!RegionConfig.getOpsDiscoverRegions() && p.isOp()))
          continue;
        else if (applicableRegion.equals(region.getRegion()))
          triggerDiscover(region, p);
    }

    handleDuelRegion(p, inDuelRegion, e);
  }

  private void handleDuelRegion(Player p, boolean inDuelRegion, PlayerMoveEvent e) {
    if (p.hasMetadata("inDuelRegion")) {
      boolean wasInDuelRegion = false;
      for (MetadataValue v : p.getMetadata("inDuelRegion"))
        if (v.getOwningPlugin().equals(plugin))
          wasInDuelRegion = (boolean) v.value();
      
      if (wasInDuelRegion && !inDuelRegion) 
        DuelMachine.onPlayerExitDuelRegion(e);
      
      if (!wasInDuelRegion && inDuelRegion)
        DuelMachine.onPlayerEnterDuelRegion(e);
    }

    p.setMetadata("inDuelRegion", new FixedMetadataValue(plugin, inDuelRegion));
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