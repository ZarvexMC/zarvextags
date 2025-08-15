package com.zarvex.zarvextags;

import static com.zarvex.zarvextags.ZarvexTags.getColoredPrefix;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagModCommand implements CommandExecutor {

    private final ZarvexTags plugin;
    private final DatabaseManager databaseManager;

    public TagModCommand(ZarvexTags plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getColoredPrefix() + "Este comando só pode ser executado por um jogador.");
            return true;
        }

        if (!sender.hasPermission("zarvextags.admin")) {
            sender.sendMessage(getColoredPrefix() + "§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(getColoredPrefix() + "§cUso correto: /tagmod <add|remove> <jogador> <tag>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String targetPlayerName = args[1];
        String tagId = args[2];

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);

        if (targetPlayer == null || (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline())) {
            sender.sendMessage(getColoredPrefix() + "§cJogador '" + targetPlayerName + "' não encontrado ou nunca jogou no servidor.");
            return true;
        }

        if (!plugin.getConfig().getConfigurationSection("tags").contains(tagId)) {
            sender.sendMessage(getColoredPrefix() + "§cTag '" + tagId + "' não encontrada na configuração.");
            return true;
        }

        switch (subCommand) {
            case "add":
                databaseManager.addPlayerTag(targetPlayer.getUniqueId(), tagId);
                sender.sendMessage(getColoredPrefix() + "§aTag '" + tagId + "' adicionada ao jogador '" + targetPlayerName + "' com sucesso!");
                if (targetPlayer.isOnline()) {
                    Player onlineTarget = targetPlayer.getPlayer();
                    onlineTarget.sendMessage(getColoredPrefix() + "§aVocê recebeu a tag '" + plugin.getConfig().getString("tags." + tagId + ".displayname") + "§a'!");
                }
                break;
            case "remove":
                databaseManager.removePlayerTag(targetPlayer.getUniqueId(), tagId);
                sender.sendMessage(getColoredPrefix() + "§aTag '" + tagId + "' removida do jogador '" + targetPlayerName + "' com sucesso!");
                if (targetPlayer.isOnline()) {
                    Player onlineTarget = targetPlayer.getPlayer();
                    onlineTarget.sendMessage(getColoredPrefix() + "§cSua tag '" + plugin.getConfig().getString("tags." + tagId + ".displayname") + "§c' foi removida!");
                }
                break;
            default:
                sender.sendMessage(getColoredPrefix() + "§cUso correto: /tagmod <add|remove> <jogador> <tag>");
                break;
        }

        return true;
    }
}