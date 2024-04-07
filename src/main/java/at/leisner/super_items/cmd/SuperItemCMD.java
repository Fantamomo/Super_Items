package at.leisner.super_items.cmd;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SuperItemCMD extends Command {
    private Super_Items plugin;

    public SuperItemCMD() {
        super("superitem");
        plugin = Super_Items.getInstance();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /superitem <item> [player] [count]");
            return true;
        }

        // Überprüfung, ob das Item existiert
        List<CustomItem> gefilterteListe = plugin.getItemManager().getCustomItems().stream()
                .filter(objekt -> objekt.getName().equalsIgnoreCase(args[0]))
                .toList();

        if (gefilterteListe.isEmpty()) {
            commandSender.sendMessage(ChatColor.RED + "Das Item existiert nicht.");
            return true;
        }

        CustomItem customItem = gefilterteListe.get(0);

        // Item Anzahl
        int count = 1;
        if (args.length >= 3) {
            try {
                count = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(ChatColor.RED + "Die Anzahl muss eine Zahl sein.");
                return true;
            }
        }

        // Bestimmen des Empfängers
        List<Player> targets = new ArrayList<>();
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("@a")) {
                targets.addAll(Bukkit.getOnlinePlayers());
            } else if (args[1].equalsIgnoreCase("@s")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(ChatColor.RED + "Nur Spieler können @s verwenden.");
                    return true;
                }
                targets.add((Player) commandSender);
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    commandSender.sendMessage(ChatColor.RED + "Spieler nicht gefunden.");
                    return true;
                }
                targets.add(target);
            }
        } else if (commandSender instanceof Player) {
            targets.add((Player) commandSender);
        } else {
            commandSender.sendMessage(ChatColor.RED + "Wenn der Befehl nicht von einem Spieler ausgeführt wird, muss ein Spieler angegeben werden.");
            return true;
        }

        // Item an Spieler verteilen
        ItemStack itemStack = customItem.getItem();
        itemStack.setAmount(count);
        for (Player target : targets) {
            target.getInventory().addItem(itemStack);
            target.sendMessage(ChatColor.GREEN + "Du hast " + count + "x " + customItem.getName() + " erhalten.");
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return plugin.getItemManager().getCustomItems().stream()
                    .map(CustomItem::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            List<String> suggestions = new ArrayList<>(Arrays.asList("@a", "@s"));
            if (sender instanceof Player player) {
                suggestions.addAll(plugin.getApiPlugin().getVanishManager().listOfSeenPlayerFrom(player).stream()
                        .map(Player::getName)
                        .toList());
            } else {
                suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toList());
            }
            return suggestions;
        }
        return new ArrayList<>();
    }
}
