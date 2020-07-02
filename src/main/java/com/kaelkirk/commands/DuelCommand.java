package com.kaelkirk.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class DuelCommand implements CommandExecutor, TabCompleter {

  // private Plugin plugin;

  public DuelCommand(Plugin plugin) {
    // this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (args.length < 1)
      return false;

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    return null;
  }

}