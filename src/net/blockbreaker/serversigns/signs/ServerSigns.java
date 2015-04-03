package net.blockbreaker.serversigns.signs;

import net.blockbreaker.serversigns.system.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lukas on 03.04.2015.
 */
public class ServerSigns {

    public static String prefix = "§3[§6ServerSigns§3] ";

    public static HashMap<String, String> servers = new HashMap<String, String>();
    public static HashMap<String, String> serverinfo = new HashMap<String, String>();

    public static File serverfile = new File("./plugins/ServerSigns/", "server.yml");
    public static FileConfiguration server = YamlConfiguration.loadConfiguration(serverfile);

    public static File signfile = new File("./plugins/ServerSigns/", "signs.yml");
    public static FileConfiguration sign = YamlConfiguration.loadConfiguration(signfile);


    public ServerSigns() {

        loadFiles();
        Bukkit.getPluginManager().registerEvents(new SignPlace(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SignBreak(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SignInteract(), Main.getPlugin());

        servers = loadServers();

        if(serverfile.exists()) {
            pingServer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    //signupdate();
                }
            }, 20L);
        }
    }

    public void loadFiles() {
        loadServerFile();
        loadSignFile();
    }

    public void loadServerFile() {
        serverfile = new File("./plugins/ServerSigns/", "server.yml");
        FileConfiguration server = YamlConfiguration.loadConfiguration(serverfile);

        server.addDefault("Server.Lobby.Address", "localhost:25565");
        server.addDefault("Server.Lobby.ServerName", "Lobby");
        server.options().copyDefaults(true);
        try {
            server.save(serverfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadSignFile() {
        File signfile = new File("./plugins/ServerSigns/", "signs.yml");
        FileConfiguration sign = YamlConfiguration.loadConfiguration(signfile);

        try {
            sign.save(signfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> loadServers() {

        HashMap<String, String> serverinfo = new HashMap<>();
        for(String s : server.getConfigurationSection("Server").getKeys(false)) {
            serverinfo.put(server.getString("Server." + s + ".ServerName"), server.getString("Server." + s + ".Adress"));
        }

        return serverinfo;
    }

    private void pingServer() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(String server : servers.keySet()) {
                    String[] ip = servers.get(server).split(":");
                    String address = ip[0];
                    int port = Integer.valueOf(ip[1]);


                    PingServer ping = new PingServer(address, port);
                    ping.fetchData();

                    int OnlinePlayers = ping.getOnlinePlayers();
                    int MaxPlayers = ping.getMaxPlayers();
                    String MOTD = ping.getMotd();
                    boolean online = ping.isOnline();

                    serverinfo.put(server, OnlinePlayers + "/" + MaxPlayers + "/" + MOTD);
                }
            }
        }, 0L, 2*20L);
    }

    public void signUpdate() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {

                List<String> signs = sign.getStringList("String");

                for (String sign : signs) {
                    String[] infos = sign.split(",");
                    String servername = infos[0];
                    String world = infos[1];
                    double x = Double.valueOf(infos[2]);
                    double y = Double.valueOf(infos[3]);
                    double z = Double.valueOf(infos[4]);

                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                    if (loc.getBlock().getState() instanceof Sign) {
                        Sign s = (Sign) loc.getBlock().getState();
                        String[] serverinfos = serverinfo.get(servername).split("/");

                        int OnlinePlayers = 0;
                        int MaxPlayers = 0;
                        String MOTD = null;
                        boolean Online = false;

                        try {

                            OnlinePlayers = Integer.valueOf(serverinfos[1]);
                            MaxPlayers = Integer.valueOf(serverinfos[2]);
                            MOTD = serverinfos[3];
                            Online = true;

                        } catch (Exception ex) {
                            Online = false;
                        } finally {
                            update(s, OnlinePlayers, MaxPlayers, MOTD, servername, Online);
                        }
                    }

                }
            }
        }, 0L, 20L);
    }

    public void update(Sign s, int onlineplayer, int maxplayer, String motd, String servername, boolean online) {

        if(online) {
            s.setLine(0, "§a[Join]");
            s.setLine(1, servername);
            s.setLine(2, motd);
            s.setLine(3, "§7" + onlineplayer + "§9/§7" + maxplayer);
        } else {
            s.setLine(0, "§a[Join]");
            s.setLine(1, servername);
            s.setLine(2, "§c§lOffline");
            s.setLine(3, "§7-- §9/§7 --");
        }

        s.update();
    }
}
