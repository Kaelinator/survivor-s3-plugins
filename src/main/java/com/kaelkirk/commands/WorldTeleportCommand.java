package com.kaelkirk.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.kaelkirk.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class WorldTeleportCommand implements CommandExecutor, TabCompleter {
  Plugin plugin;

  public WorldTeleportCommand(Plugin plugin) {
    this.plugin = plugin;
	}

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    String worldName = Arrays.asList(args).stream().collect(Collectors.joining(" "));

    World targetWorld = Bukkit.getServer().getWorld(worldName);

    if (targetWorld == null) {
      sender.sendMessage(worldName + " does not exist.");
      return true;
    }

    if (sender instanceof Player) {
      Player player = (Player) sender;

      player.teleport(targetWorld.getSpawnLocation());
      sender.sendMessage("Teleporting to " + worldName + ".");
    }


    return true;
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