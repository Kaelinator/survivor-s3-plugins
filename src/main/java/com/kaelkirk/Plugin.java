package com.kaelkirk;

import com.kaelkirk.commands.DuelCommand;
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
    this.saveDefaultConfig();
    getCommand("wtp").setExecutor(new WorldTeleportCommand(this));
    getCommand("duel").setExecutor(new DuelCommand(this));
    ConfigHandler config = new ConfigHandler(this);
    config.loadWorlds();
  }
 
}
