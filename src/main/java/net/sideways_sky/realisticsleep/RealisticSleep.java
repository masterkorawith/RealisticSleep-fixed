package net.sideways_sky.realisticsleep;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RealisticSleep extends JavaPlugin {
    public static RealisticSleep instance;
    public Skipper skipper;
    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        skipper = new Skipper(SkipMode.valueOf(getConfig().getString("night-skip")),
                getConfig().getInt("night-skip-speed"));
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }
}
