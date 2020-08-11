package me.theminddroid.contrabandasker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Commands implements CommandExecutor {

    private static Set<UUID> cooldownSet;

    public Commands() {
        cooldownSet = new HashSet<>();
    }

    public static Set<UUID> getCooldownSet () {
        return cooldownSet;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (tryRunCommand(command, args, player, "sword", " §3has asked you to give up your §6Sword§3.",
                "You do not have permission to ask for someone's sword.")) {
            return true;
        }

        if (tryRunCommand(command, args, player, "bow", " §3has asked you to give up your §6Bow§3.",
                "You do not have permission to ask for someone's bow.")) {
            return true;
        }

        if (tryRunCommand(command, args, player, "pot", " §3has asked you to hand over your §6Potion§3.",
                "You do not have permission to ask for someone's potion.")) {
            return true;
        }

        if (tryRunCommand(command, args, player, "drug", " §3has asked you to give up your §6Drugs§3.",
                "You do not have permission to ask for someone's drugs.")) {
            return true;
        }

        if (tryRunCommand(command, args, player, "arrow", " §3has asked you to give up your §6Arrows§3.",
                "You do not have permission to ask for someone's arrows"))
            return true;

        if (tryRunCommand(command, args, player, "out", " §3has asked you to exit the §6Safe Zone§3.",
                "You do not have permission to ask someone to leave a save zone."))
            return true;

        return false;
    }

    /**
     * Attempts to run contraband command.
     * @param command Command run by user.
     * @param args Arguments sent by user.
     * @param player Player who sent command.
     * @param contraband Contraband to handle.
     * @param permissionErrorMessage Message sent to users who lack permission.
     * @return Returns true when command is handled.
     */

    private boolean tryRunCommand(Command command, String[] args, Player player, String contraband, String displayedMessage, String permissionErrorMessage) {
        if (!command.getName().equals(contraband)) {
            return false;
        }

        if (!player.hasPermission("ContrabandAsker." + contraband)) {
            player.sendMessage(ChatColor.RED + permissionErrorMessage);
            return true;
        }

        runCommand(player, contraband, args, displayedMessage);
        return true;
    }


    private void runCommand(Player player, String contraband, String[] args, String displayedMessage) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You must give the command the correct argument.");
            player.sendMessage(ChatColor.RED + "/" + contraband + " <player>");
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "Your target " + args[0] + " is not online!");
                return;
            } else {

                if (cooldownSet.contains(target.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You must wait to ask that player again.");
                    return;
                }

                if (contraband.equals("out")) {
                    player.sendMessage(ChatColor.DARK_RED + "[ContrabandAsker]: " + ChatColor.DARK_AQUA + "You have asked " + ChatColor.GOLD
                            + target.getDisplayName() + ChatColor.DARK_AQUA
                            + " to leave the " + ChatColor.GOLD + "save zone" + ChatColor.DARK_AQUA + ". They have" + ChatColor.GOLD
                            + " 5 seconds" + ChatColor.DARK_AQUA + " to comply.");

                    Bukkit.broadcast(ChatColor.DARK_RED + "[ContrabandAsker]: " + ChatColor.GOLD + player.getDisplayName()
                            + ChatColor.DARK_AQUA + " has asked " + ChatColor.GOLD + target.getDisplayName() + ChatColor.DARK_AQUA
                            + " to leave the " + ChatColor.GOLD + "safe zone" + ChatColor.DARK_AQUA + ".", "ContrabandAsker.broadcast");
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "[ContrabandAsker]: " + ChatColor.DARK_AQUA + "You have asked "
                            + ChatColor.GOLD + target.getDisplayName() + ChatColor.DARK_AQUA + " to give up their "
                            + ChatColor.GOLD + contraband + ChatColor.DARK_AQUA + ". They have" + ChatColor.GOLD
                            + " 5 seconds" + ChatColor.DARK_AQUA + " to comply.");

                    Bukkit.broadcast(ChatColor.DARK_RED + "[ContrabandAsker]: " + ChatColor.GOLD + player.getDisplayName()
                            + ChatColor.DARK_AQUA + " has asked " + ChatColor.GOLD + target.getDisplayName() + ChatColor.DARK_AQUA
                            + " to hand over their " + ChatColor.GOLD + contraband + ChatColor.DARK_AQUA + ".", "ContrabandAsker.broadcast");
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.f, 1.7f);
                target.sendTitle("§l§4ContrabandAsker", ChatColor.GOLD + player.getDisplayName() + ChatColor.DARK_AQUA
                        + displayedMessage, 10, 100, 10);

                target.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 10.f, 1.7f);
            }

            if (!target.isOnline()) {
                player.sendMessage(ChatColor.DARK_RED + "[ContrabandAsker]: " + ChatColor.GOLD + target.getName() + ChatColor.DARK_AQUA
                        + " has logged off during the countdown. You may " + ChatColor.RED + "jail them " + ChatColor.DARK_AQUA
                        + "when they return.");
                return;
            }

            cooldownSet.add(target.getUniqueId());
            new cooldownRunnable(JavaPlugin.getProvidingPlugin(ContrabandAsker.class), target).runTaskLater(JavaPlugin.getProvidingPlugin(ContrabandAsker.class), 200);
            new CountDown(target, player).startCountDown();
        }
    }
}