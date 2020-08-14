package me.theminddroid.contrabandasker;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CountDown {
    int time = 8;
    Player target;
    Player player;
    String title;
    String prefix;

    CountDown (Player target, Player player, String title, String prefix) {
        this.target = target;
        this.player = player;
        this.title = title;
        this.prefix = prefix;
    }

    public void startCountDown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!target.isOnline()) {
                    cancel();
                    player.sendMessage(ChatColor.DARK_RED + "[" + prefix + "]: " + ChatColor.GOLD + target.getName() + ChatColor.DARK_AQUA
                            + " has logged off during the countdown. You may " + ChatColor.RED + "jail them " + ChatColor.DARK_AQUA
                            + "when they return.");
                    return;
                }

                if (time > 5) {
                    time--;
                    return;
                }

                if (time != 0) {
                    target.sendTitle("",ChatColor.DARK_AQUA + "You have " + ChatColor.RED + ChatColor.BOLD
                            + time + ChatColor.DARK_AQUA + " seconds to comply...",0,20,0);

                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.f, 1.7f);
                    time--;
                } else {
                    cancel();
                    target.sendTitle("",ChatColor.DARK_AQUA + "Failure to comply will result in "
                            + ChatColor.GOLD + "jail time " + ChatColor.DARK_AQUA + "or " + ChatColor.RED + "death"
                            + ChatColor.DARK_AQUA + ".",0,80,5);
                    player.sendMessage(ChatColor.DARK_RED + "[" + prefix + "]: " + ChatColor.DARK_AQUA + "Time is up for "
                            + ChatColor.GOLD + target.getDisplayName() + ChatColor.DARK_AQUA
                            + ". If they have failed to comply you may " + ChatColor.GOLD + "jail" + ChatColor.DARK_AQUA + " or " + ChatColor.RED
                            + "kill" + ChatColor.DARK_AQUA + " them.");

                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10.f, 1.7f);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10.f, 1.7f);
                }
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(ContrabandAsker.class), 20, 20);
    }
}
