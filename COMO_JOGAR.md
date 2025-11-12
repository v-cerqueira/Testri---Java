# 🎮 Como Jogar ArcaneTetris

## 🎯 Objetivo do Jogo

ArcaneTetris é um jogo de Tetris com elementos mágicos e multiplayer. O objetivo é:
- **Single Player**: Eliminar o máximo de linhas possível antes de o tabuleiro encher
- **Multiplayer**: Fazer o adversário perder primeiro (tabuleiro encher)

## 🎹 Controles - Player 1

### Movimento Básico
- **A** ou **← (Seta Esquerda)**: Mover peça para esquerda
- **D** ou **→ (Seta Direita)**: Mover peça para direita
- **S** ou **↓ (Seta Baixo)**: Soft drop (peça cai mais rápido)
- **W** ou **↑ (Seta Cima)**: Rotacionar peça (sentido horário)
- **Espaço**: Hard drop (peça cai instantaneamente)

### Feitiços
- **Q**: PushLine (20 Mana) - Envia uma linha para o adversário *(só multiplayer)*
- **E**: TimeSlow (30 Mana) - Reduz a velocidade do adversário *(só multiplayer)*
- **R**: Bomb (40 Mana) - Remove um bloco 3x3 no tabuleiro do adversário *(só multiplayer)*
- **T**: PieceSwap (25 Mana) - Troca a peça atual com a próxima
- **F**: Shield (35 Mana) - Protege você de feitiços por 8 segundos

### Outros
- **ESC**: Pausar/Despausar o jogo

## 🎹 Controles - Player 2 (Multiplayer Local)

### Movimento Básico
- **J** ou **Numpad 4**: Mover peça para esquerda
- **L** ou **Numpad 6**: Mover peça para direita
- **K** ou **Numpad 5**: Soft drop
- **I** ou **Numpad 8**: Rotacionar peça
- **Numpad 0**: Hard drop

### Feitiços
- **U**: PushLine (20 Mana)
- **O**: TimeSlow (30 Mana)
- **P**: Bomb (40 Mana)
- **Y**: PieceSwap (25 Mana)
- **H**: Shield (35 Mana)

### Outros
- **ESC**: Pausar/Despausar o jogo

## 🔮 Sistema de Mana e Feitiços

### Como Ganhar Mana
- Você ganha **10 Mana** por cada linha eliminada
- Mana máxima: **100**

### Feitiços Disponíveis

#### 1. **Enviar Linha** (20 Mana) *(só multiplayer)*
- **Efeito**: Envia uma linha incompleta para o adversário
- **Uso**: Estratégico para pressionar o oponente
- **Cooldown**: 6 segundos

#### 2. **Lentidão** (30 Mana) *(só multiplayer)*
- **Efeito**: Reduz a velocidade de queda das peças do adversário
- **Uso**: Dá mais tempo para você e dificulta para o oponente
- **Cooldown**: 12 segundos

#### 3. **Trocar Peça** (25 Mana)
- **Efeito**: Troca a peça atual com a próxima na fila
- **Uso**: Útil quando a peça atual não se encaixa bem
- **Cooldown**: 10 segundos

#### 4. **Bomba** (40 Mana) *(só multiplayer)*
- **Efeito**: Remove as 2 linhas completas mais altas do tabuleiro do adversário
- **Uso**: Sabota o adversário removendo linhas completas do topo do tabuleiro dele
- **Cooldown**: 18 segundos

#### 5. **Escudo** (35 Mana)
- **Efeito**: Remove a última linha completa (base) do seu tabuleiro e protege de feitiços por 8 segundos
- **Uso**: Defensivo, remove uma linha completa da base e evita receber feitiços do adversário
- **Cooldown**: 20 segundos

## 🎯 Dicas e Estratégias

### Básicas
1. **Não deixe buracos**: Tente manter o tabuleiro o mais plano possível
2. **Planeje a próxima peça**: Olhe sempre a próxima peça na fila
3. **Use hard drop**: Economize tempo usando Espaço quando necessário
4. **Mantenha o centro limpo**: Deixe espaço no centro para peças maiores

### Estratégias com Feitiços
1. **PushLine**: Use quando o adversário estiver com o tabuleiro alto
2. **TimeSlow**: Use em momentos críticos quando o adversário precisa de velocidade
3. **Bomba**: Use para sabotar o adversário, especialmente quando ele tem linhas completas no topo - remove as 2 mais altas!
4. **Trocar Peça**: Use quando a peça atual não se encaixa bem e a próxima seria melhor
5. **Escudo**: Use quando você tiver uma linha completa na base para removê-la, ou preventivamente quando o adversário estiver com muita mana

### Multiplayer
1. **Economize mana no início**: Acumule mana para usar em momentos críticos
2. **Observe o adversário**: Veja quando ele está vulnerável para atacar
3. **Defesa também é importante**: Use Shield quando necessário
4. **Coordene ataques**: Combine PushLine com TimeSlow para máximo efeito

## 📊 Sistema de Pontuação

- **1 linha**: Pontos base
- **2 linhas**: Multiplicador 2x
- **3 linhas**: Multiplicador 3x
- **4 linhas (Tetris)**: Multiplicador 4x + bônus

## 🎮 Modos de Jogo

### Single Player
- Jogue sozinho contra o tempo
- Aumente sua pontuação máxima
- Pratique suas habilidades

### Local Multiplayer
- Jogue contra um amigo no mesmo teclado
- Divisão de tela
- Use feitiços para sabotar o oponente

### Online Multiplayer
- Jogue contra jogadores online (LAN)
- Requer servidor rodando
- Mesma mecânica do multiplayer local

## ⚠️ Game Over

O jogo termina quando:
- O tabuleiro enche completamente (peças chegam ao topo)
- Não há mais espaço para novas peças

## 🔄 Recursos do Jogo

- **Preview da próxima peça**: Sempre visível na tela
- **HUD (Heads-Up Display)**: Mostra pontuação, nível, linhas e mana
- **Indicador de cooldown**: Mostra quando os feitiços podem ser usados novamente
- **Barra de mana**: Visualização clara da mana disponível
- **Aceleração progressiva**: As peças caem cada vez mais rápido conforme você avança de nível
- **Feitiços inteligentes**: No modo single player, feitiços que requerem adversário não gastam mana

---

**Dica Pro**: Pratique no modo Single Player antes de jogar multiplayer para dominar os feitiços e controles!

