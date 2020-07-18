package com.kaelkirk.machines.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class DiscoverableRegion {

  private ProtectedRegion region;
  private Material blockType;
  private Location location;
  private boolean discovered;

  public DiscoverableRegion(String worldName, ConfigurationSection region) {
    this(worldName, region.getName(), (String) region.get("block"), (String) region.get("blockLocation"));
  }

  public DiscoverableRegion(String worldName, String regionId, String block, String blockLocation) {
    double x = -1, y = -1, z = -1;
    String[] loc = blockLocation.split(" ");
    try {
      x = Double.parseDouble(loc[0]);
      y = Double.parseDouble(loc[1]);
      z = Double.parseDouble(loc[2]);
    } catch (Exception e) {
      System.err.println("Invalid blockLocation at " + worldName + "." + regionId);
    }

    String blockName = block.startsWith("minecraft:") ? block.split(":")[1] : block;

    blockType = Material.matchMaterial(blockName);

    if (blockType == null)
      System.err.println("Invalid material " + block + " at " + worldName + "." + regionId);
    
    World world = Bukkit.getWorld(worldName);

    if (world == null)
      System.err.println("Cannot find world " + world);

    location = new Location(world, x, y, z);

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionManager regions = container.get(BukkitAdapter.adapt(world));
    region = regions.getRegion(regionId);

    if (region == null)
      System.err.println("Region " + regionId + " does not exist in world " + worldName);
  }

  public ProtectedRegion getRegion() {
    return this.region;
  }

  public Location getLocation() {
    return this.location;
  }

  public Material getBlockType() {
    return this.blockType;
  }

  public void setDiscovered(boolean discovered) {
    this.discovered = discovered;
  }

  public boolean isDiscovered() {
    return this.discovered;
  }

}