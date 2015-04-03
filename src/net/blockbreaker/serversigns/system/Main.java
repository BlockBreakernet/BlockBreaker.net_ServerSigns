package net.blockbreaker.serversigns.system;

import net.blockbreaker.serversigns.signs.ServerSigns;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lukas on 03.04.2015.
 */
public class Main extends JavaPlugin{


    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        new ServerSigns();
    }

    @Override
    public void onDisable() {

    }

    public static Main getPlugin() {
        return instance;
    }
}
