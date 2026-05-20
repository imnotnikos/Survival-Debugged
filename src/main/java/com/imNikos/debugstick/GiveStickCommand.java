package com.imNikos.debugstick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GiveStickCommand implements CommandExecutor {

    private final SurvivalDebugStickPlugin plugin;

    public GiveStickCommand(SurvivalDebugStickPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("customdebugstick.give")) {
            player.sendMessage(Component.text("You lack permission to spawn this item.", NamedTextColor.RED));
            return true;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();

        meta.displayName(Component.text("Survival Debug Stick", NamedTextColor.LIGHT_PURPLE));
        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey key = new NamespacedKey(plugin, "custom_debug_stick");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        stick.setItemMeta(meta);
        player.getInventory().addItem(stick);
        player.sendMessage(Component.text("Debug stick granted!", NamedTextColor.GREEN));

        return true;
    }
}