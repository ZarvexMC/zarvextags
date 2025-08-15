package com.zarvex.zarvextags;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final ZarvexTags plugin;

    public ReloadCommand(ZarvexTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("zarvextags.reload")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        plugin.reloadConfig();
        plugin.loadTags();
        sender.sendMessage(ChatColor.GREEN + "As tags foram recarregadas com sucesso!");
        plugin.getLogger().info("Tags recarregadas por " + sender.getName());

        return true;
    }
}
