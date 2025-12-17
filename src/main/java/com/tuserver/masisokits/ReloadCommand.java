package com.tuserver.masisokits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final MasisoKits plugin;

    public ReloadCommand(MasisoKits plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage("§aLa configuración de MasisoKits fue recargada correctamente.");
        return true;
    }
}
