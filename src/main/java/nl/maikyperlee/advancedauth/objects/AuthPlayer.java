package nl.maikyperlee.advancedauth.objects;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import nl.maikyperlee.advancedauth.AdvancedAuth;
import nl.maikyperlee.advancedauth.modules.google.qr.QRMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class AuthPlayer extends AuthData {

    public String authKey = "";
    public GoogleAuthenticatorKey googleAuthenticatorKey = null;
    public ItemStack previousItemInSlot = null;

    private Player player;

    public AuthPlayer(Player player){
        super(0, false);
        this.player = player;
    }

    private void verify(){
        boolean match = false;
        for (Player p1 : Bukkit.getOnlinePlayers()){
            if (p1.equals(this.player)){
                match = true;
                break;
            }
        }

        if (!match){
            Player newPlayer = null;
            for (Player p1 : Bukkit.getOnlinePlayers()){
                if (p1.getUniqueId().toString().equals(this.player.getUniqueId().toString())){
                    newPlayer = p1.getPlayer();
                    break;
                }
            }

            if (newPlayer == null)return;
            this.player = newPlayer;
        }
    }

    public Player getBukkitPlayer() {
        return player;
    }

    public boolean isNew(){
        return AdvancedAuth.getAuthData().contains("users." + getBukkitPlayer().getUniqueId().toString());
    }

    public GoogleAuthenticatorKey generateCode(){
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        this.authKey = key.getKey();
        this.googleAuthenticatorKey = key;

        return key;
    }

    public void sendQRMap(){
        verify();

        AdvancedAuth.handleMessage("enter-code-in-gauth", getBukkitPlayer(), this.googleAuthenticatorKey.getKey());

        try {
            URL url = new URL(qrImageUrl(this.authKey));
            BufferedImage image = ImageIO.read(url);

            ItemStack map = new ItemStack(Material.MAP, 1);

            ItemMeta mapMeta = map.getItemMeta();
            mapMeta.setDisplayName("QR Code");
            mapMeta.setLore(Arrays.asList("Use this secure key if the QR doesn't work:", this.authKey));
            map.setItemMeta(mapMeta);

            MapView view = Bukkit.createMap(getBukkitPlayer().getWorld());
            view.getRenderers().clear();
            view.addRenderer(new QRMap(image));

            map.setDurability(view.getId());

            int slot = -1;
            for (int i = 0; i < 9; i++){
                if (getBukkitPlayer().getInventory().getItem(i) == null){
                    slot = i;
                    break;
                }
            }

            if (slot == -1){
                this.previousItemInSlot = getBukkitPlayer().getItemInHand();
                getBukkitPlayer().setItemInHand(map);
            }else{
                getBukkitPlayer().getInventory().setItem(slot, map);
                getBukkitPlayer().getInventory().setHeldItemSlot(slot);
            }

            Location l = getBukkitPlayer().getLocation();
            l.setPitch(40F);

            getBukkitPlayer().teleport(l);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String qrImageUrl(String secret) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("https://chart.googleapis.com/chart?chs=128x128&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=");
        addOtpAuthPart(getBukkitPlayer().getName(), secret, sb);
        return sb.toString();
    }

    private void addOtpAuthPart(String keyId, String secret, StringBuilder sb) {
        sb.append("otpauth://totp/").append(keyId).append("?secret=").append(secret);
    }

}
