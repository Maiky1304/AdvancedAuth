package nl.maikyperlee.advancedauth.util.configuration;

import nl.maikyperlee.advancedauth.AdvancedAuth;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration {

    private YamlConfiguration configuration;

    private String fileName;
    private boolean overrideOption = false;

    private AdvancedAuth advancedAuth;

    public Configuration(AdvancedAuth advancedAuth){
        this.advancedAuth = advancedAuth;
    }

    public Configuration setFileName(String fileName){
        this.fileName = (!fileName.endsWith(".yml") ? fileName + ".yml" : fileName);
        return this;
    }

    public Configuration setOverrideOption(boolean overrideOption){
        this.overrideOption = overrideOption;
        return this;
    }

    public void loadConfiguration(){
        File f = new File(this.advancedAuth.getDataFolder(), this.fileName);

        // Check if directory exists (if not create)
        if (!this.advancedAuth.getDataFolder().exists()){
            this.advancedAuth.getDataFolder().mkdir();
        }

        if (!f.exists()){
            this.advancedAuth.saveResource(this.fileName, this.overrideOption);
        }

        this.configuration = YamlConfiguration.loadConfiguration(f);
    }

    public void save(){
        File f = new File(this.advancedAuth.getDataFolder(), this.fileName);

        try {
            this.configuration.save(f);
        } catch(IOException e){
            this.advancedAuth.getLogger().warning("Error when tried to save " + this.fileName);
        }
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public AdvancedAuth getAdvancedAuth() {
        return advancedAuth;
    }


}
