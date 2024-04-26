package me.davipccunha.naturalspawners.listener;

import me.davipccunha.utils.item.NBTHandler;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(event.getBlock().getState() instanceof CreatureSpawner)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        ItemStack spawner = event.getItemInHand().clone();

        String entityName = NBTHandler.getNBT(spawner, "entity");

        if (entityName == null) {
            player.sendMessage("§cOcorreu um erro. Contate nossa equipe de suporte.");
            event.setCancelled(true);
            return;
        }

        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();

        creatureSpawner.setSpawnedType(EntityType.valueOf(entityName));
        creatureSpawner.update();
    }
}
