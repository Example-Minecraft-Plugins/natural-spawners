package me.davipccunha.naturalspawners.command;

import me.davipccunha.utils.entity.EntityName;
import me.davipccunha.utils.inventory.InventoryUtil;
import me.davipccunha.utils.item.NBTHandler;
import me.davipccunha.utils.messages.ErrorMessages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class SpawnerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("spawners.admin.give")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return false;
        }

        if (args.length != 4 || !args[0].equalsIgnoreCase("give"))
            return invalidate(sender, "§cUso: /spawner give <nick> <tipo> <quantidade>");

        final String name = args[1];
        final Player player = Bukkit.getPlayer(name);
        if (player == null)
            return invalidate(sender, ErrorMessages.PLAYER_NOT_FOUND.getMessage());

        final String type = args[2].toUpperCase();

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return invalidate(sender, "§cTipo de entidade inválido.");
        }

        int amount = NumberUtils.toInt(args[3]);
        if (amount <= 0)
            return invalidate(sender, ErrorMessages.INVALID_AMOUNT.getMessage());
        if (amount > 64)
            return invalidate(sender, "§cA quantidade máxima é 64.");

        final ItemStack spawner = this.createSpawner(entityType.toString(), amount);
        int missingAmount = InventoryUtil.getMissingAmount(player.getInventory(), spawner);
        if (missingAmount < amount)
            return invalidate(sender, ErrorMessages.INVENTORY_FULL.getMessage());

        player.getInventory().addItem(spawner);

        sender.sendMessage(String.format("§aSpawner de §f%s (x%d) §adado com sucesso para §f%s§a.", EntityName.valueOf(type), amount, player.getName()));
        return true;
    }

    private boolean invalidate(CommandSender player, String message) {
        player.sendMessage(message);
        return false;
    }

    private ItemStack createSpawner(String type, int amount) {
        ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, amount);
        ItemMeta spawnerMeta = spawner.getItemMeta();
        spawnerMeta.setDisplayName("§fSpawner de " + EntityName.valueOf(type));
        spawner.setItemMeta(spawnerMeta);

        return NBTHandler.addNBT(spawner, Map.of("entity", type));
    }
}
