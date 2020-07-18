package com.kaelkirk.machines.region;

import com.kaelkirk.Plugin;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionEnterEvent implements Listener {

  private Plugin plugin;
  private RegionQuery regionQuery;
  
  public RegionEnterEvent(Plugin plugin) {
    this.plugin = plugin;

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    this.regionQuery = container.createQuery();

  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    /* Ops shouldn't trigger a region found */
    // if (p.isOp())
    //   return;

    Location location = BukkitAdapter.adapt(p.getLocation());
    ApplicableRegionSet set = regionQuery.getApplicableRegions(location);

    set.forEach((ProtectedRegion r) -> p.sendMessage("You are in region " + r.getId() + "."));
  }
}