package net.blockbreaker.serversigns;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lukas on 02.04.2015.
 */
public class Game extends JavaPlugin implements Listener {

    public static Game instance;

    public Game() {
        instance = this;
    }

    public static Game getInstance() {
        return instance;
    }

    public static File locFile = new File("plugins/Test", "Locations.yml");
    public static FileConfiguration locCfg = YamlConfiguration.loadConfiguration(locFile);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        updateSign();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();

        if(e.getLine(0).toLowerCase().contains("[join]")) {
            locCfg.set("loc.sign.X", loc.getX());
            locCfg.set("loc.sign.Y", loc.getY());
            locCfg.set("loc.sign.Z", loc.getZ());
            locCfg.set("loc.sign.World", loc.getWorld().getName());

            e.setLine(0, "§4§l?????????");
            e.setLine(1, "§4§lPinging");
            e.setLine(2, "§4§l...");
            e.setLine(3, "§4§l?????????");
            try {
                locCfg.save(locFile);
                p.sendMessage("§aSchild erstellt");
            } catch (IOException e1) {
                p.sendMessage("§cSchild NICHT erstellt");
                e1.printStackTrace();
            }
        }
    }

    public void updateSign() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                double x = locCfg.getDouble("loc.sign.X");
                double y = locCfg.getDouble("loc.sign.Y");
                double z = locCfg.getDouble("loc.sign.Z");
                World w = Bukkit.getWorld(locCfg.getString("loc.sign.World"));

                Location loc = new Location(w, x, y, z);

                Sign s = (Sign) loc.getBlock().getState();

                Server server = new Server(Game.SrvCfg.getString("Server." + s.getLine(1) + ".IP"));

                s.setLine(3, server.parseData(Server.Connection.ONLINE_PLAYERS) + " / " + server.parseData(Server.Connection.MAX_PLAYERS));
                s.setLine(2, server.parseData(Server.Connection.MOTD));

                if(s.getLine(2).equalsIgnoreCase("null")) {
                    s.setLine(0, "§4§l?????????");
                    s.setLine(1, "§4§lOFFLINE");
                    s.setLine(2, "§4§l- / -");
                    s.setLine(3, "§4§l?????????");
                } else if (!s.getLine(2).equalsIgnoreCase("null")){
                    s.setLine(0, "§7[Join]");
                    s.setLine(1, null);
                    s.setLine(2, "§2" + server.parseData(Server.Connection.MOTD));
                    s.setLine(3, "§f" + server.parseData(Server.Connection.ONLINE_PLAYERS) + "/" + server.parseData(Server.Connection.MAX_PLAYERS));
                }
                s.update();
            }

        }, 100L, 10L);
    }
}
