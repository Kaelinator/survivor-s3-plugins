package com.kaelkirk.machines.duels;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class DuelConfig {
  private Plugin plugin;

  public DuelConfig(Plugin plugin) {
    this.plugin = plugin;
  }

  public ProtectedRegion getDuelRegion() {

    String duelLocation = plugin.getConfig().getString("duelLocation");
    String[] duelSplit = duelLocation.split(":");
    if (duelSplit.length != 2) {
      System.err.println("Invalid duelLocation: " + duelLocation);
      System.out.println(duelSplit.length);
      return null;
    }
    
    World world = Bukkit.getWorld(duelSplit[0]);
    if (world == null) {
      System.err.println("World " + duelSplit[0] + " does not exist");
      return null;
    }

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionManager regions = container.get(BukkitAdapter.adapt(world));
    ProtectedRegion region = regions.getRegion(duelSplit[1]);

    if (region == null)
      System.err.println("Region " + duelSplit[1] + " does not exist in world " + duelSplit[0]);
    
    return region;
  }
}