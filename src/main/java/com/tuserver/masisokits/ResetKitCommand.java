package com.tuserver.masisokits;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ResetKitCommand implements CommandExecutor {

    private final KitCommand kitCommand;

    public ResetKitCommand(KitCommand kitCommand) {
        this.kitCommand = kitCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Uso: /resetkit <jugador> <nombrekit>");
            return true;
        }

        String playerName = args[0];
        String kitName = args[1];

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage("El jugador " + playerName + " no está conectado.");
            return true;
        }

        Map<String, Long> playerMap = kitCommand.getCooldowns().get(target.getUniqueId());

        if (playerMap != null && playerMap.containsKey(kitName)) {
            playerMap.remove(kitName);
            sender.sendMessage("Cooldown del kit " + kitName + " reseteado para " + playerName + "!");
            target.sendMessage("Tu cooldown del kit " + kitName + " fue reseteado por un admin.");
        } else {
            sender.sendMessage("Ese jugador no tenía cooldown activo para el kit " + kitName + ".");
        }

        return true;
    }
}
