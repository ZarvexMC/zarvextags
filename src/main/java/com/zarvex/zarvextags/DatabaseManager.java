package com.zarvex.zarvextags;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private final ZarvexTags plugin;
    private Connection connection;

    public DatabaseManager(ZarvexTags plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration config = plugin.getConfig();
        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String database = config.getString("database.database");
        String username = config.getString("database.username");
        String password = config.getString("database.password");

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database, username, password);
            plugin.getLogger().info("Conexão com o banco de dados estabelecida com sucesso!");
            createTables();
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Conexão com o banco de dados fechada com sucesso!");
            } catch (SQLException e) {
                plugin.getLogger().severe("Não foi possível fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }

    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS player_tags (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "tag_id VARCHAR(255) NOT NULL," +
                    "selected BOOLEAN NOT NULL DEFAULT FALSE" +
                    ")");
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível criar as tabelas do banco de dados: " + e.getMessage());
        }
    }

    public List<String> getPlayerTags(UUID playerUuid) {
        List<String> tags = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT tag_id FROM player_tags WHERE player_uuid = ?")) {
            statement.setString(1, playerUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tags.add(resultSet.getString("tag_id"));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível obter as tags do jogador: " + e.getMessage());
        }
        return tags;
    }

    public void addPlayerTag(UUID playerUuid, String tagId) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO player_tags (player_uuid, tag_id) VALUES (?, ?)")) {
            statement.setString(1, playerUuid.toString());
            statement.setString(2, tagId);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível adicionar a tag ao jogador: " + e.getMessage());
        }
    }

    public void removePlayerTag(UUID playerUuid, String tagId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM player_tags WHERE player_uuid = ? AND tag_id = ?")) {
            statement.setString(1, playerUuid.toString());
            statement.setString(2, tagId);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível remover a tag do jogador: " + e.getMessage());
        }
    }

    public void setSelectedTag(UUID playerUuid, String tagId) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE player_tags SET selected = FALSE WHERE player_uuid = ?")) {
            statement.setString(1, playerUuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível desmarcar a tag anterior: " + e.getMessage());
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE player_tags SET selected = TRUE WHERE player_uuid = ? AND tag_id = ?")) {
            statement.setString(1, playerUuid.toString());
            statement.setString(2, tagId);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível definir a tag selecionada: " + e.getMessage());
        }
    }

    public String getSelectedTag(UUID playerUuid) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT tag_id FROM player_tags WHERE player_uuid = ? AND selected = TRUE")) {
            statement.setString(1, playerUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("tag_id");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Não foi possível obter a tag selecionada do jogador: " + e.getMessage());
        }
        return null;
    }
}
