# 🧙‍♂️ ArcaneTetris

**Tetris Mágico + Multiplayer + Inteligência Artificial**

ArcaneTetris é um jogo de Tetris competitivo com elementos de RPG mágico, desenvolvido em Java 17+ com JavaFX.

## 🎯 Características

- **Tetris Clássico**: Lógica sólida de peças, colisões e pontuação
- **Sistema Mágico**: Feitiços estratégicos com sistema de mana
- **Multiplayer**: Local (split-screen) e online (LAN)
- **IA Inteligente**: Heurística avançada para bots

## 🔮 Feitiços Disponíveis

- **PushLine** (20 Mana): Envia uma linha para o adversário
- **TimeSlow** (30 Mana): Reduz velocidade do adversário
- **PieceSwap** (25 Mana): Troca a peça atual com a próxima
- **Bomb** (40 Mana): Remove um bloco 3x3
- **Shield** (35 Mana): Protege por 8 segundos

## ⚙️ Tecnologias

- **Java 17+**
- **JavaFX 21**: Interface gráfica
- **Jackson**: Serialização JSON para rede
- **SLF4J**: Sistema de logs
- **JUnit 5**: Testes unitários
- **Maven**: Gerenciamento de dependências

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+

### Compilação e Execução

```bash
# Compilar o projeto
mvn clean compile

# Executar o jogo
mvn javafx:run

# Executar testes
mvn test
```

### Executar JAR

```bash
mvn clean package
java -jar target/arcane-tetris-1.0.0.jar
```

## 🎮 Controles

### Player 1
- **A / ←**: Mover esquerda
- **D / →**: Mover direita
- **S / ↓**: Soft drop
- **W / ↑**: Rotacionar
- **Espaço**: Hard drop
- **Q**: Feitiço PushLine
- **E**: Feitiço TimeSlow
- **R**: Feitiço Bomb
- **ESC**: Pausar

### Player 2 (Multiplayer Local)
- **J / Numpad 4**: Mover esquerda
- **L / Numpad 6**: Mover direita
- **K / Numpad 5**: Soft drop
- **I / Numpad 8**: Rotacionar
- **Numpad 0**: Hard drop
- **U**: Feitiço PushLine
- **O**: Feitiço TimeSlow

## 📁 Estrutura do Projeto

```
com.arcane.tetris
├── app
│   └── Main.java
├── engine
│   ├── GameEngine.java
│   ├── Board.java
│   ├── Piece.java
│   ├── Player.java
│   └── spells/
├── ui
│   ├── MainMenuController.java
│   ├── GameController.java
│   └── HUDController.java
├── net
│   ├── Server.java
│   ├── Client.java
│   └── NetProtocol.java
├── ai
│   └── AiController.java
└── util
    ├── Logger.java
    └── Config.java
```

## 💾 Configuração

As configurações são salvas em `~/.arcane_tetris/config.json`:

```json
{
  "manaPerLine": 10,
  "maxMana": 100,
  "spells": {
    "PushLine": {"cost": 20, "cooldown": 6},
    "TimeSlow": {"cost": 30, "cooldown": 12},
    "PieceSwap": {"cost": 25, "cooldown": 10},
    "Bomb": {"cost": 40, "cooldown": 18},
    "Shield": {"cost": 35, "cooldown": 20}
  },
  "aiWeights": {
    "height": 0.5,
    "holes": 0.7,
    "bumpiness": 0.3,
    "lines": -1.0
  }
}
```

## 🌐 Multiplayer Online

### Iniciar Servidor

```java
Server server = new Server(8080);
server.start();
```

### Conectar Cliente

```java
Client client = new Client("localhost", 8080, "player1");
client.connect();
```

## 🧠 IA Heurística

A IA avalia jogadas usando a fórmula:

```
score = w1 * height + w2 * holes + w3 * bumpiness + w4 * linesCleared
```

Onde:
- **height**: Altura agregada do tabuleiro
- **holes**: Número de buracos
- **bumpiness**: Variação de altura entre colunas
- **linesCleared**: Linhas completadas

## 🧪 Testes

Execute os testes unitários:

```bash
mvn test
```

Cobertura atual: Engine base (>60%)

## 📝 Licença

Este projeto é um trabalho acadêmico/educacional.

## 🎨 Tema Visual

Paleta de cores arcana:
- **Roxo escuro**: `#1a0d2e`
- **Roxo médio**: `#5a189a`
- **Roxo claro**: `#9d4edd`
- **Dourado**: `#ffd60a`
- **Azul escuro**: `#0d1b2a`

## 🔮 Próximas Melhorias

- [ ] Modo de aprendizado por reforço (Q-Learning/PPO)
- [ ] Sistema de replay
- [ ] Leaderboard online
- [ ] Editor de feitiços visual
- [ ] Skins customizáveis
- [ ] Efeitos sonoros

---

Desenvolvido com 🧙‍♂️ e JavaFX

