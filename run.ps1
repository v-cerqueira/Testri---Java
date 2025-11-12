# Script para executar ArcaneTetris
Write-Host "=== ArcaneTetris - Executando o jogo ===" -ForegroundColor Cyan

# Tenta encontrar Maven
$mvnPath = $null
$possibleMavenPaths = @(
    "mvn",
    "C:\Program Files\Apache\maven\bin\mvn.cmd",
    "C:\apache-maven\bin\mvn.cmd",
    "$env:USERPROFILE\apache-maven\bin\mvn.cmd",
    "C:\tools\maven\bin\mvn.cmd"
)

foreach ($path in $possibleMavenPaths) {
    try {
        $result = Get-Command $path -ErrorAction SilentlyContinue
        if ($result) {
            $mvnPath = $path
            Write-Host "Maven encontrado: $mvnPath" -ForegroundColor Green
            break
        }
    } catch {
        continue
    }
}

if ($mvnPath) {
    Write-Host "Compilando projeto..." -ForegroundColor Yellow
    & $mvnPath clean compile
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Executando jogo..." -ForegroundColor Yellow
        & $mvnPath javafx:run
    } else {
        Write-Host "Erro na compilação!" -ForegroundColor Red
    }
} else {
    Write-Host "Maven não encontrado. Tentando executar diretamente com Java..." -ForegroundColor Yellow
    
    # Verifica se há classes compiladas
    if (Test-Path "target\classes\com\arcane\tetris\app\Main.class") {
        Write-Host "Classes encontradas. Mas JavaFX precisa do Maven para gerenciar dependências." -ForegroundColor Yellow
        Write-Host "Por favor, instale o Maven ou adicione-o ao PATH." -ForegroundColor Yellow
        Write-Host "Download: https://maven.apache.org/download.cgi" -ForegroundColor Cyan
    } else {
        Write-Host "Projeto não compilado. Por favor, compile primeiro com Maven." -ForegroundColor Red
    }
}

Write-Host "`nPressione qualquer tecla para sair..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")




