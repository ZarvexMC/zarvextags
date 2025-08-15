package com.zarvex.zarvextags;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.zarvex.zarvextags.TagModCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ZarvexTags extends JavaPlugin {

    private final Map<String, String> tags = new HashMap<>();
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        getLogger().info("Iniciando ZarvexTags...");
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI não encontrado! O plugin será desabilitado.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Configuração do config.yml
        saveDefaultConfig();
        loadTags();

        // Configuração do banco de dados
        databaseManager = new DatabaseManager(this);
        if (!databaseManager.connect()) {
            getLogger().severe("Falha ao conectar no banco de dados. O plugin será desabilitado.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("tag").setExecutor(new TagCommand(this));
        getCommand("tagsreload").setExecutor(new ReloadCommand(this));
        getCommand("tagmod").setExecutor(new TagModCommand(this, databaseManager));
        new TagsExpansion(this).register();

        getLogger().info("ZarvexTags habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        databaseManager.disconnect();
        getLogger().info("ZarvexTags desabilitado.");
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void loadTags() {
        tags.clear();
        FileConfiguration config = getConfig();
        if (config.getConfigurationSection("tags") != null) {
            for (String key : config.getConfigurationSection("tags").getKeys(false)) {
                String displayName = config.getString("tags." + key + ".displayname");
                if (displayName != null) {
                    tags.put(key, displayName);
                }
            }
        }
        getLogger().info(tags.size() + " tags predefinidas carregadas.");
    }
}

