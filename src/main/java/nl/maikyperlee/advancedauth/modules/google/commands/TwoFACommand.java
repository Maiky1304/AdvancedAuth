package nl.maikyperlee.advancedauth.modules.google.commands;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import nl.maikyperlee.advancedauth.AdvancedAuth;
import nl.maikyperlee.advancedauth.modules.google.events.ConnectToServerListener;
import nl.maikyperlee.advancedauth.objects.AuthPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TwoFACommand {

    public static void execute(Player p, int inputCode) {
        AuthPlayer authPlayer = ConnectToServerListener.getLockedUsers().get(p.getUniqueId());

        String secretkey = AdvancedAuth.getAuthData().getString("users." + p.getUniqueId().toString() + ".key");

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean codeisvalid = gAuth.authorize(secretkey, inputCode);


        if (codeisvalid) {
            authPlayer.verificated = true;
            authPlayer.lastVerificationDate = System.currentTimeMillis();

            ConnectToServerListener.getLockedUsers().remove(p.getUniqueId());
            p.sendMessage("§aThank you, you can now resume your time on the server.");

            if (ConnectToServerListener.getLockedUsers().get(p.getUniqueId())
                    .previousItemInSlot != null) {
                ItemStack stack = ConnectToServerListener.getLockedUsers().get(p.getUniqueId())
                        .previousItemInSlot;

                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), stack);
            }else{
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(),
                        null);
            }

        }else{
            p.sendMessage("§cThe code you've entered '" + inputCode + "' is incorrect.");
        }
    }
}
