package com.kaelkirk;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class ConfigHandler {
  private Plugin plugin;
  
  public ConfigHandler(Plugin plugin) {
    this.plugin = plugin;
  }

  public void loadWorlds() {
    List<String> worlds = plugin.getConfig().getStringList("worlds");

    for (String worldName : worlds) 
      Bukkit.getServer().createWorld(new WorldCreator(worldName));

    System.out.println("Loaded all worlds");
  }
}