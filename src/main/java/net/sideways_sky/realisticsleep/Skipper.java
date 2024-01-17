package net.sideways_sky.realisticsleep;

import net.minecraft.server.ServerTickRateManager;
import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class Skipper {
    private final SkipMode skipMode;
    private final int skipSpeed;
    private float preSkipSpeed = 20;
    public boolean isSkipping = false;
    public void start(int ticks){
        if(skipMode == SkipMode.SPRINT) {
            bukkitManager.requestGameToSprint(ticks);
        } else {
            preSkipSpeed = bukkitManager.getTickRate();
            bukkitManager.setTickRate(skipSpeed);
            Bukkit.getScheduler().runTaskLater(RealisticSleep.instance, this::stop, ticks);
        }
        isSkipping = true;
    }
    public void stop(){
        if(skipMode == SkipMode.SPRINT) {
            if(nmsManager == null){
                Bukkit.getServerTickManager().stopSprinting();
            } else {
                nmsManager.stopSprinting(false);
            }
        } else {
            bukkitManager.setTickRate(preSkipSpeed);
        }
        isSkipping = false;
    }
    private final ServerTickManager bukkitManager;
    @Nullable
    private ServerTickRateManager nmsManager = null;
    public Skipper(SkipMode skipMode, int skipSpeed)  {
        this.skipMode = skipMode;
        this.skipSpeed = skipSpeed;
        bukkitManager = Bukkit.getServerTickManager();

        try {
            Class<?> clazz = Class.forName("org.bukkit.craftbukkit.v1_20_R3.CraftServerTickManager");
            Field manager = clazz.getDeclaredField("manager");
            manager.setAccessible(true);
            nmsManager = (ServerTickRateManager) manager.get(bukkitManager);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            Bukkit.getLogger().warning("Failed to fetch ServerTickRateManager - defaulting to bukkitManager");
        }

    }
}
