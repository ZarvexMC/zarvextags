package com.zarvex.zarvextags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TagCommand implements CommandExecutor {

    private final ZarvexTags plugin;

    public TagCommand(ZarvexTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("zarvextags.tag")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uso correto: /tag <jogador> <tag>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "O jogador " + args[0] + " não está online.");
            return true;
        }

        String tag = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String formattedTag = ChatColor.translateAlternateColorCodes('&', tag);

        plugin.getPlayerTags().put(target.getUniqueId(), formattedTag);

        sender.sendMessage(ChatColor.GREEN + "A tag do jogador " + target.getName() + " foi definida para: " + formattedTag);
        return true;
    }
}
