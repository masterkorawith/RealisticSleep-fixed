package net.sideways_sky.realisticsleep;

import org.bukkit.Bukkit;

public class Skipper {
    private final SkipMode skipMode;
    private final int skipSpeed;
    private float preSkipSpeed = 20;
    public boolean isSkipping(){
        if(skipMode == SkipMode.SPRINT){
            return Bukkit.getServerTickManager().isSprinting();
        } else {
            return Bukkit.getServerTickManager().getTickRate() == skipSpeed;
        }
    }
    public void start(int ticks){
        if(skipMode == SkipMode.SPRINT) {
            Bukkit.getServerTickManager().requestGameToSprint(ticks);
        } else {
            preSkipSpeed = Bukkit.getServerTickManager().getTickRate();
            Bukkit.getServerTickManager().setTickRate(skipSpeed);
            Bukkit.getScheduler().runTaskLater(RealisticSleep.instance, this::stop, ticks);
        }
    }
    public void stop(){
        if(skipMode == SkipMode.SPRINT) {
            Bukkit.getServerTickManager().stopSprinting();
        } else {
            Bukkit.getServerTickManager().setTickRate(preSkipSpeed);
        }
    }
    public Skipper(SkipMode skipMode, int skipSpeed) {
        this.skipMode = skipMode;
        this.skipSpeed = skipSpeed;
    }
}
