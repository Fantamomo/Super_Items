package at.leisner.super_items;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomItem extends Listener {
    public @Nullable Recipe getRecipe();
    public @NotNull ItemStack getItem();
    public NamespacedKey getNameKey();
    public @NotNull String getName();
}
