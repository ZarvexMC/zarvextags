package com.zarvex.zarvextags;

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
            sender.sendMessage("Este comando só pode ser executado por um jogador.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("zarvextags.tagmod")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUso correto: /tagmod <jogador> <tag>");
            return true;
        }

        String targetPlayerName = args[0];
        String tagId = args[1];

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            player.sendMessage("§cJogador não encontrado ou nunca jogou no servidor.");
            return true;
        }

        if (!plugin.getConfig().getConfigurationSection("tags").contains(tagId)) {
            player.sendMessage("§cTag '" + tagId + "' não encontrada na configuração.");
            return true;
        }

        // Add the tag to the player in the database
        databaseManager.addPlayerTag(targetPlayer.getUniqueId(), tagId);
        player.sendMessage("§aTag '" + tagId + "' adicionada ao jogador '" + targetPlayerName + "' com sucesso!");

        // If the target player is online, update their tags
        if (targetPlayer.isOnline()) {
            Player onlineTarget = targetPlayer.getPlayer();
            // You might want to send a message to the target player as well
            onlineTarget.sendMessage("§aVocê recebeu a tag '" + plugin.getConfig().getString("tags." + tagId + ".displayname") + "§a'!");
            // Re-apply placeholders if necessary (e.g., if using PlaceholderAPI)
            // PlaceholderAPI.setPlaceholders(onlineTarget, "%zarvextags_tag%"); // Example
        }

        return true;
    }
}
