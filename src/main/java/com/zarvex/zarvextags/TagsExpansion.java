package com.zarvex.zarvextags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TagsExpansion extends PlaceholderExpansion {

    private final ZarvexTags plugin;

    public TagsExpansion(ZarvexTags plugin) {
        this.plugin = plugin;
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

        // %zarvextags_tag%
        if (params.equalsIgnoreCase("tag")) {
            String tagId = plugin.getDatabaseManager().getSelectedTag(player.getUniqueId());

            if (tagId == null) {
                return ""; // Sem tag
            }

            String displayName = plugin.getTags().get(tagId);

            if (displayName == null) {
                return ""; // Tag sem displayname
            }

            return ChatColor.translateAlternateColorCodes('&', displayName);
        }

        return null;
    }
}
