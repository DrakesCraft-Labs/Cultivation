<div align="center">

  <img src="https://raw.githubusercontent.com/DrakesCraft-Labs/Cultivation_Updated/main/banner.svg" alt="Cultivation_Updated Banner" width="920" />

# 🧪 Cultivation-Drake

**Addon de Slimefun4 con Aceleración Nativa en Rust (Java 21 Project Panama FFM API)**

<p>
  <a href="https://github.com/DrakesCraft-Labs/Cultivation_Updated"><img src="https://img.shields.io/badge/GitHub-Cultivation--Drake-181717?style=for-the-badge&logo=github" alt="GitHub"/></a>
  <img src="https://img.shields.io/badge/Java-21_FFM_Panama-F89820?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21 FFM"/>
  <img src="https://img.shields.io/badge/Rust-FFM_Accelerated-FF4500?style=for-the-badge&logo=rust&logoColor=white" alt="Rust Native"/>
  <img src="https://img.shields.io/badge/Paper-1.21.11-38BDF8?style=for-the-badge&logo=minecraft&logoColor=white" alt="Paper 1.21.11"/>
</p>

</div>

---

## ⚡ Novedades del Modelo Híbrido Cero-Riesgo

`Cultivation-Drake` integra el componente Panama FFM **`RustNativeBridge`** para delegar la aceleración de tickers de máquinas y cálculos pesados directamente al motor nativo `Slimefun-Rust` (`slimefun_ffi`):
- 🚀 **Procesamiento de Ticks en Nanosegundos**: Multi-hilo paralelo real en CPU sin pausas de Garbage Collector.
- 🛡️ **Preservación Total sin Reset (SQLite 0-Reset)**: Mantiene intactos todos los bloques e inventarios existentes en `stored-blocks.db`.

---

## 🛠️ Compilación

```bash
mvn clean package
```

## 🌱 Guía de cruces

La referencia operativa es el **Diccionario de Cruces** de la guía de Slimefun:
lee las recetas cargadas por esta versión del servidor y evita que una tabla web
desactualizada entregue combinaciones inválidas.

La guía de soporte y desarrollo está en
[docs/GUIA_DE_CRUCES_DRAKESCRAFT.md](docs/GUIA_DE_CRUCES_DRAKESCRAFT.md).

## 📜 Procedencia

Este es un fork mantenido por DrakesCraft Labs. Conserva la licencia GPL-3.0 y
los avisos de autoría originales requeridos, mientras que la operación, soporte
y documentación se gestionan desde este repositorio.

---

<div align="center">

**DrakesCraft Labs** · Mantenido por [**JackStar6677-1**](https://github.com/JackStar6677-1)

</div>

## Qué añade al juego

Flora, cruces y cultivos de Slimefun para DrakesCraft.


Todo se fabrica y se investiga desde la guía normal (`/sf guide`), como cualquier otro contenido
de Slimefun: no hace falta ningún comando especial para empezar.

## Compatibilidad

| | |
|---|---|
| Servidor | Paper / Purpur **1.21.11** |
| Java | **21** |
| Requiere | [Slimefun4-Drake](https://github.com/DrakesCraft-Labs/Slimefun4-Drake) |
| Lado | Solo servidor — quien juega no instala nada |
| Versión | ${project.version} |

## Instalación

1. Descarga el `.jar` de la última versión.
2. Déjalo en la carpeta `plugins/` del servidor, junto a Slimefun.
3. Reinicia el servidor. Los objetos aparecen solos en la guía.

> Este addon está portado al fork de Slimefun de DrakesCraft. Con el Slimefun original puede no
> cargar, porque cambia el espacio de nombres de las clases.

## Créditos
- Sefiraat
- J3fftw
- JustAHuman

Port y mantenimiento por **DrakesCraft Labs**. La autoría original es de quien figura arriba; el detalle está en [docs/UPSTREAM_ATTRIBUTION.md](https://raw.githubusercontent.com/DrakesCraft-Labs/Cultivation_Updated/main/docs/UPSTREAM_ATTRIBUTION.md).

Licencia **GPL-3.0-only**.
