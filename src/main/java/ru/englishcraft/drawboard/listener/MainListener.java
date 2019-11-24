package ru.englishcraft.drawboard.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.board.Board;
import ru.englishcraft.drawboard.board.BoardCreator;
import ru.englishcraft.drawboard.board.DrawColor;
import ru.englishcraft.drawboard.config.Config;
import ru.englishcraft.drawboard.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainListener implements Listener {

    private Map<Inventory, Board> inventoryBoardMap = new HashMap<>();

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
                player.hasPermission("drawboard.admin") &&
                    config.getMagicWand().equals(type) &&
                    config.getWhiteboardBlock().equals(block.getType())
            ) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                new BoardCreator(block, player);
            }
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Block block = player.getTargetBlock(null, 100);
            Optional<Board> first = config.getBoards().stream()
                .filter(board -> board.isContains(block))
                .findFirst();
            if (!first.isPresent())
                return;

            Board board = first.get();
            Inventory inventory = PlayerUtils.convertColorToInv();
            inventoryBoardMap.put(inventory, board);
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void onCliCkEvent(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if (inventoryBoardMap.containsKey(inventory)) {
            ItemStack item = e.getCurrentItem();
            if (item == null)
                return;
            e.setCancelled(true);

            Material type = item.getType();
            DrawColor drawColor = DrawColor.valueOf(type);
            if (drawColor != null) {
                Player player = (Player) e.getWhoClicked();
                Board board = inventoryBoardMap.get(inventory);
                board.changeColor(player, drawColor);
                player.closeInventory();
                player.sendMessage("§2[DrawBoard]: §aЦвет успешно изменен на " + drawColor.getDisplayName() + "!");
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        inventoryBoardMap.remove(e.getInventory());
    }
}
