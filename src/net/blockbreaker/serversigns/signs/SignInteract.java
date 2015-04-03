package net.blockbreaker.serversigns.signs;

import net.blockbreaker.serversigns.system.Main;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Lukas on 03.04.2015.
 */
public class SignInteract implements Listener {

    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock().getState() instanceof Sign) {
                Sign s = (Sign) e.getClickedBlock().getState();

                if(s.getLine(0).equalsIgnoreCase("§a[Join]")) {
                    if(s.getLine(2).equalsIgnoreCase("§c§lOffline")) {
                        p.sendMessage("§6Der §3Server §6ist nicht Online!");
                    } else {
                        Connect(s.getLine(1), p);
                    }
                }
            }
        }
    }

    public void Connect(String server, Player p) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.sendPluginMessage(Main.getPlugin(), "BungeeCord", b.toByteArray());
    }
}
