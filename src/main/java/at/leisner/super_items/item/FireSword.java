package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class FireSword implements CustomItem {
    private final ItemStack item;
    private final NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "fire_sword");;
    private final ShapedRecipe recipe;

    public FireSword() {
        item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.displayName(Component.text("FIRE SWORD").color(TextColor.color(255, 0, 0)));
        meta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(meta);

        recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape(" F ", " F ", " S ");
        recipe.setIngredient('F', Material.BLAZE_POWDER);
        recipe.setIngredient('S', Material.STICK);
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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Optional: Add any specific behavior when an entity is damaged with this sword.
    }
}
