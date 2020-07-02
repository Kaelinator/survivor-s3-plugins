package com.kaelkirk.commands;

import java.util.List;

import com.kaelkirk.machines.duels.DuelMachine;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class DuelCommand implements CommandExecutor, TabCompleter {

  private Plugin plugin;
  private DuelMachine duelMachine;

  public DuelCommand(Plugin plugin, DuelMachine duelMachine) {
    this.plugin = plugin;
    this.duelMachine = duelMachine;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (args.length < 1)
      return false;

    if (!(sender instanceof Player)) {
      sender.sendMessage("Only players may use /duel.");
      return true;
    }

    Player dueler = (Player) sender;

    String dueleeName = args[0];
    Player duelee = Bukkit.getPlayerExact(dueleeName);

    if (duelee == null) {
      sender.sendMessage("Player " + dueleeName + " not found.");
      return true;
    }

    if (duelee.getUniqueId().equals(dueler.getUniqueId())) {
      sender.sendMessage("You may not duel yourself.");
      return true;
    }

    duelMachine.initiateNewDuel(dueler, duelee);

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    return null;
  }

}