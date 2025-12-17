package com.tuserver.masisokits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

import java.util.*;

public class KitCommand implements CommandExecutor {

    private final MasisoKits plugin;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public KitCommand(MasisoKits plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando es solo para jugadores.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length < 1) {
            p.sendMessage("Uso: /masisokit <nombre>");
            return true;
        }

        String kitName = args[0];
        ConfigurationSection kit = plugin.getConfig().getConfigurationSection("kits." + kitName);
        if (kit == null) {
            p.sendMessage("Ese kit no existe.");
            return true;
        }

        // Cooldown
        int delay = kit.getInt("delay", 0);
        long now = System.currentTimeMillis();
        cooldowns.putIfAbsent(p.getUniqueId(), new HashMap<>());
        Map<String, Long> playerMap = cooldowns.get(p.getUniqueId());

        if (playerMap.containsKey(kitName)) {
            long last = playerMap.get(kitName);
            long msDelay = delay * 1000L;
            long remaining = (last + msDelay) - now;
            if (remaining > 0) {
                long secs = remaining / 1000;
                p.sendMessage("Todavía faltan " + secs + "s para reclamar este kit.");
                return true;
            }
        }

        // Cobro de dinero (cost)
        double cost = kit.getDouble("cost", 0.0);
        if (cost > 0.0) {
            if (plugin.getEconomy().getBalance(p) < cost) {
                p.sendMessage("§cNo tienes suficiente dinero. Necesitas §e" + cost);
                return true;
            }
            plugin.getEconomy().withdrawPlayer(p, cost);
            p.sendMessage("§aHas pagado §e" + cost + " §apor el kit " + kitName + ".");
        }

        // Recompensa de dinero (money)
        double reward = kit.getDouble("money", 0.0);
        if (reward > 0.0) {
            plugin.getEconomy().depositPlayer(p, reward);
            p.sendMessage("§aHas recibido §e" + reward + " §apor reclamar el kit " + kitName + ".");
        }

        // Ejecutar comandos como consola o jugador según prefijo
        List<String> commands = kit.getStringList("left_click_commands");
        for (String raw : commands) {
            String parsed = raw.replace("%player_name%", p.getName())
                            .replace("%player_uuid%", p.getUniqueId().toString());

            if (parsed.startsWith("[console]")) {
                String cmd = parsed.replace("[console]", "").trim();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } else if (parsed.startsWith("[player]")) {
                String cmd = parsed.replace("[player]", "").trim();
                p.performCommand(cmd);
            } else {
                // Por defecto, consola
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
            }
        }


        // Dar ítems
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) (List<?>) kit.getMapList("items");
        for (Map<String, Object> it : items) {
            String matName = (String) it.getOrDefault("material", "STONE");
            Material mat = Material.matchMaterial(matName);
            if (mat == null) mat = Material.STONE;

            int amount = ((Number) it.getOrDefault("amount", 1)).intValue();
            ItemStack stack = new ItemStack(mat, amount);
            ItemMeta meta = stack.getItemMeta();
            if (meta == null) continue;

            // Nombre con colores
            if (it.containsKey("name")) {
                String rawName = (String) it.get("name");
                String coloredName = ChatColor.translateAlternateColorCodes('&', rawName);
                meta.setDisplayName(coloredName);
            }

            // Lore con colores
            if (it.containsKey("lore")) {
                @SuppressWarnings("unchecked")
                List<String> lore = (List<String>) it.get("lore");
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    String coloredLine = ChatColor.translateAlternateColorCodes('&', line);
                    coloredLore.add(coloredLine);
                }
                meta.setLore(coloredLore);
            }

            // Encantamientos
            if (it.containsKey("enchantments")) {
                @SuppressWarnings("unchecked")
                List<String> ench = (List<String>) it.get("enchantments");
                for (String line : ench) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        try {
                            org.bukkit.enchantments.Enchantment e =
                                    org.bukkit.enchantments.Enchantment.getByName(parts[0].toUpperCase());
                            int lvl = Integer.parseInt(parts[1]);
                            if (e != null) meta.addEnchant(e, lvl, true);
                        } catch (Exception ex) {
                            Bukkit.getLogger().warning("[MasisoKits] Error en encantamiento: " + line + " -> " + ex.getMessage());
                        }
                    }
                }
            }

            // Atributos (AttributeModifiers)
            if (it.containsKey("attributes")) {
                @SuppressWarnings("unchecked")
                List<String> attrs = (List<String>) it.get("attributes");
                for (String line : attrs) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        try {
                            String attrKey = parts[0].trim().toUpperCase().replace('.', '_').replace('-', '_');
                            Attribute attr = Attribute.valueOf(attrKey);

                            double value = Double.parseDouble(parts[1]);

                            org.bukkit.inventory.EquipmentSlot slot =
                                    (parts.length >= 3)
                                            ? org.bukkit.inventory.EquipmentSlot.valueOf(parts[2].trim().toUpperCase())
                                            : org.bukkit.inventory.EquipmentSlot.HAND;

                            AttributeModifier.Operation operation =
                                    (parts.length >= 4)
                                            ? AttributeModifier.Operation.valueOf(parts[3].trim().toUpperCase())
                                            : AttributeModifier.Operation.ADD_NUMBER;

                            AttributeModifier modifier = new AttributeModifier(
                                    UUID.randomUUID(),
                                    attr.name().toLowerCase(),
                                    value,
                                    operation,
                                    slot
                            );
                            meta.addAttributeModifier(attr, modifier);

                        } catch (Exception ex) {
                            Bukkit.getLogger().warning("[MasisoKits] Error en atributo: " + line + " -> " + ex.getMessage());
                        }
                    }
                }
            }

            stack.setItemMeta(meta);
            p.getInventory().addItem(stack);
        }

        playerMap.put(kitName, now);
        p.sendMessage("Kit " + kitName + " entregado!");
        return true;
    }

    public Map<UUID, Map<String, Long>> getCooldowns() {
        return cooldowns;
    }
}
