package com.zarvex.zarvextags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class TagCommand implements CommandExecutor {

    private final ZarvexTags plugin;
    private final Logger logger;

    public TagCommand(ZarvexTags plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        logger.info("Comando /tag executado por " + sender.getName() + " com argumentos: " + String.join(" ", args));

        if (!sender.hasPermission("zarvextags.tag")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uso correto: /tag <jogador> <id_da_tag>");
            sender.sendMessage(ChatColor.GRAY + "Para remover a tag de um jogador, use: /tag <jogador> remover");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "O jogador " + args[0] + " não está online.");
            return true;
        }

        String tagId = args[1].toLowerCase();

        if (tagId.equalsIgnoreCase("remover")) {
            plugin.setPlayerTag(target.getUniqueId(), null);
            sender.sendMessage(ChatColor.GREEN + "A tag do jogador " + target.getName() + " foi removida.");
            logger.info("Tag de " + target.getName() + " removida por " + sender.getName());
            return true;
        }

        if (!plugin.getTags().containsKey(tagId)) {
            sender.sendMessage(ChatColor.RED + "A tag '" + tagId + "' não existe.");
            logger.warning("Tentativa de usar tag inexistente '" + tagId + "' por " + sender.getName());
            return true;
        }

        plugin.setPlayerTag(target.getUniqueId(), tagId);

        String displayName = plugin.getTags().get(tagId);
        sender.sendMessage(ChatColor.GREEN + "A tag do jogador " + target.getName() + " foi definida para: " + ChatColor.translateAlternateColorCodes('&', displayName));
        logger.info("Tag de " + target.getName() + " definida para '" + tagId + "' por " + sender.getName());
        return true;
    }
}
