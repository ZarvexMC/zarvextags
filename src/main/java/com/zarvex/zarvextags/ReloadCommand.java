package com.zarvex.zarvextags;

import static com.zarvex.zarvextags.ZarvexTags.getColoredPrefix;

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
        if (command.getName().equalsIgnoreCase("tagreload")) {
            if (sender.hasPermission("zarvextags.reload")) {
                plugin.reloadConfig();
                plugin.loadTags();
                sender.sendMessage(getColoredPrefix() + ChatColor.GREEN + "As tags foram recarregadas com sucesso!");
                plugin.getLogger().info("Tags recarregadas por " + sender.getName());
            } else {
                sender.sendMessage(getColoredPrefix() + ChatColor.RED + "Você não tem permissão para usar este comando.");
            }
            return true;
        }
        return false;
    }
}
