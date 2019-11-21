package ru.englishcraft.drawboard.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.board.BoardCreator;
import ru.englishcraft.drawboard.config.Config;


public class MainListener implements Listener {


    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            Player player = e.getPlayer().getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            Config config = DrawBoard.getInstance().config();
            if (
                config.getMagicWand().equals(itemInHand.getType()) &&
                    config.getWhiteboardBlock().equals(block.getType())
            ) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                new BoardCreator(block, player);
            }
        }
    }

}
