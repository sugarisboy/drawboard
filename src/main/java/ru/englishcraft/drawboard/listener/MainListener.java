package ru.englishcraft.drawboard.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.board.BoardCreator;
import ru.englishcraft.drawboard.config.Config;

public class MainListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer().getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Material type = itemInHand.getType();
        Config config = DrawBoard.getInstance().config();

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (config.getMagicWand().equals(type)) {
                Block block = player.getTargetBlock(null, 100);
                config.getBoards().forEach(board -> board.draw(block, player));
            } else if (config.getClearWand().equals(type)) {
                Block block = player.getTargetBlock(null, 100);
                config.getBoards().forEach(board -> board.clear(block, player));
            } else if (config.getCancelWand().equals(type)) {
                Block block = player.getTargetBlock(null, 100);
                config.getBoards().forEach(board -> board.cancel(block, player));
            }
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();

            if (
                config.getMagicWand().equals(type) &&
                    config.getWhiteboardBlock().equals(block.getType())
            ) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                new BoardCreator(block, player);
            }
        }
    }

}
