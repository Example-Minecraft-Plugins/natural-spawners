package me.davipccunha.naturalspawners.listener;

import me.davipccunha.utils.entity.EntityName;
import me.davipccunha.utils.item.NBTHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof CreatureSpawner)) return;

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand.getType() != Material.DIAMOND_PICKAXE && !player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cVocê precisa de uma picareta de diamante para quebrar spawners.");
            return;
        }

        ItemStack spawner = event.getBlock().getState().getData().toItemStack(1);

        String entity = ((CreatureSpawner) event.getBlock().getState()).getSpawnedType().toString();

        final Map<String, String> tags = new HashMap<>() {{
            put("entity", entity);
        }};

        ItemStack spawnerNBT = NBTHandler.addNBT(spawner, tags);
        ItemMeta spawnerMeta = spawnerNBT.getItemMeta();
        spawnerMeta.setDisplayName("§fSpawner de " + EntityName.valueOf(entity));
        spawnerNBT.setItemMeta(spawnerMeta);

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setExpToDrop(0);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawnerNBT);
        }
    }
}
