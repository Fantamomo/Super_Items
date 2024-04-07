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
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class SmeltingPickaxe implements CustomItem {
    private final ItemStack item;
    private final NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "smelting_pickaxe");
    private final ShapedRecipe recipe;

    public SmeltingPickaxe() {
        item = new ItemStack(Material.DIAMOND_PICKAXE);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE,1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("SMELTING PICKAXE").color(TextColor.color(255, 130, 0)));
        itemMeta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(itemMeta);

        recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape(
                " O ",
                "OPO",
                " O ");
        recipe.setIngredient('O', Material.FURNACE);
        recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
    }

    @Override
    public @Nullable org.bukkit.inventory.Recipe getRecipe() {
        // Hier könnte ein Rezept hinzugefügt werden, falls nötig.
        return null;
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
    private ItemStack getSmeltResult(ItemStack source) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();
            if (recipe instanceof FurnaceRecipe && ((FurnaceRecipe) recipe).getInputChoice().test(source)) {
                return recipe.getResult();
            }
        }
        return null;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (ItemManager.isSame(event.getPlayer().getInventory().getItemInMainHand(),this)) {
            Material blockType = event.getBlock().getType();
            ItemStack smeltedProduct = getSmeltResult(new ItemStack(blockType));
            if (smeltedProduct != null) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), smeltedProduct);
                event.setDropItems(false);
            }
        }
    }
}
