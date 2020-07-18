package com.kaelkirk.machines.region;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class RegionConfig {
  
  private Plugin plugin;

  public RegionConfig(Plugin plugin) {
    this.plugin = plugin;
  }

  public boolean getOpsDiscoverRegions() {
    return plugin.getConfig().getBoolean("opsDiscoverRegions");
  }

  public DiscoverableRegion[] getDiscoverableRegions() {
    ConfigurationSection regions = plugin.getConfig().getConfigurationSection("discoverableRegions");

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