package com.kaelkirk.machines.worldteleport;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class WorldTeleportConfig {
  private static WorldTeleportConfig wtpConfig = new WorldTeleportConfig();
  private FileConfiguration config;

  private WorldTeleportConfig() { }

  public static WorldTeleportConfig init(Plugin plugin) {
    wtpConfig.config = plugin.getConfig();
    loadWorlds();
    return wtpConfig;
  }

  private static void loadWorlds() {
    List<String> worlds = wtpConfig.config.getStringList("worlds");

    for (String worldName : worlds) 
      Bukkit.getServer().createWorld(new WorldCreator(worldName));

    System.out.println("Loaded all worlds");
  }
}