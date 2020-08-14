package me.theminddroid.contrabandasker;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ContrabandAsker extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Contraband Asker is starting up...");

        int pluginID = 8533;
        Metrics metrics = new Metrics(this,pluginID);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Objects.requireNonNull(getCommand("sword")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("bow")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("pot")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("drug")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("arrow")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("out")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        System.out.println("Contraband Asker is terminating...");
    }
}
