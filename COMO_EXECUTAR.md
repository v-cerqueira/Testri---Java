# 🚀 Como Executar o ArcaneTetris

## 📋 Pré-requisitos

1. **Java 17 ou superior** ✅ (Você tem Java 25 instalado)
2. **Maven 3.6+** ⚠️ (Precisa instalar ou adicionar ao PATH)

## 🔧 Instalação do Maven

### Opção 1: Download Manual
1. Baixe o Maven em: https://maven.apache.org/download.cgi
2. Extraia para `C:\Program Files\Apache\maven` (ou outro diretório)
3. Adicione `C:\Program Files\Apache\maven\bin` ao PATH do sistema

### Opção 2: Via Chocolatey (Recomendado)
```powershell
choco install maven
```

### Opção 3: Via Scoop
```powershell
scoop install maven
```

## ▶️ Executando o Jogo

### Método 1: Maven (Recomendado)

Abra o PowerShell ou CMD no diretório do projeto e execute:

```bash
# Compilar
mvn clean compile

# Executar
mvn javafx:run
```

Ou tudo de uma vez:
```bash
mvn clean compile javafx:run
```

### Método 2: Script Batch

Execute o arquivo `run.bat` que foi criado:
```bash
run.bat
```

### Método 3: Via IDE

Se você usar uma IDE como IntelliJ IDEA ou Eclipse:

1. **IntelliJ IDEA**:
   - Abra o projeto
   - Clique com botão direito em `pom.xml` → "Add as Maven Project"
   - Execute `Main.java` diretamente (a IDE gerencia as dependências)

2. **Eclipse**:
   - Importe como projeto Maven
   - Execute `Main.java` como Java Application

3. **VS Code**:
   - Instale a extensão "Extension Pack for Java"
   - Abra a classe `Main.java`
   - Clique em "Run" (▶️)

## 🐛 Solução de Problemas

### Erro: "mvn não é reconhecido"
- Instale o Maven ou adicione ao PATH
- Verifique com: `mvn --version`

### Erro: "JavaFX runtime components are missing"
- Execute via Maven (`mvn javafx:run`)
- Ou adicione os módulos JavaFX manualmente

### Erro de Compilação
- Verifique se está usando Java 17+
- Execute `mvn clean` primeiro
- Verifique se todas as dependências foram baixadas

### Erro ao carregar CSS
- Verifique se `src/main/resources/styles/arcane.css` existe
- O arquivo deve estar na pasta `resources`

## 📝 Comandos Úteis

```bash
# Limpar e compilar
mvn clean compile

# Executar testes
mvn test

# Criar JAR executável
mvn clean package

# Ver dependências
mvn dependency:tree

# Atualizar dependências
mvn clean install -U
```

## 🎮 Após Executar

Quando o jogo iniciar, você verá:
- Menu principal com opções
- Single Player
- Local Multiplayer
- Online Multiplayer
- Opções

Use as teclas conforme descrito no README.md para jogar!

---

**Dica**: Se tiver problemas, verifique se o Java está no PATH:
```bash
java -version
```

E se o Maven está instalado:
```bash
mvn --version
```

