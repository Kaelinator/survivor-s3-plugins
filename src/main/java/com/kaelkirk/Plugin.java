package com.kaelkirk;

import com.kaelkirk.machines.duels.DuelCommand;
import com.kaelkirk.machines.duels.DuelMachine;
import com.kaelkirk.machines.duels.DuelScoreboard;
import com.kaelkirk.machines.region.RegionEnterEvent;
import com.kaelkirk.machines.worldteleport.WorldTeleportCommand;
import com.kaelkirk.machines.worldteleport.WorldTeleportConfig;

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
    manager.registerEvents(new RegionEnterEvent(this), this);

    getCommand("wtp").setExecutor(new WorldTeleportCommand(this));
    getCommand("duel").setExecutor(new DuelCommand(this, duelMachine));

    WorldTeleportConfig config = new WorldTeleportConfig(this);
    config.loadWorlds();
  }
 
}
