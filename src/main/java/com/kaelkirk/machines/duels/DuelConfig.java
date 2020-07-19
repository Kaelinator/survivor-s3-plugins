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
  private Integer waitTime;
  private Integer pregameTime;
  private Integer ingameTime;
  private Integer postgameTime;

  private DuelConfig() { }

  public static DuelConfig getInstance() {
    return duelConfig;
  }

  public static void init(Plugin plugin) {
    duelConfig.config = plugin.getConfig();
  }

  public static int getPregameTime() {
    if (duelConfig.pregameTime != null)
      return duelConfig.pregameTime;

    duelConfig.pregameTime = duelConfig.config.getInt("duel.pregameTime");
    return duelConfig.pregameTime;
  }

  public static int getWaitTime() {
    if (duelConfig.waitTime != null)
      return duelConfig.waitTime;

    duelConfig.waitTime = duelConfig.config.getInt("duel.waitTime");
    return duelConfig.waitTime;
  }

  public static int getIngameTime() {
    if (duelConfig.ingameTime != null)
      return duelConfig.ingameTime;

    duelConfig.ingameTime = duelConfig.config.getInt("duel.ingameTime");
    return duelConfig.ingameTime;
  }

  public static int getPostgameTime() {
    if (duelConfig.postgameTime != null)
      return duelConfig.postgameTime;

    duelConfig.postgameTime = duelConfig.config.getInt("duel.postgameTime");
    return duelConfig.postgameTime;
  }

  public static int getInitialHonor() {
    if (duelConfig.initialHonor != null)
      return duelConfig.initialHonor;
    
    duelConfig.initialHonor = duelConfig.config.getInt("duel.initialHonor");
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