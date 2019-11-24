package ru.englishcraft.drawboard.cmd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.board.BoardCreator;
import ru.englishcraft.drawboard.utils.PlayerUtils;

public class BoardCommand implements CommandExecutor {

    private final int MAX_WIDTH = 150;
    private final int MAX_HEIGHT = 100;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!player.hasPermission("drawboard.admin")) {
                player.sendMessage(ChatColor.RED + "Недостаточно прав");
                return true;
            }

            if (args.length == 3) {
                // /board gen <width> <height>
                if (args[0].equalsIgnoreCase("gen")) {
                    int width, height;

                    try {
                        width = Integer.valueOf(args[1]);
                        height = Integer.valueOf(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Ширина и высота должны быть числом!");
                        return true;
                    }

                    if (height > MAX_HEIGHT) {
                        player.sendMessage(ChatColor.RED + "Максимальная высота: " + MAX_HEIGHT + " блоков");
                        return true;
                    }
                    if (width > MAX_WIDTH) {
                        player.sendMessage(ChatColor.RED + "Максимальная ширина: " + MAX_WIDTH + " блоков");
                        return true;
                    }

                    Location location = player.getLocation();
                    Vector v = PlayerUtils.toDirection(location.getYaw());
                    Block block1 = location.getBlock();
                    Block block2 = location.getWorld().getBlockAt(
                        (int) (location.getBlockX() + width * v.getX()),
                        location.getBlockY() + height,
                        (int) (location.getBlockZ() + width * v.getZ())
                    );

                    new BoardCreator(block1, block2, player);
                    return true;
                }

            } else if (args.length == 1) {
                // /board delete
                if (args[0].equalsIgnoreCase("delete")) {
                    Block block = player.getTargetBlock(null, 100);
                    try {
                        DrawBoard.getInstance().config().getBoards()
                            .stream()
                            .filter(board -> board.isContains(block))
                            .forEach(board -> board.delete(player));
                    } catch (Exception ignored) {
                    }
                    return true;
                }
            }

            player.sendMessage(ChatColor.YELLOW + " /board gen <width> <height> - Сгенерирует доску");
            player.sendMessage(ChatColor.YELLOW + " /board delete - Удалит доску, на которую вы смотрите ");

        } else {
            commandSender.sendMessage("Command only for players!");
        }
        return true;
    }
}
