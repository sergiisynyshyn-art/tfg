#!/usr/bin/env bash
# VERIFICACIÓN FINAL - Google Sign-In Implementation para Serenum
# Este script verifica que todos los archivos están en su lugar

echo "═══════════════════════════════════════════════════════════════"
echo "  🔍 VERIFICACIÓN FINAL: GOOGLE SIGN-IN IMPLEMENTATION"
echo "═══════════════════════════════════════════════════════════════"
echo ""

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Arrays para tracking
FILES_JAVA=()
FILES_CONFIG=()
FILES_DOCS=()
FILES_MISSING=()

# Función para verificar archivo
check_file() {
    local file=$1
    local type=$2

    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $file"
        case $type in
            "java") FILES_JAVA+=("$file") ;;
            "config") FILES_CONFIG+=("$file") ;;
            "docs") FILES_DOCS+=("$file") ;;
        esac
    else
        echo -e "${RED}✗${NC} $file (NO ENCONTRADO)"
        FILES_MISSING+=("$file")
    fi
}

echo ""
echo "📄 ARCHIVOS JAVA:"
echo "────────────────────────────────────────────────────────────────"
check_file "app/src/main/java/com/example/serenum/LoginActivity.java" "java"
check_file "app/src/main/java/com/example/serenum/DatabaseHelper.java" "java"
check_file "app/src/main/java/com/example/serenum/EmailSender.java" "java"
check_file "app/src/main/java/com/example/serenum/MainActivity.java" "java"

echo ""
echo "⚙️  ARCHIVOS DE CONFIGURACIÓN:"
echo "────────────────────────────────────────────────────────────────"
check_file "app/build.gradle.kts" "config"
check_file "gradle/libs.versions.toml" "config"
check_file "app/src/main/AndroidManifest.xml" "config"

echo ""
echo "📚 ARCHIVOS DE DOCUMENTACIÓN:"
echo "────────────────────────────────────────────────────────────────"
check_file "README_GOOGLE_SIGNIN.md" "docs"
check_file "INDICE_DOCUMENTACION.md" "docs"
check_file "RESUMEN_GOOGLE_SIGNIN.md" "docs"
check_file "GUIA_RAPIDA_GOOGLE_SIGNIN.md" "docs"
check_file "DOCUMENTACION_GOOGLE_SIGNIN.md" "docs"
check_file "EJEMPLOS_USO_GOOGLE_SIGNIN.md" "docs"
check_file "CHECKLIST_IMPLEMENTACION.md" "docs"
check_file "ENTREGA_FINAL.md" "docs"

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "📊 ESTADÍSTICAS:"
echo "═══════════════════════════════════════════════════════════════"
printf "Archivos Java:             %3d\n" "${#FILES_JAVA[@]}"
printf "Archivos de Configuración: %3d\n" "${#FILES_CONFIG[@]}"
printf "Archivos de Documentación: %3d\n" "${#FILES_DOCS[@]}"
printf "Total Archivos:            %3d\n" $((${#FILES_JAVA[@]} + ${#FILES_CONFIG[@]} + ${#FILES_DOCS[@]}))

if [ ${#FILES_MISSING[@]} -gt 0 ]; then
    echo ""
    echo -e "${RED}⚠️  ARCHIVOS FALTANTES (${#FILES_MISSING[@]}):${NC}"
    echo "────────────────────────────────────────────────────────────────"
    for file in "${FILES_MISSING[@]}"; do
        echo "  - $file"
    done
else
    echo ""
    echo -e "${GREEN}✅ TODOS LOS ARCHIVOS ESTÁN PRESENTES${NC}"
fi

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "🚀 PRÓXIMOS PASOS:"
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "1. Lee: README_GOOGLE_SIGNIN.md"
echo "2. Sigue: GUIA_RAPIDA_GOOGLE_SIGNIN.md"
echo "3. Verifica: CHECKLIST_IMPLEMENTACION.md"
echo "4. Consulta: INDICE_DOCUMENTACION.md para navegar"
echo ""
echo "═══════════════════════════════════════════════════════════════"

# Salida
if [ ${#FILES_MISSING[@]} -eq 0 ]; then
    exit 0  # Éxito
else
    exit 1  # Error
fi

