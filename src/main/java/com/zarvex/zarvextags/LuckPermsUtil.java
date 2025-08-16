package com.zarvex.zarvextags;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class LuckPermsUtil {
    public static void setTagPermission(UUID uuid, String tagId) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) {
            // Tenta carregar o usuário se não estiver em cache
            luckPerms.getUserManager().loadUser(uuid).thenAccept(u -> {
                if (u != null) {
                    setTagPermissionInternal(u, tagId);
                    luckPerms.getUserManager().saveUser(u);
                }
            });
        } else {
            setTagPermissionInternal(user, tagId);
            luckPerms.getUserManager().saveUser(user);
        }
    }

    private static void setTagPermissionInternal(User user, String tagId) {
        // Remove todas permissões zarvextags.*
        user.data().clear(node -> node.getKey().startsWith("zarvextags."));
        // Adiciona a permissão da tag selecionada
        Node node = Node.builder("zarvextags." + tagId).value(true).build();
        user.data().add(node);
    }
}
