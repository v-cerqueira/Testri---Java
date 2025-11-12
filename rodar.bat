@echo off
chcp 65001 >nul
cd /d "%~dp0"
set JAVA_HOME=C:\Program Files\Java\jdk-25
if exist "mvnw.cmd" (
    echo Compilando e executando...
    call mvnw.cmd clean compile javafx:run
) else (
    echo Maven Wrapper nao encontrado!
    pause
)

