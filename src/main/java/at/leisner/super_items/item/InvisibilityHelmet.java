package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class InvisibilityHelmet implements CustomItem {
    private final ItemStack item;
    private static HashMap<EquipmentSlot,ItemStack> equipmentSlotItemStackHashMap = new HashMap<>();
    private NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "invisibility_helmet");
    private ShapelessRecipe recipe;
    private final Super_Items plugin;

    static {
        equipmentSlotItemStackHashMap.put(EquipmentSlot.CHEST, new ItemStack(Material.AIR));
        equipmentSlotItemStackHashMap.put(EquipmentSlot.HEAD, new ItemStack(Material.AIR));
        equipmentSlotItemStackHashMap.put(EquipmentSlot.OFF_HAND, new ItemStack(Material.AIR));
        equipmentSlotItemStackHashMap.put(EquipmentSlot.LEGS, new ItemStack(Material.AIR));
        equipmentSlotItemStackHashMap.put(EquipmentSlot.FEET, new ItemStack(Material.AIR));
        equipmentSlotItemStackHashMap.put(EquipmentSlot.HAND, new ItemStack(Material.AIR));

    }

    public InvisibilityHelmet() {
        plugin = Super_Items.getInstance();
        item = new ItemStack(Material.DIAMOND_HELMET);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE,1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("INVISIBILITY HELMET").color(TextColor.color(0, 255, 255)));
        itemMeta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(itemMeta);

        recipe = new ShapelessRecipe(namespacedKey, item);
        recipe.addIngredient(Material.GOLDEN_CARROT);
        recipe.addIngredient(Material.DIAMOND_HELMET);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return recipe; // Optional: Rezept hinzufügen
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public NamespacedKey getNameKey() {
        return namespacedKey;
    }

    @Override
    public @NotNull String getName() {
        return namespacedKey.getKey();
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getHelmet() != null && ItemManager.isSame(player.getInventory().getHelmet(), this)) {
            if (event.isSneaking()) {
                // Mache den Spieler unsichtbar inklusive Rüstung und Item in der Hand.
                player.setInvisible(true);
                sendEquipmentChange(player, true);
            } else {
                // Mache den Spieler sichtbar.
                player.setInvisible(false);
                sendEquipmentChange(player, false);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        Player player = event.getPlayer();
        if (player.getInventory().getHelmet() != null && ItemManager.isSame(player.getInventory().getHelmet(), this)) {
            sendEquipmentChange(player, true);
            Bukkit.getScheduler().runTaskLater(plugin,() -> sendEquipmentChange(event.getPlayer(),true),1);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerChangedMainHandEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (event.getPlayer().getInventory().getHelmet() != null && ItemManager.isSame(event.getPlayer().getInventory().getHelmet(), this)) {
            sendEquipmentChange(event.getPlayer(), true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> sendEquipmentChange(event.getPlayer(), true), 1);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (event.getPlayer().getInventory().getHelmet() != null && ItemManager.isSame(event.getPlayer().getInventory().getHelmet(), this)) {
            sendEquipmentChange(event.getPlayer(), true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> sendEquipmentChange(event.getPlayer(), true), 1);
        }
    }

    public void sendEquipmentChange(Player player, boolean hide) {
        if (hide) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online == player) continue;
                online.sendEquipmentChange(player, equipmentSlotItemStackHashMap);
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online == player) continue;
                online.sendEquipmentChange(player,EquipmentSlot.CHEST, player.getInventory().getChestplate());
                online.sendEquipmentChange(player,EquipmentSlot.HEAD, player.getInventory().getHelmet());
                online.sendEquipmentChange(player,EquipmentSlot.HAND, player.getInventory().getItemInMainHand());
                online.sendEquipmentChange(player,EquipmentSlot.OFF_HAND, player.getInventory().getItemInOffHand());
                online.sendEquipmentChange(player,EquipmentSlot.LEGS, player.getInventory().getLeggings());
                online.sendEquipmentChange(player,EquipmentSlot.FEET, player.getInventory().getBoots());
            }
        }
    }
}
