package com.kaelkirk.machines.duels;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class DuelConfig {

  private static DuelConfig duelConfig = new DuelConfig();
  private FileConfiguration config;
  private ProtectedRegion duelRegion;
  private Integer initialHonor;

  private DuelConfig() { }

  public static DuelConfig getInstance() {
    return duelConfig;
  }

  public static DuelConfig init(Plugin plugin) {
    duelConfig.config = plugin.getConfig();
    return duelConfig;
  }

  public static int getInitialHonor() {
    if (duelConfig.initialHonor != null)
      return duelConfig.initialHonor;
    
    duelConfig.initialHonor = duelConfig.config.getInt("duel.duelConfig.initialHonor");
    return duelConfig.initialHonor;
  }

  public static ProtectedRegion getDuelRegion() {
    /* use cached value */
    if (duelConfig.duelRegion != null)
      return duelConfig.duelRegion;

    String duelLocation = duelConfig.config.getString("duel.location");
    String[] duelSplit = duelLocation.split(":");
    if (duelSplit.length != 2) {
      System.err.println("Invalid duel location: " + duelLocation);
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
    duelConfig.duelRegion = regions.getRegion(duelSplit[1]);

    if (duelConfig.duelRegion == null)
      System.err.println("Region " + duelSplit[1] + " does not exist in world " + duelSplit[0]);
    
    return duelConfig.duelRegion;
  }
}