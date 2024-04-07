package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class WindFeather implements CustomItem {
    private final ItemStack item;
    private final NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "wind_feather");
    private ShapelessRecipe recipe;

    public WindFeather() {
        item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Wind FEATHER").color(TextColor.color(255, 0, 255)));
        meta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(namespacedKey, item);
        recipe.addIngredient(3, Material.FEATHER);
        recipe.addIngredient(Material.ENDER_PEARL);
    }

    @Override
    public Recipe getRecipe() {
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
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (ItemManager.isSame(event.getItem(), this) && player.getCooldown(Material.FEATHER) == 0) {
            // Example action: Boost the player upwards
            player.setVelocity(player.getVelocity().add(new Vector(0, 5, 0)));
            // Optional: Implement damage based on falling below the start height.
            if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) player.setCooldown(Material.FEATHER,80);
        }
    }
}
