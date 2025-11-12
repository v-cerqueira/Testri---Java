@echo off
echo === ArcaneTetris - Executando o jogo ===
echo.

REM Tenta encontrar e executar Maven
where mvn >nul 2>&1
if %ERRORLEVEL% == 0 (
    echo Maven encontrado! Compilando...
    call mvn clean compile
    if %ERRORLEVEL% == 0 (
        echo Executando jogo...
        call mvn javafx:run
    ) else (
        echo Erro na compilacao!
        pause
        exit /b 1
    )
) else (
    echo Maven nao encontrado no PATH.
    echo.
    echo Por favor, instale o Maven ou adicione-o ao PATH.
    echo Download: https://maven.apache.org/download.cgi
    echo.
    echo Ou execute manualmente:
    echo   mvn clean compile javafx:run
    echo.
    pause
    exit /b 1
)

pause




