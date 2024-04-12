package me.davipccunha.naturalspawners;

import lombok.Getter;
import me.davipccunha.naturalspawners.command.SpawnerCommand;
import me.davipccunha.naturalspawners.listener.BlockBreakListener;
import me.davipccunha.naturalspawners.listener.BlockPlaceListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class NaturalSpawnersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.init();
        getLogger().info("Spawners plugin enabled!");
    }

    public void onDisable() {
        getLogger().info("Spawners plugin disabled!");
    }

    private void init() {
        registerListeners();
        registerCommands();

        saveDefaultConfig();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
    }

    private void registerCommands() {
        this.getCommand("spawner").setExecutor(new SpawnerCommand());
    }
}
