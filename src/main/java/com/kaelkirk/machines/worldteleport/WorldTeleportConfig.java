package com.kaelkirk.machines.worldteleport;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

public class WorldTeleportConfig {
  private Plugin plugin;

  public WorldTeleportConfig(Plugin plugin) {
    this.plugin = plugin;
  }

  public void loadWorlds() {
    List<String> worlds = plugin.getConfig().getStringList("worlds");

    for (String worldName : worlds) 
      Bukkit.getServer().createWorld(new WorldCreator(worldName));

    System.out.println("Loaded all worlds");
  }
}