package nl.maikyperlee.advancedauth.modules.google.commands;

import nl.maikyperlee.advancedauth.modules.google.commands.lib.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class AdvancedAuthCommand extends ICommand {

    public AdvancedAuthCommand() {
        super("advancedauth", "advancedauth.admin");
    }

    @Override
    public TabCompleter getTabCompleter() {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // do things
    }
}
