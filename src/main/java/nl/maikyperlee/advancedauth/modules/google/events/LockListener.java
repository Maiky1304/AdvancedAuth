package nl.maikyperlee.advancedauth.modules.google.events;

import nl.maikyperlee.advancedauth.AdvancedAuth;
import nl.maikyperlee.advancedauth.modules.google.commands.TwoFACommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class LockListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (!ConnectToServerListener.getLockedUsers().containsKey(event.getPlayer().getUniqueId()))return;

        if (AdvancedAuth.getSettingsConfig().getBoolean("settings.disable-interact")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvP(PlayerInteractAtEntityEvent event){
        if (!ConnectToServerListener.getLockedUsers().containsKey(event.getPlayer().getUniqueId()))return;

        if (AdvancedAuth.getSettingsConfig().getBoolean("settings.disable-interact")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if (!ConnectToServerListener.getLockedUsers().containsKey(event.getPlayer().getUniqueId()))return;

        if (AdvancedAuth.getSettingsConfig().getBoolean("settings.disable-interact")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (!ConnectToServerListener.getLockedUsers().containsKey(event.getWhoClicked().getUniqueId()))return;

        if (AdvancedAuth.getSettingsConfig().getBoolean("settings.disable-interact")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwitchHeld(PlayerItemHeldEvent event){
        if (!ConnectToServerListener.getLockedUsers().containsKey(event.getPlayer().getUniqueId()))return;

        if (AdvancedAuth.getSettingsConfig().getBoolean("settings.disable-interact")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();

        if (!ConnectToServerListener.getLockedUsers().containsKey(p.getUniqueId()))return;
        if (!message.startsWith("/2fa")){
            event.setCancelled(true);
            p.sendMessage("§cPlease first authenticate your account, before using the chat for messaging/commands.");
            return;
        }

        String[] args = message.split(" ");
        if (args.length == 1){
            event.setCancelled(true);
            p.sendMessage("§cPlease enter a code using /2fa <code>");
            return;
        }

        if (args.length == 2){
            event.setCancelled(true);
            String code = args[1];

            int codeInt;
            try {
                codeInt = Integer.parseInt(code);
            } catch (NumberFormatException exception){
                p.sendMessage("§cThe code you provided contains alphabetical characters.");
                return;
            }

            TwoFACommand.execute(p, codeInt);
            return;
        }

        if (args.length > 2){
            event.setCancelled(true);
            p.sendMessage("§cPlease enter a code using /2fa <code>");
            return;
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        Player p = event.getPlayer();

        if (ConnectToServerListener.getLockedUsers().containsKey(p.getUniqueId())){
            if (ConnectToServerListener.getLockedUsers().get(p.getUniqueId())
                    .previousItemInSlot != null) {
                ItemStack stack = ConnectToServerListener.getLockedUsers().get(p.getUniqueId())
                        .previousItemInSlot;

                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), stack);
            }else{
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(),
                        null);
            }

            ConnectToServerListener.getLockedUsers().remove(p.getUniqueId());

            AdvancedAuth.getAuthData().set("users." + p.getUniqueId().toString(), null);
            AdvancedAuth.getAuthLoader().save();
        }
    }

}
