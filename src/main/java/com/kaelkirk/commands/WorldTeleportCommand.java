package com.kaelkirk.commands;

import java.util.ArrayList;
import java.util.List;

import com.kaelkirk.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class WorldTeleportCommand implements CommandExecutor, TabCompleter {
  Plugin plugin;

  public WorldTeleportCommand(Plugin plugin) {
    this.plugin = plugin;
	}

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    sender.sendMessage("Teleporting to " + args[0] + ".");
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> suggestions = new ArrayList<String>();

    for (World world : Bukkit.getServer().getWorlds()) {
      suggestions.add(world.getName());
    }

    return suggestions;
  }
}