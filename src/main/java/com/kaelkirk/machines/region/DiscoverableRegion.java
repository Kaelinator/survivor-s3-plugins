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
      throw new NullPointerException("Invalid blockLocation at " + worldName + "." + regionId);
    }

    blockType = Material.matchMaterial(block);

    if (blockType == null)
      throw new NullPointerException("Invalid material \"" + block + "\" at " + worldName + "." + regionId);
    
    World world = Bukkit.getWorld(worldName);

    if (world == null)
      throw new NullPointerException("Cannot find world named \"" + worldName + "\"");

    location = new Location(world, x, y, z);

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionManager regions = container.get(BukkitAdapter.adapt(world));
    region = regions.getRegion(regionId);

    if (region == null)
      throw new NullPointerException("Region " + regionId + " does not exist in world " + worldName);
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