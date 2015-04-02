package net.blockbreaker.serversigns;

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
 * Created by Lukas on 02.04.2015.
 */
public class ClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock().getState() instanceof Sign) {
                Player p = e.getPlayer();
                Sign s = (Sign) e.getClickedBlock().getState();
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                if(s.getLine(0).equalsIgnoreCase("§7[Join]")) {
                    e.getPlayer().sendMessage("Verbinde zu Server...");
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("127.0.0.1:25565");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    p.sendPluginMessage(Game.getInstance(), "BungeeCord", b.toByteArray());
                }
            }
        }
    }
}
