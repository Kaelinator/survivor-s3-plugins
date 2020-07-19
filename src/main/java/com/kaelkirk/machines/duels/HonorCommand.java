package com.kaelkirk.machines.duels;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HonorCommand implements CommandExecutor {
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1)
      return false;

    if (!sender.isOp()) {
      sender.sendMessage("You do not have permission to use this command!");
      return true;
    }
    
    Player target = Bukkit.getPlayer(args[0]);
    if (target == null) {
      sender.sendMessage("Cannot find player " + args[0]);
      return true;
    }

    if (args.length < 2) {
      int honor = DuelMachine.getPlayerHonor(target);

      sender.sendMessage(target.getDisplayName() + ChatColor.WHITE + " has " + honor + " honor.");
      return true;
    }

    int honor = 0;
    try {
      honor = Integer.parseInt(args[1]);
    } catch (Exception e) {
      return false;
    }
  
    DuelMachine.setPlayerHonor(target, honor);
    sender.sendMessage("Set " + target.getDisplayName() + ChatColor.WHITE + "'s honor to " + honor + ".");
    return true;
  }
}