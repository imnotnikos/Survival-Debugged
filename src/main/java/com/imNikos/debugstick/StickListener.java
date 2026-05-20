package com.imNikos.debugstick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class StickListener implements Listener {

    private final NamespacedKey stickTag;
    private final Map<UUID, Map<Material, String>> playerModes = new HashMap<>();

    // The Master Dictionary: Contains all possible string values from your list
    private final List<String> stateDatabase = Arrays.asList(
            // Directions & Axes
            "north", "east", "south", "west", "up", "down", "x", "y", "z",
            // Halves, Positions & Sizes
            "upper", "lower", "top", "bottom", "double", "single", "head", "foot",
            "none", "small", "large", "thin", "thick", "short", "tall", "low",
            "tip", "frustum", "middle", "base", "merge",
            // Shapes & Attachments
            "straight", "inner_left", "inner_right", "outer_left", "outer_right", "left", "right",
            "north_south", "east_west", "ascending_east", "ascending_north", "ascending_south", "ascending_west",
            "north_east", "north_west", "south_east", "south_west",
            "floor", "wall", "ceiling", "single_wall", "double_wall", "hanging", "side",
            // Modes & Types
            "compare", "subtract", "normal", "sticky", "corner", "data", "load", "save",
            "active", "inactive", "cooldown", "partial", "unstable", "full",
            // Instruments
            "harp", "banjo", "basedrum", "bass", "bell", "bit", "chime", "cow_bell", "creeper", 
            "custom_head", "didgeridoo", "dragon", "flute", "guitar", "hat", "iron_xylophone", 
            "piglin", "pling", "skeleton", "snare", "wither_skeleton", "xylophone", "zombie"
    );

    public StickListener(SurvivalDebugStickPlugin plugin) {
        this.stickTag = new NamespacedKey(plugin, "custom_debug_stick");
    }

    private Map<String, String> getBlockStates(BlockData data) {
        Map<String, String> states = new LinkedHashMap<>();
        String str = data.getAsString();
        if (!str.contains("[")) return states; 
        
        String stateStr = str.substring(str.indexOf('[') + 1, str.indexOf(']'));
        for (String pair : stateStr.split(",")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) states.put(parts[0], parts[1]);
        }
        return states;
    }

    @EventHandler
    public void onStickUse(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(stickTag, PersistentDataType.BYTE)) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        BlockData data = clickedBlock.getBlockData();
        Material mat = clickedBlock.getType();
        
        Map<String, String> states = getBlockStates(data);
        if (states.isEmpty()) {
            player.sendActionBar(Component.text("This block has no block states.", NamedTextColor.RED));
            return;
        }

        List<String> keys = new ArrayList<>(states.keySet());
        Map<Material, String> pModes = playerModes.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        String currentProp = pModes.getOrDefault(mat, keys.get(0));

        if (!keys.contains(currentProp)) {
            currentProp = keys.get(0);
            pModes.put(mat, currentProp);
        }

        String currentValue = states.get(currentProp);

        // LEFT CLICK: Swap Block State
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            int currentIndex = keys.indexOf(currentProp);
            String nextProp = keys.get((currentIndex + 1) % keys.size());
            
            pModes.put(mat, nextProp);
            String nextValue = states.get(nextProp);
            player.sendActionBar(Component.text("Selected: " + nextProp + " (" + nextValue + ")", NamedTextColor.YELLOW));
        }
        
        // RIGHT CLICK: Toggle Value
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String newValue = getNextValidValue(data, currentProp, currentValue);
            
            if (!newValue.equals(currentValue)) {
                String baseDataStr = data.getAsString();
                String targetStr = currentProp + "=" + currentValue;
                String replacementStr = currentProp + "=" + newValue;
                String newDataStr = baseDataStr.replace(targetStr, replacementStr);
                
                try {
                    BlockData newData = Bukkit.createBlockData(newDataStr);
                    // 'false' flag prevents block physics and neighbor updates (preserves door halves!)
                    clickedBlock.setBlockData(newData, false); 
                    player.sendActionBar(Component.text(currentProp + " -> " + newValue, NamedTextColor.GREEN));
                } catch (Exception ignored) { }
            }
        }
    }

    private String getNextValidValue(BlockData baseData, String key, String currentVal) {
        String baseStr = baseData.getAsString();

        // 1. Booleans
        if (currentVal.equals("true")) return "false";
        if (currentVal.equals("false")) return "true";

        // 2. Integers (e.g., ages, rotations)
        if (currentVal.matches("\\d+")) {
            int val = Integer.parseInt(currentVal);
            for (int i = 1; i <= 30; i++) {
                if (isValidState(baseStr, key, currentVal, String.valueOf(val + i))) return String.valueOf(val + i);
            }
            for (int i = 0; i <= val; i++) {
                if (isValidState(baseStr, key, currentVal, String.valueOf(i))) return String.valueOf(i);
            }
        }

        // 3. Strings (Cycles through our Master Dictionary)
        if (stateDatabase.contains(currentVal)) {
            int idx = stateDatabase.indexOf(currentVal);
            for (int i = 1; i <= stateDatabase.size(); i++) {
                int nextIdx = (idx + i) % stateDatabase.size();
                String testVal = stateDatabase.get(nextIdx);
                if (isValidState(baseStr, key, currentVal, testVal)) return testVal;
            }
        }
        return currentVal;
    }

    private boolean isValidState(String baseStr, String key, String oldVal, String newVal) {
        String testStr = baseStr.replace(key + "=" + oldVal, key + "=" + newVal);
        try {
            Bukkit.createBlockData(testStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}