package com.kaelkirk.machines.region;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class RegionConfig {
  
  private Plugin plugin;

  public RegionConfig(Plugin plugin) {
    this.plugin = plugin;
  }

  public Set<String> getDiscoverableRegions() {
    ConfigurationSection regions = plugin.getConfig().getConfigurationSection("discoverableRegions");
    Set<String> discoverableRegions = regions.getKeys(true);
    return discoverableRegions;
  }
}