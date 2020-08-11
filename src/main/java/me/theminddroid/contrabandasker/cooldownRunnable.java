package me.theminddroid.contrabandasker;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class cooldownRunnable extends BukkitRunnable {

    Plugin plugin;
    Player target;

    public cooldownRunnable(Plugin plugin, Player target) {
        this.plugin = plugin;
        this.target = target;
    }

    @Override
    public void run() {
        Commands.getCooldownSet().remove(target.getUniqueId());
    }
}
