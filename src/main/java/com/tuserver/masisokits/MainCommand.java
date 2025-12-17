package com.tuserver.masisokits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final MasisoKits plugin;
    private final KitCommand kitCommand;
    private final ResetKitCommand resetKitCommand;
    private final ReloadCommand reloadCommand;

    public MainCommand(MasisoKits plugin) {
        this.plugin = plugin;
        this.kitCommand = new KitCommand(plugin);
        this.resetKitCommand = new ResetKitCommand(kitCommand);
        this.reloadCommand = new ReloadCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Uso: /masisokit <kit|reset|reload>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reset":
                return resetKitCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "reload":
                return reloadCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            default:
                if (sender instanceof Player) {
                    return kitCommand.onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Solo jugadores pueden reclamar kits.");
                    return true;
                }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reset");
            completions.add("reload");

            // además, sugerir nombres de kits desde config
            completions.addAll(plugin.getConfig().getConfigurationSection("kits").getKeys(false));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            // sugerir jugadores online
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                completions.add(p.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("reset")) {
            // sugerir kits para el segundo argumento
            completions.addAll(plugin.getConfig().getConfigurationSection("kits").getKeys(false));
        }

        return completions;
    }
}
