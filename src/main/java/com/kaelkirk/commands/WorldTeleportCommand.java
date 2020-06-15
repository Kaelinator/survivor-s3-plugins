package com.kaelkirk.commands;

import com.kaelkirk.Plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldTeleportCommand implements CommandExecutor {
  Plugin plugin;

  public WorldTeleportCommand(Plugin plugin) {
    this.plugin = plugin;
	}

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
    sender.sendMessage("Okay then!");
    return false;
  }
}