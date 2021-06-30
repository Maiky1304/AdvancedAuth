package nl.maikyperlee.advancedauth.modules.google.events;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.Getter;
import nl.maikyperlee.advancedauth.AdvancedAuth;
import nl.maikyperlee.advancedauth.modules.google.qr.QRMap;
import nl.maikyperlee.advancedauth.objects.AuthPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ConnectToServerListener implements Listener {

    @Getter
    private static HashMap<UUID, AuthPlayer> lockedUsers = new HashMap<>();

    private static List<UUID> incomingUsers = new ArrayList<>();

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event){
        incomingUsers.add(event.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();

        await(() -> {
            incomingUsers.remove(p.getUniqueId());

            if (!AdvancedAuth.getAuthData().contains("users." + p.getUniqueId().toString())){
                AuthPlayer authPlayer = new AuthPlayer(p);

                GoogleAuthenticatorKey key = authPlayer.generateCode();
                authPlayer.sendQRMap();

                lockedUsers.put(p.getUniqueId(), authPlayer);
                AdvancedAuth.getAuthData().set("users." + p.getUniqueId().toString() + ".key", key.getKey());
                AdvancedAuth.getAuthLoader().save();
            }
        }, 2);
    }

    void await(Runnable runnable, int delay){
        Bukkit.getScheduler().runTaskLater(AdvancedAuth.getPlugin(), runnable, 20*delay);
    }

}
