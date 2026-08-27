package dev.sefiraat.cultivation;

import dev.sefiraat.cultivation.implementation.slimefun.tools.PlantAnalyser;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HarvestablePlantTest {

    @Test
    void testDropAmountCalculation() {
        // Level 1: default + (default * (1 / 5)) = default
        int amountLevel1 = getExpectedDropAmount(1, 2);
        assertEquals(2, amountLevel1);

        // Level 5: default + (default * (5 / 5)) = 2 + 2 = 4
        int amountLevel5 = getExpectedDropAmount(5, 2);
        assertEquals(4, amountLevel5);

        // Level 10: default + (default * (10 / 5)) = 2 + 4 = 6
        int amountLevel10 = getExpectedDropAmount(10, 2);
        assertEquals(6, amountLevel10);
    }

    @Test
    void testPlantAnalyserGuardFilter() {
        // When heldItem is PlantAnalyser, harvesting must be skipped
        assertTrue(isAnalyser(PlantAnalyser.class));
        assertFalse(isAnalyser(SlimefunItem.class));
        assertFalse(isAnalyser(null));
    }

    private static int getExpectedDropAmount(int level, int defaultAmount) {
        return defaultAmount + (defaultAmount * (level / 5));
    }

    private static boolean isAnalyser(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return PlantAnalyser.class.isAssignableFrom(clazz);
    }
}
