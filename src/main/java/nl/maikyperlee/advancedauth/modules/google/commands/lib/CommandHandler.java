package nl.maikyperlee.advancedauth.modules.google.commands.lib;

import nl.maikyperlee.advancedauth.AdvancedAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    private static HashMap<String, ICommand> registry = new HashMap<>();

    public static void register(ICommand command){
        if (registry.containsKey(command.getCommand()))return;
        registry.put(command.getCommand(), command);

        AdvancedAuth.getPlugin().getCommand(command.getCommand()).setExecutor(new CommandHandler());
        AdvancedAuth.getPlugin().getCommand(command.getCommand()).setTabCompleter(command.getTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ICommand command1 = registry.get(command.getName());

        if (command1.getPermission() != null){
            if (!commandSender.hasPermission(command1.getPermission())){
                commandSender.sendMessage("§cYou don't have permissions to use this command.");
                return true;
            }
            command1.execute(commandSender, strings);
        }else{
            command1.execute(commandSender, strings);
        }
        return true;
    }
}
