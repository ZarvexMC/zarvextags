package com.zarvex.zarvextags;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ZarvexTags extends JavaPlugin {

    private final Map<UUID, String> playerTags = new HashMap<>();

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI não encontrado! O plugin será desabilitado.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("tag").setExecutor(new TagCommand(this));
        new TagsExpansion(this).register();

        getLogger().info("ZarvexTags habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ZarvexTags desabilitado.");
    }

    public Map<UUID, String> getPlayerTags() {
        return playerTags;
    }
}
