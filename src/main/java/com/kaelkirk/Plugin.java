package com.kaelkirk;

import com.kaelkirk.machines.duels.DuelCommand;
import com.kaelkirk.machines.duels.DuelConfig;
import com.kaelkirk.machines.duels.DuelMachine;
import com.kaelkirk.machines.duels.DuelScoreboard;
import com.kaelkirk.machines.region.RegionConfig;
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

    WorldTeleportConfig.init(this);
    DuelConfig.init(this);
    RegionConfig.init(this);
    DuelMachine.init(this);

    PluginManager manager = getServer().getPluginManager();

    manager.registerEvents(new DuelScoreboard(), this);
    manager.registerEvents(new RegionEnterEvent(), this);

    getCommand("wtp").setExecutor(new WorldTeleportCommand(this));
    getCommand("duel").setExecutor(new DuelCommand());
  }
 
}
