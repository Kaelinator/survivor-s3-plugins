package com.kaelkirk.machines.duels;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DuelCommand implements CommandExecutor {

  private DuelMachine duelMachine;
  private RegionQuery regionQuery;
  private DuelConfig config;

  public DuelCommand(DuelMachine duelMachine) {
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    this.regionQuery = container.createQuery();
    this.duelMachine = duelMachine;
    config = new DuelConfig(plugin);
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

    // if (duelee.getUniqueId().equals(dueler.getUniqueId())) {
    //   sender.sendMessage("You may not duel yourself.");
    //   return true;
    // }

    if (!playerIsInRegion(dueler, config.getDuelRegion())) {
      sender.sendMessage("You must be in " + config.getDuelRegion().getFlag(Flags.GREET_TITLE) +
        ChatColor.WHITE + " in order to duel.");
      return true;
    }

    duelMachine.initiateNewDuel(dueler, duelee);

    return true;
  }

  /**
   * Returns whether p is in region
   * 
   * @param p - the player to check
   * @param region - the region p must be in
   */
  private boolean playerIsInRegion(Player p, ProtectedRegion region) {

    Location location = BukkitAdapter.adapt(p.getLocation());
    ApplicableRegionSet set = regionQuery.getApplicableRegions(location);

    for (ProtectedRegion applicableRegion : set)
      if (applicableRegion.equals(region))
        return true;

    return false;
  }

}