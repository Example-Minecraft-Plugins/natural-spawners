package me.davipccunha.naturalspawners.listener;

import me.davipccunha.utils.entity.EntityName;
import me.davipccunha.utils.item.NBTHandler;
import org.bukkit.GameMode;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof CreatureSpawner)) return;

        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();

        if (!itemInHand.getType().toString().contains("PICKAXE") && !player.hasPermission("spawners.admin.break")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cVocê precisa de uma picareta para quebrar spawners.");
            return;
        }

        final ItemStack spawner = event.getBlock().getState().getData().toItemStack(1);

        final String entity = ((CreatureSpawner) event.getBlock().getState()).getSpawnedType().toString();

        final Map<String, String> tags = Map.of("entity", entity);

        ItemStack spawnerNBT = NBTHandler.addNBT(spawner, tags);
        ItemMeta spawnerMeta = spawnerNBT.getItemMeta();
        spawnerMeta.setDisplayName("§fSpawner de " + EntityName.valueOf(entity));
        spawnerNBT.setItemMeta(spawnerMeta);

        event.setExpToDrop(0);

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            final HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(spawnerNBT);
            if (!remaining.isEmpty()) {
                for (ItemStack item : remaining.values())
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
            }
        }
    }
}
