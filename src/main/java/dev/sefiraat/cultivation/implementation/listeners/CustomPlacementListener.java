package dev.sefiraat.cultivation.implementation.listeners;

import dev.sefiraat.cultivation.Cultivation;
import dev.sefiraat.cultivation.api.interfaces.CustomPlacementBlock;
import dev.sefiraat.cultivation.api.slimefun.items.bushes.CultivationBush;
import dev.sefiraat.cultivation.api.slimefun.items.plants.CultivationPlant;
import dev.drake.sefilib.entity.display.DisplayGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The purpose of this listener is to allow us to cancel the block placement if not on the
 * correct material. If done within the onBlockPlace handler, the BlockStorage is retained
 * leading to dupes.
 * Also allows other objects implementing {@link CustomPlacementBlock}
 * to fire their own methods
 * TODO PR to slimefun to either do blockstorage after checking the event is cancelled or to remove
 */
public class CustomPlacementListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
        SlimefunItem slimefunItem = SlimefunItem.getByItem(event.getItemInHand());
        if (slimefunItem instanceof CustomPlacementBlock block) {
            block.whenPlaced(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWaterHitsPlant(@Nonnull BlockFromToEvent event) {
        Location location = event.getToBlock().getLocation();
        unsafelyKillItem(location, BlockStorage.check(location));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtends(@Nonnull BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Block issueBlock = block.getRelative(BlockFace.UP);
            Location location = issueBlock.getLocation();
            unsafelyKillItem(location, BlockStorage.check(location));
        }
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetracts(@Nonnull BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            Block issueBlock = block.getRelative(BlockFace.UP);
            Location location = issueBlock.getLocation();
            unsafelyKillItem(location, BlockStorage.check(location));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockSpread(@Nonnull BlockSpreadEvent event) {
        Location location = event.getBlock().getLocation();
        unsafelyKillItem(location, BlockStorage.check(location));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBoneMeal(@Nonnull BlockFertilizeEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            Block issueBlock = blockState.getBlock();
            Location location = issueBlock.getLocation();
            unsafelyKillItem(location, BlockStorage.check(location));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSnowmanBlockForm(@Nonnull EntityBlockFormEvent event) {
        Location location = event.getBlock().getLocation();
        unsafelyKillItem(location, BlockStorage.check(location));
    }

    /**
     * Plants/Bushes are represented by player head / display entities. Breaking the supporting
     * block bypasses Slimefun's normal break handler, so remove the captured displays once
     * Minecraft has resolved the physics update instead of leaving an untouchable ghost.
     * Also handles AIR-display plants where BlockPhysics may not clear BlockStorage.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlantPhysics(@Nonnull BlockPhysicsEvent event) {
        Block block = event.getBlock();
        SlimefunItem slimefunItem = BlockStorage.check(block);
        if (!(slimefunItem instanceof CultivationPlant) && !(slimefunItem instanceof CultivationBush)) {
            return;
        }
        if (block.getRelative(BlockFace.DOWN).getType().isSolid()) {
            return;
        }

        Location location = block.getLocation();
        SlimefunItem capturedItem = slimefunItem;
        // Capturar grupos antes del tick siguiente (pueden perderse al limpiar storage)
        DisplayGroup plantDisplay = null;
        DisplayGroup cropDisplay = null;
        DisplayGroup bushDisplay = null;
        if (capturedItem instanceof CultivationPlant plant) {
            plantDisplay = plant.getPlantDisplayGroup(location);
            cropDisplay = plant.getCropDisplayGroup(location);
        } else if (capturedItem instanceof CultivationBush bush) {
            bushDisplay = bush.getBushDisplayGroup(location);
        }

        DisplayGroup finalPlantDisplay = plantDisplay;
        DisplayGroup finalCropDisplay = cropDisplay;
        DisplayGroup finalBushDisplay = bushDisplay;
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> {
            // Si el soporte sigue sólido alguien lo repuso en el mismo tick
            if (location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                return;
            }
            SlimefunItem still = BlockStorage.check(location);
            if (still != null) {
                // Forzar ruptura completa con drop de semilla (comportamiento esperado al quitar bloque de abajo)
                unsafelyKillItem(location, still);
                return;
            }
            // Limpieza de ghosts huérfanos donde BlockStorage ya fue borrado pero quedan displays
            if (finalPlantDisplay != null) {
                finalPlantDisplay.remove();
            }
            if (finalCropDisplay != null) {
                finalCropDisplay.remove();
            }
            if (finalBushDisplay != null) {
                finalBushDisplay.remove();
            }
            if (BlockStorage.hasBlockInfo(location.getBlock())) {
                BlockStorage.clearBlockInfo(location);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSupportBreak(@Nonnull org.bukkit.event.block.BlockBreakEvent event) {
        Block broken = event.getBlock();
        // El bloque de arriba puede ser planta (AIR-display o PLAYER_HEAD) o bush
        Block above = broken.getRelative(BlockFace.UP);
        SlimefunItem aboveItem = BlockStorage.check(above);
        if (aboveItem == null) {
            return;
        }
        // Para PLAYER_HEAD Slimefun ya dispara SENSITIVE_MATERIALS, pero para AIR-display no.
        // Forzamos limpieza en ambos casos para asegurar drop de semilla y borrado de displays.
        Location loc = above.getLocation();
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> {
            SlimefunItem still = BlockStorage.check(loc);
            if (still != null) {
                unsafelyKillItem(loc, still);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLiquidDispense(@Nonnull BlockDispenseEvent event) {
        Block block = event.getBlock();
        if (isLiquid(event.getItem())
            && block.getBlockData() instanceof Directional directional
        ) {
            BlockFace face = directional.getFacing();
            Location location = block.getRelative(face).getLocation();
            unsafelyKillItem(location, BlockStorage.check(location));
        }
    }

    private void unsafelyKillItem(@Nonnull Location location, @Nullable SlimefunItem slimefunItem) {
        if (slimefunItem instanceof CultivationPlant plant) {
            location.getWorld().dropItem(location, plant.getDroppedItemStack(location));
            plant.removeCropped(location);
            plant.removePlantDisplayGroup(location);
            plant.removeLevelProfile(location);
            plant.removeOwner(location);
            BlockStorage.clearBlockInfo(location);
            location.getBlock().setType(Material.AIR);
        } else if (slimefunItem instanceof CultivationBush bush) {
            location.getWorld().dropItem(location, bush.getItem().clone());
            bush.removeBushDisplayGroup(location);
            bush.removeOwner(location);
            BlockStorage.clearBlockInfo(location);
        }
    }

    private boolean isLiquid(@Nonnull ItemStack itemStack) {
        Material material = itemStack.getType();
        return material == Material.WATER_BUCKET
            || material == Material.LAVA_BUCKET
            || material == Material.POWDER_SNOW_BUCKET;
    }
}
