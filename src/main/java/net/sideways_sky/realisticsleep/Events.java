package net.sideways_sky.realisticsleep;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.TimeSkipEvent;

import java.util.HashMap;
public class Events implements Listener {
    public static HashMap<World, SleepingWorld> worlds = new HashMap<>();
    @EventHandler
    public static void onServerLoad(ServerLoadEvent e){
         worlds.clear();
         Bukkit.getWorlds().forEach(world -> {
             if(!world.isBedWorks()){return;}
             worlds.put(world, new SleepingWorld(world));
         });
    }
    private static void update(World w){
        Bukkit.getScheduler().runTaskLater(RealisticSleep.instance, () -> {
            SleepingWorld world = worlds.get(w);
            world.update();
            world.sendSleepingUpdate();
        }, 1);
    }
    @EventHandler
    public static void onPlayerBedEnter(PlayerBedEnterEvent e){
        if(e.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK){return;}
        update(e.getPlayer().getWorld());
    }
    @EventHandler
    public static void onPlayerSleep(PlayerDeepSleepEvent e){
        update(e.getPlayer().getWorld());
    }
    @EventHandler
    public static void onPlayerBedLeave(PlayerBedLeaveEvent e){
        update(e.getPlayer().getWorld());
    }
    @EventHandler
    public static void onTimeSkip(TimeSkipEvent e){
        if(e.getSkipReason() != TimeSkipEvent.SkipReason.NIGHT_SKIP){return;}
        worlds.get(e.getWorld()).sendSleepingUpdate();
        e.setCancelled(true);
    }
}
