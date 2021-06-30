package nl.maikyperlee.advancedauth.modules.google;

import nl.maikyperlee.advancedauth.AdvancedAuth;
import nl.maikyperlee.advancedauth.modules.google.events.ConnectToServerListener;
import nl.maikyperlee.advancedauth.modules.google.events.LockListener;
import org.bukkit.Bukkit;

public class GoogleAuthLoader {

    public static void load(String[] args){
        /**
         * Event Handlers
         * @see org.bukkit.event.Listener
         */
        Bukkit.getPluginManager().registerEvents(new ConnectToServerListener(), AdvancedAuth.getPlugin());
        Bukkit.getPluginManager().registerEvents(new LockListener(), AdvancedAuth.getPlugin());
    }

}
