package com.zarvex.zarvextags;

import static com.zarvex.zarvextags.ZarvexTags.getColoredPrefix;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TagCommand implements CommandExecutor {

    private final ZarvexTags plugin;

    public TagCommand(ZarvexTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getColoredPrefix() + ChatColor.RED + "Este comando só pode ser executado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            List<String> playerTags = plugin.getDatabaseManager().getPlayerTags(player.getUniqueId());

            if (playerTags.isEmpty()) {
                player.sendMessage(getColoredPrefix() + ChatColor.RED + "Você não possui nenhuma tag.");
                return true;
            }

            player.sendMessage(getColoredPrefix() + ChatColor.LIGHT_PURPLE + "Suas tags:");
            for (String tagId : playerTags) {
                String displayName = plugin.getTags().get(tagId);
                if (displayName != null) {
                    TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', displayName));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tagId));
                    player.spigot().sendMessage(message);
                }
            }
            return true;
        }

        if (args.length == 1) {
            String tagId = args[0].toLowerCase();
            List<String> playerTags = plugin.getDatabaseManager().getPlayerTags(player.getUniqueId());

            if (!playerTags.contains(tagId)) {
                player.sendMessage(getColoredPrefix() + ChatColor.RED + "Você não possui a tag '" + tagId + "'.");
                return true;
            }

            plugin.getDatabaseManager().setSelectedTag(player.getUniqueId(), tagId);
            String displayName = plugin.getTags().get(tagId);
            player.sendMessage(getColoredPrefix() + ChatColor.WHITE + "Sua tag foi definida para: " + ChatColor.translateAlternateColorCodes('&', displayName));
            return true;
        }

    player.sendMessage(getColoredPrefix() + ChatColor.RED + "Uso correto: /tag <id_da_tag> ou /tag");
        return true;
    }
}
