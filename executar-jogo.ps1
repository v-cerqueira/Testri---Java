# Script para executar ArcaneTetris sem Maven instalado
Write-Host "=== ArcaneTetris - Executando ===" -ForegroundColor Cyan

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir

Write-Host "Diretório do projeto: $projectDir" -ForegroundColor Gray

# Verifica Java
$javaCmd = "java"
try {
    $javaVersion = & $javaCmd -version 2>&1
    Write-Host "Java encontrado!" -ForegroundColor Green
} catch {
    Write-Host "ERRO: Java não encontrado! Instale Java 17+" -ForegroundColor Red
    exit 1
}

# Compila as classes Java
Write-Host "`nCompilando projeto..." -ForegroundColor Yellow

$srcDir = Join-Path $projectDir "src\main\java"
$targetDir = Join-Path $projectDir "target\classes"
$resourcesDir = Join-Path $projectDir "src\main\resources"

# Cria diretório de destino
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
}

# Encontra dependências JavaFX no repositório Maven local
$mavenRepo = Join-Path $env:USERPROFILE ".m2\repository"
$javafxPath = Join-Path $mavenRepo "org\openjfx"

if (-not (Test-Path $javafxPath)) {
    Write-Host "ERRO: Dependências JavaFX não encontradas!" -ForegroundColor Red
    Write-Host "Por favor, instale o Maven e execute: mvn dependency:resolve" -ForegroundColor Yellow
    Write-Host "Ou use uma IDE como IntelliJ IDEA/Eclipse" -ForegroundColor Yellow
    exit 1
}

# Monta classpath com todas as dependências
$classpath = @()
$classpath += $targetDir

# Adiciona JavaFX ao classpath
$javafxModules = @("javafx-controls", "javafx-fxml")
foreach ($module in $javafxModules) {
    $modulePath = Get-ChildItem -Path (Join-Path $javafxPath $module) -Recurse -Filter "*.jar" | Select-Object -First 1 -ExpandProperty FullName
    if ($modulePath) {
        $classpath += $modulePath
    }
}

# Adiciona Jackson
$jacksonPath = Join-Path $mavenRepo "com\fasterxml\jackson\core"
$jacksonJars = Get-ChildItem -Path $jacksonPath -Recurse -Filter "*.jar" | Select-Object -ExpandProperty FullName
$classpath += $jacksonJars

# Adiciona SLF4J
$slf4jPath = Join-Path $mavenRepo "org\slf4j"
$slf4jJars = Get-ChildItem -Path $slf4jPath -Recurse -Filter "*.jar" | Select-Object -ExpandProperty FullName
$classpath += $slf4jJars

$classpathStr = $classpath -join ";"

Write-Host "`nExecutando jogo..." -ForegroundColor Yellow
Write-Host "Isso pode demorar na primeira vez (baixando dependências)..." -ForegroundColor Gray

# Executa o jogo
$mainClass = "com.arcane.tetris.app.Main"

& $javaCmd --module-path ($classpath -join ";") `
    --add-modules javafx.controls,javafx.fxml `
    -cp $classpathStr `
    $mainClass

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nERRO ao executar! Tente:" -ForegroundColor Red
    Write-Host "1. Instalar Maven: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "2. Executar: mvn clean compile javafx:run" -ForegroundColor Yellow
    Write-Host "3. Ou usar uma IDE como IntelliJ IDEA" -ForegroundColor Yellow
}




