# ZarvexTags Plugin Documentation

## Overview

ZarvexTags is a simple and efficient Spigot plugin that allows server administrators to set custom tags for players. These tags are displayed in chat and other plugins that support PlaceholderAPI.

## Features

*   Create and customize an unlimited number of tags.
*   Set tags for players using a simple command.
*   Remove tags from players.
*   PlaceholderAPI support for displaying tags in other plugins.
*   Easy to configure.

## Installation

1.  Download the latest version of the plugin.
2.  Place the `zarvextags-1.0-SNAPSHOT.jar` file in your server's `plugins` folder.
3.  Restart your server.

## Configuration

The plugin's configuration is located in the `config.yml` file in the `plugins/ZarvexTags` folder. Here you can define all the tags that will be available on the server.

### `config.yml`

```yaml
# Configuração de Tags para o plugin ZarvexTags
#
# Aqui você pode definir todas as tags que estarão disponíveis no servidor.
# A "chave" da tag (ex: "fundador") é o que você usará no comando /tag.
#
# name: O nome descritivo da tag.
# displayname: O texto que será exibido no placeholder, com suporte a códigos de cores (&).
tags:
  fundador:
    name: 'Fundador'
    displayname: '&4&lFUNDADOR'
  membro:
    name: 'Membro'
    displayname: '&7Membro'
```

*   **tags:** This is the main section for defining the tags.
*   **<tag_id>:** This is the unique identifier for the tag. It is used in the `/tag` command. For example, `fundador`.
*   **name:** This is the descriptive name of the tag.
*   **displayname:** This is the text that will be displayed in the placeholder. It supports color codes using the `&` symbol.

## Commands

### `/tag <player> <tag_id>`

This command sets a tag for a player.

*   **<player>:** The name of the player.
*   **<tag_id>:** The ID of the tag to set.

**Permission:** `zarvextags.tag`

### `/tag <player> remover`

This command removes the tag from a player.

*   **<player>:** The name of the player.

**Permission:** `zarvextags.tag`

## Placeholders

The plugin supports PlaceholderAPI. You can use the following placeholder to display the player's tag:

`%zarvextags_tag%`

This will display the `displayname` of the tag that the player has set.