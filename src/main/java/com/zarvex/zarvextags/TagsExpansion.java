package com.zarvex.zarvextags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TagsExpansion extends PlaceholderExpansion {

    private final ZarvexTags plugin;

    public TagsExpansion(ZarvexTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zarvextags-tag";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Felipe";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // Indica que a expansão deve ser salva no disco
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // Como o identificador já é a tag completa, não precisamos verificar os parâmetros.
        return plugin.getPlayerTags().getOrDefault(player.getUniqueId(), "");
    }
}
