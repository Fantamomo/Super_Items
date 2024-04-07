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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class NoTargetHelmet implements CustomItem {
    private HashMap<Player, Integer> damageCooldown = new HashMap<>();
    private ItemStack item;
    private ShapedRecipe recipe;
    private NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "no_target_helmet");

    public NoTargetHelmet() {
        item = new ItemStack(Material.DIAMOND_HELMET);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("NO TARGET HELMET").color(TextColor.color(0, 255, 255)));
        itemMeta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(itemMeta);

        recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape(
                "DDD",
                "DHD",
                "DDD");
        recipe.setIngredient('H',Material.DIAMOND_HELMET);
        recipe.setIngredient('D', Material.DIAMOND);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return recipe;
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
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player p)) return;
        if (p.getInventory().getHelmet() == null) return;
        if (!ItemManager.isSame(p.getInventory().getHelmet(),this)) return;
        event.setCancelled(true);
        if (p.getGameMode() != GameMode.CREATIVE) {
            if (!damageCooldown.containsKey(p)) damageCooldown.put(p, 20);
            damageCooldown.put(p,damageCooldown.get(p)-1);
            if (damageCooldown.get(p) < 0) p.getInventory().getHelmet().damage(1, (LivingEntity) event.getEntity());
        }
    }
}
