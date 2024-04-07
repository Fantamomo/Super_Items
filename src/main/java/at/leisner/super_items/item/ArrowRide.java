package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArrowRide implements CustomItem {
    private final ItemStack item;
    private final NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "arrow_ride");;
    private ShapelessRecipe recipe;

    public ArrowRide() {
        item = new ItemStack(Material.SPECTRAL_ARROW);
        item.addUnsafeEnchantment(Enchantment.LUCK, 1); // Mark the item uniquely.
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Rideable Arrow").color(TextColor.color(255,255,0)));
        meta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(namespacedKey, item);
        recipe.addIngredient(Material.ARROW);
        recipe.addIngredient(Material.SADDLE);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return recipe;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item.clone();
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (ItemManager.isSame(event.getItem(), this) && event.getPlayer().getCooldown(Material.SPECTRAL_ARROW) == 0) {
            Player player = event.getPlayer();
            Arrow arrow = player.launchProjectile(Arrow.class);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
            arrow.addPassenger(player);
            // Note: The physics and behavior might need adjustment.
            if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) player.setCooldown(Material.SPECTRAL_ARROW,50);

        }
    }
}
