package com.zarvex.zarvextags;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ZarvexTags extends JavaPlugin {

    private final Map<UUID, String> playerTags = new HashMap<>();
    private final Map<String, String> tags = new HashMap<>();
    private File playerDataFile;
    private FileConfiguration playerDataConfig;

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

        // Configuração do playerdata.yml
        createPlayerDataConfig();
        loadPlayerData();

        getCommand("tag").setExecutor(new TagCommand(this));
        getCommand("tagsreload").setExecutor(new ReloadCommand(this));
        new TagsExpansion(this).register();

        getLogger().info("ZarvexTags habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        savePlayerData();
        getLogger().info("ZarvexTags desabilitado.");
    }

    public Map<UUID, String> getPlayerTags() {
        return playerTags;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setPlayerTag(UUID playerUuid, String tagId) {
        if (tagId == null) {
            playerTags.remove(playerUuid);
            playerDataConfig.set(playerUuid.toString(), null);
        } else {
            playerTags.put(playerUuid, tagId);
            playerDataConfig.set(playerUuid.toString(), tagId);
        }
        savePlayerData(); // Salva imediatamente após a alteração
    }

    private void createPlayerDataConfig() {
        playerDataFile = new File(getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            playerDataFile.getParentFile().mkdirs();
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Não foi possível criar o arquivo playerdata.yml");
                e.printStackTrace();
            }
        }

        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    public void loadPlayerData() {
        playerTags.clear();
        for (String uuidString : playerDataConfig.getKeys(false)) {
            playerTags.put(UUID.fromString(uuidString), playerDataConfig.getString(uuidString));
        }
        getLogger().info(playerTags.size() + " tags de jogadores carregadas.");
    }

    public void savePlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            getLogger().severe("Não foi possível salvar o arquivo playerdata.yml");
            e.printStackTrace();
        }
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

