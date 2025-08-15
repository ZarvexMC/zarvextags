# Documentação do Plugin ZarvexTags (v2)

Este documento foi gerado pela IA Gemini e detalha a estrutura e o funcionamento do plugin ZarvexTags após a implementação do sistema de configuração de tags.

---

## 1. Visão Geral do Plugin

O ZarvexTags é um plugin para Spigot 1.8.9 que cria um sistema de tags baseado em um arquivo de configuração. Sua principal função é permitir que administradores definam uma lista de tags em um arquivo `config.yml` e as atribuam aos jogadores através de um comando.

- **Comando Principal:** `/tag <jogador> <id_da_tag>`
- **Placeholder Gerado:** `%zarvextags-tag%`
- **Dependência Chave:** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.624/)

---

## 2. Como Funciona (Arquitetura)

A arquitetura foi modificada para ser mais robusta e configurável.

### a. `config.yml` (Arquivo de Configuração)
Localizado em `plugins/ZarvexTags/config.yml`, este arquivo é o coração do novo sistema. Nele, você define todas as tags disponíveis.
- **Estrutura:** Possui uma seção principal `tags`, onde cada sub-chave é um ID único para a tag (ex: `fundador`).
- **Propriedades da Tag:** Cada tag tem um `name` (descritivo) e um `displayname` (o que aparece no jogo, com suporte a cores `&`).

### b. `playerdata.yml` (Dados dos Jogadores)
Para garantir que os jogadores não percam suas tags ao reiniciar o servidor, o plugin agora cria este arquivo. Ele armazena um mapa simples de `UUID` do jogador para o `ID da tag` que ele tem equipada.

### c. `ZarvexTags.java` (Classe Principal)
Agora gerencia os arquivos de configuração e os dados dos jogadores.
- **`onEnable()`:** Carrega o `config.yml` (e o cria com valores padrão se não existir) e carrega os dados do `playerdata.yml` para um `HashMap` em memória.
- **`onDisable()`:** Salva o estado atual do `HashMap` de tags de jogadores no `playerdata.yml`.
- **`setPlayerTag()`:** Um novo método central para definir a tag de um jogador, que atualiza tanto o mapa em memória quanto o arquivo `playerdata.yml`.

### d. `TagCommand.java` (Executor de Comando)
A lógica do comando foi completamente alterada:
- **Uso:** `/tag <jogador> <id_da_tag>` ou `/tag <jogador> remover`.
- **Validação:** O comando agora verifica se o `<id_da_tag>` fornecido existe na lista de tags do `config.yml`.
- **Atribuição:** Se a tag for válida, ele chama `plugin.setPlayerTag()` para atribuir a tag ao jogador.
- **Remoção:** O argumento `remover` permite limpar a tag de um jogador.

### e. `TagsExpansion.java` (Expansão do PlaceholderAPI)
A expansão agora é mais inteligente:
- **`onPlaceholderRequest()`:**
  1. Pega o ID da tag do jogador (ex: "fundador") a partir dos dados carregados.
  2. Usa esse ID para procurar a tag correspondente no `config.yml`.
  3. Obtém o valor de `displayname` dessa tag.
  4. Retorna o `displayname` formatado com cores para ser exibido no jogo.

---

## 3. Manutenção do Plugin

### Compilando o Plugin
Para gerar o arquivo `.jar` após fazer alterações no código, execute:
```bash
mvn clean package
```
O arquivo final estará em `target/zarvextags-1.0-SNAPSHOT.jar`.

### Configurando Tags
Para adicionar, remover ou editar tags, modifique o arquivo `plugins/ZarvexTags/config.yml` e reinicie o servidor ou use um comando de reload de outro plugin.