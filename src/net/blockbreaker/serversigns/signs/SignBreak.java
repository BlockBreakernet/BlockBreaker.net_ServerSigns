package net.blockbreaker.serversigns.signs;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lukas on 03.04.2015.
 */
public class SignBreak implements Listener {

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if(e.getBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getBlock().getState();

            if(s.getLine(0).equalsIgnoreCase("§a[Join]")) {
                List<String> signs = ServerSigns.sign.getStringList("Signs");

                String world = e.getBlock().getWorld().getName();
                double x = e.getBlock().getX();
                double y = e.getBlock().getY();
                double z = e.getBlock().getZ();

                if(signs.contains(s.getLine(1) + "," + world + "," + x + "," + y + "," + z)) {
                    signs.remove(s.getLine(1) + "," + world + "," + x + "," + y + "," + z);
                    ServerSigns.sign.set("Signs", signs);

                    try {
                        ServerSigns.sign.save(ServerSigns.signfile);
                        e.getPlayer().sendMessage(ServerSigns.prefix + "§6Du hast das §3Server Sign §6erfolgreich zerstört.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
