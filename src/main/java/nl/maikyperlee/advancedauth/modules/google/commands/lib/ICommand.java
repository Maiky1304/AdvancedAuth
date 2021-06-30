package nl.maikyperlee.advancedauth.modules.google.commands.lib;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class ICommand {

    @Getter
    private String command,permission;

    public ICommand(String command){
        this.command = command;
    }

    public ICommand(String command, String permission){
        this(command);
        this.permission = permission;
    }

    public abstract TabCompleter getTabCompleter();
    public abstract void execute(CommandSender sender, String[] args);
}
