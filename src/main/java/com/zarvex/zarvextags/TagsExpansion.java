package com.zarvex.zarvextags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class TagsExpansion extends PlaceholderExpansion {

    private final ZarvexTags plugin;
    private final Logger logger;

    public TagsExpansion(ZarvexTags plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zarvextags";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        logger.info("Placeholder requisitado: %zarvextags_" + params + "% para o jogador " + player.getName());

        // %zarvextags_tag%
        if (params.equalsIgnoreCase("tag")) {
            String tagId = plugin.getPlayerTags().get(player.getUniqueId());
            logger.info("Tag ID do jogador: " + tagId);

            if (tagId == null) {
                return ""; // Sem tag
            }

            String displayName = plugin.getTags().get(tagId);
            logger.info("Display name da tag: " + displayName);

            if (displayName == null) {
                return ""; // Tag sem displayname
            }

            return ChatColor.translateAlternateColorCodes('&', displayName);
        }

        return null;
    }
}
