package com.kaelkirk;

import com.kaelkirk.commands.WorldTeleportCommand;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class Plugin extends JavaPlugin {
 
  @Override
  public void onDisable() {

  }

  @Override
  public void onEnable() {
    System.out.println("SAVING DEFAULT CONFIG");
    this.saveDefaultConfig();
    getCommand("wtp").setExecutor(new WorldTeleportCommand(this));
  }
 
}
