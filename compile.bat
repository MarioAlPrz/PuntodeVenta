@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ===================================
echo   Compilando Punto de Venta
echo ===================================
echo.

if not exist bin mkdir bin

echo Buscando archivos Java...
dir /s /b src\*.java > sources.txt

echo Compilando codigo...
javac -cp "lib\*" -d bin @sources.txt

if %ERRORLEVEL% equ 0 (
    del /f /q sources.txt >nul 2>&1
    echo.
    echo ===================================
    echo   Compilacion exitosa!
    echo   Iniciando programa...
    echo ===================================
    echo.
    java -cp "bin;lib\*" Main
) else (
    echo.
    echo ===================================
    echo   ERROR EN LA COMPILACION
    echo ===================================
    echo Revisa si tienes el JDK de Java instalado.
    echo.
)

pause

