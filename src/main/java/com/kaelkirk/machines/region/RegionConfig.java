package com.kaelkirk.machines.region;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class RegionConfig {
  
  private static RegionConfig regionConfig = new RegionConfig();
  private FileConfiguration config;

  private RegionConfig() { }

  public static RegionConfig init(Plugin plugin) {
    regionConfig.config = plugin.getConfig();
    return regionConfig;
  }

  public static boolean getOpsDiscoverRegions() {
    return regionConfig.config.getBoolean("opsDiscoverRegions");
  }

  public static DiscoverableRegion[] getDiscoverableRegions() {
    ConfigurationSection regions = regionConfig.config.getConfigurationSection("discoverableRegions");

    return regions.getKeys(false)
      .stream()
      .map((String key) -> regions.getConfigurationSection(key))
      .flatMap((ConfigurationSection world) -> {

        return world.getKeys(false)
          .stream()
          .map((String key) -> world.getConfigurationSection(key))
          .filter((ConfigurationSection region) -> region.contains("block"))
          .filter((ConfigurationSection region) -> region.contains("blockLocation"))
          .map((ConfigurationSection region) -> new DiscoverableRegion(world.getName(), region));
      })
      .toArray(DiscoverableRegion[]::new);
  }
  
}