package net.blockbreaker.serversigns.signs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lukas on 03.04.2015.
 */
public class SignPlace implements Listener {

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {
        Player p = e.getPlayer();

        if(e.getLine(0).equalsIgnoreCase("[Join]")) {
            if(ServerSigns.servers.containsKey(e.getLine(1))) {
                String ServerName = e.getLine(1);

                String world = e.getBlock().getWorld().getName();
                double x = e.getBlock().getX();
                double y = e.getBlock().getY();
                double z = e.getBlock().getZ();

                List<String> servers = ServerSigns.sign.getStringList("Signs");
                servers.add(ServerName + "," + world + "," + x + "," + y + "," + z);
                ServerSigns.sign.set("Signs", servers);

                try {
                    ServerSigns.sign.save(ServerSigns.signfile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else {
                e.getBlock().breakNaturally();
                p.sendMessage(ServerSigns.prefix + "§cDieser Server existiert nicht!");
            }
        }
    }
}
