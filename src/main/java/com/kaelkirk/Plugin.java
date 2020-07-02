package com.kaelkirk;

import com.kaelkirk.commands.DuelCommand;
import com.kaelkirk.commands.WorldTeleportCommand;
import com.kaelkirk.machines.duels.DuelMachine;
import com.kaelkirk.machines.duels.DuelScoreboard;

import org.bukkit.plugin.PluginManager;
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
    DuelMachine duelMachine = new DuelMachine(this);
    PluginManager manager = getServer().getPluginManager();
    manager.registerEvents(new DuelScoreboard(this, duelMachine), this);
    getCommand("wtp").setExecutor(new WorldTeleportCommand(this));
    getCommand("duel").setExecutor(new DuelCommand(this, duelMachine));
    ConfigHandler config = new ConfigHandler(this);
    config.loadWorlds();
  }
 
}
