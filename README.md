# Cultivation Updated

Cultivation Updated es el addon de agricultura avanzada mantenido por
DrakesCraft Labs para el stack Slimefun Drake. Incorpora cultivos con genética,
árboles, arbustos, cocina y automatización sin migrar ni sustituir el progreso
existente de los jugadores.

## Runtime compatible

| Componente | Objetivo |
|---|---|
| Minecraft / Paper / Purpur | **1.21.11** |
| Java | **21** |
| Slimefun | **Slimefun Drake 11** |
| API de compilación | `paper-api 1.21.11-R0.1-SNAPSHOT` |

El addon requiere Slimefun Drake y no debe combinarse con una copia upstream o
con otro JAR de Cultivation en el mismo servidor.

## Contenido

- Cultivos con cría, rasgos de crecimiento, rendimiento y resistencia.
- Árboles y arbustos con recursos para la cadena de cocina.
- Garden Cloche y rutas de automatización compatibles con el stack instalado.
- Máquinas culinarias, recetas y efectos de comida.
- Descubrimiento progresivo y pistas de combinaciones.

El contenido es balanceable mediante su configuración. Cualquier cambio de
ritmo, producción o receta debe probarse con datos existentes antes de afectar
la economía de un mundo activo.

## Trabajo Drake

- Port compilable en Java 21 y Paper 1.21.11.
- Dependencias declaradas contra el core Slimefun Drake.
- Sin actualizador remoto ni reemplazo automático de binarios.
- Compatibilidad preservada para IDs, datos de plantas, bloques y recetas.
- Preparado para validación de inventarios y bloques existentes antes de cada
  despliegue.

## Actualización segura

1. Respalda `plugins/Cultivation v2.5.jar` y `plugins/Cultivation/`.
2. Construye el candidato y registra su checksum.
3. En staging, valida un cultivo legacy, una Cloche y una receta de cocina.
4. Reemplaza el JAR solo en una ventana de reinicio y conserva el anterior para
   rollback.

## Desarrollo

```bash
mvn -B -ntp clean verify
```

El JAR queda en `target/`.

## Procedencia

Proyecto original de Sefiraat y colaboradores. Este repositorio mantiene el
port de DrakesCraft Labs y conserva los créditos del proyecto original.
