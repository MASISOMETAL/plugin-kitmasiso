package com.tuserver.masisokits;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MasisoKits extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        // Config por defecto
        saveDefaultConfig();

        // Setup economía
        if (!setupEconomy()) {
            getLogger().severe("Vault no encontrado o sin economía disponible. Deshabilitando MasisoKits.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Masisokits habilitado.");

        // Registrar comando principal
        getCommand("masisokit").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Masisokits deshabilitado.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }
}
