package com.kaelkirk.machines.worldteleport;

import java.util.ArrayList;
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
  // private Plugin plugin;

  public WorldTeleportCommand(Plugin plugin) {
    // this.plugin = plugin;
	}

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (sender instanceof Player && !((Player) sender).isOp()) {
      sender.sendMessage("You do not have permission to use this command.");
      return true;
    }

    String worldName = null;
    String selector = null;

    switch (args.length) {
      case 0:
        sender.sendMessage("No world specified.");
        return false;

      case 1: // interpret as the world
        worldName = args[0].replaceAll("/", " ");
        break;
      
      case 2:
        selector = args[0];
        worldName = args[1].replaceAll("/", " ");
        break;

      default:
        sender.sendMessage("Exceeded maximum arguments: " + args.length + " > 2.");
        return false;
    }

    World targetWorld = Bukkit.getServer().getWorld(worldName);

    if (targetWorld == null) {
      sender.sendMessage("World " + worldName + " does not exist.");
      return true;
    }

    Player target = null;

    if (selector != null) {
      target = Bukkit.getPlayerExact(selector);
      if (target == null) {
        sender.sendMessage("Selector not found: " + selector);
        return true;
      }
      sender.sendMessage("Teleporting " + target.getName() + " to " + worldName + ".");
    } else if (sender instanceof Player) {
      target = (Player) sender;
    } else {
      sender.sendMessage("No selector specified.");
      return false;
    }

    target.sendMessage("Teleporting to " + worldName + ".");
    target.teleport(targetWorld.getSpawnLocation());

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> suggestions = new ArrayList<String>();

    List<String> worldSuggestions = Bukkit.getServer().getWorlds().stream()
      .map((World w) -> w.getName().replaceAll(" ", "/"))
      .filter((String name) -> name.startsWith(args[args.length - 1]))
      .collect(Collectors.toList());

    List<String> nameSuggestions = Bukkit.getOnlinePlayers().stream()
      .map((Player p) -> p.getName())
      .filter((String name) -> name.startsWith(args[args.length - 1]))
      .collect(Collectors.toList());
    
    suggestions.addAll(worldSuggestions);
    suggestions.addAll(nameSuggestions);

    return suggestions;
  }
}