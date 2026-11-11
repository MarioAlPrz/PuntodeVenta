#!/bin/bash
# =====================================================
# Punto de Venta - Script de Compilación y Ejecución
# Proyecto Escolar 2025
# =====================================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"

# Classpath con todos los JARs en lib
CP="$LIB_DIR/*"

echo "🔨 Compilando Punto de Venta..."
echo ""

# Crear directorio de salida
mkdir -p "$BIN_DIR"
rm -rf "$BIN_DIR"/*

# Compilar todos los archivos Java
find "$SRC_DIR" -name "*.java" > /tmp/pv_sources.txt
javac -encoding UTF-8 -cp "$CP" -d "$BIN_DIR" @/tmp/pv_sources.txt 2>&1

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ ¡Compilación exitosa!"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "  Para ejecutar:"
    echo "  java -cp \"bin:lib/*\" Main"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""

    # Preguntar si quiere ejecutar
    read -p "¿Ejecutar ahora? (s/n): " respuesta
    if [ "$respuesta" = "s" ] || [ "$respuesta" = "S" ]; then
        echo ""
        echo "🚀 Iniciando aplicación..."
        java -cp "bin:$LIB_DIR/*" Main
    fi
else
    echo ""
    echo "❌ Error en la compilación"
    exit 1
fi
