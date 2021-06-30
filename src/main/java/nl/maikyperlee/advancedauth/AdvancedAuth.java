package nl.maikyperlee.advancedauth;

import lombok.AccessLevel;
import lombok.Getter;
import nl.maikyperlee.advancedauth.objects.enums.AuthenticationType;
import nl.maikyperlee.advancedauth.util.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AdvancedAuth extends JavaPlugin {

    @Getter
    private static long pluginStartTime;

    @Getter
    private static AuthenticationHandler authenticationHandler;

    @Getter
    private static Configuration authLoader;

    @Getter
    private static YamlConfiguration settingsConfig,authData,messages;

    @Getter
    private static AdvancedAuth plugin;

    @Override
    public void onEnable() {
        // Initialize Plugin
        plugin = this;

        // Plugin Start Time
        pluginStartTime = System.currentTimeMillis();

        // Configuration File
        Configuration configuration = new Configuration(plugin)
        .setFileName("settings.yml").setOverrideOption(true);
        configuration.loadConfiguration();
        settingsConfig = configuration.getConfiguration();

        authLoader = new Configuration(plugin)
                .setFileName("authData.yml").setOverrideOption(true);
        authLoader.loadConfiguration();
        authData = authLoader.getConfiguration();

        Configuration configuration3 = new Configuration(plugin)
                .setFileName("messages.yml").setOverrideOption(true);
        configuration3.loadConfiguration();
        messages = configuration3.getConfiguration();


        // Authentication Handler
        try {
            authenticationHandler = new AuthenticationHandler(AuthenticationType.valueOf(
                    settingsConfig.getString("settings.authentication-type").toUpperCase()
            ));
        } catch(IllegalArgumentException exception){
            getLogger().severe("[Config] Invalid Authentication Type provided in settings.yml change this and then restart the server.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
    }

    public static void handleMessage(String path, Player p, Object... objects){
        if (messages.contains(path)){
            if (messages.get(path) instanceof List<?>){
                for (String string : messages.getStringList(path)){
                    String toBeSend = string;
                    retrieve(p, toBeSend, objects);
                }
            }else{
                String toBeSend = messages.getString(path);
                retrieve(p, toBeSend, objects);
            }
        }
    }

    private static void retrieve(Player p, String toBeSend, Object[] objects) {
        for (int i = 0; i < objects.length; i++){
            String val = "<" + i + ">";
            System.out.println(val);
            if (toBeSend.contains(val)){
                toBeSend = toBeSend.replaceAll(val, String.valueOf(objects[i]));
            }
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', toBeSend));
    }
}
