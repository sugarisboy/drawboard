package ru.englishcraft.drawboard.board;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.config.Config;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardCreator {

    private int taskId;
    private LinkedList<Block> end;
    private Set<Block> all;

    private Material whiteboardBlock;
    private Player player;

    private BoardCreator(Player player) {
        this.player = player;
        this.end = new LinkedList<>();
        this.all = new HashSet<>();
    }

    // identify size board
    public BoardCreator(Block block, Player player) {
        this(player);

        Config config = DrawBoard.getInstance().config();
        whiteboardBlock = config.getWhiteboardBlock();

        Set<Block> nears = new HashSet<>();
        nears.add(block);

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DrawBoard.getInstance(), () -> {

            if (nears == null || nears.size() == 0)
                init();

            Set<Block> oldNears = new HashSet<>(nears);
            nears.clear();

            for (Block b : oldNears) {
                b.setType(Material.RED_WOOL);
                all.add(b);

                List<Block> near = nearBlocks(b);
                if (near.size() == 0) {
                    end.add(b);
                } else {
                    nears.addAll(near);
                }
            }

        }, 10L, 2L);
    }

    // init board by two point
    public BoardCreator(Block block1, Block block2, Player player) {
        this(player);
        this.end.add(block1);
        this.end.add(block2);

        Board board = init();
        if (board != null) {
            board.clear(block1, player);
        }

    }

    private List<Block> nearBlocks(Block block) {
        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        return Stream.of(
            world.getBlockAt(x + 1, y, z),
            world.getBlockAt(x, y + 1, z),
            world.getBlockAt(x, y, z + 1),
            world.getBlockAt(x - 1, y, z),
            world.getBlockAt(x, y - 1, z),
            world.getBlockAt(x, y, z - 1)
        )
            .filter(i -> i.getType().equals(whiteboardBlock))
            .collect(Collectors.toList());
    }

    private Board init() {
        Bukkit.getScheduler().cancelTask(taskId);
        for (Block b : all) {
            b.setType(whiteboardBlock);
        }

        Block block1 = end.get(0);
        Block block2 = null;
        for (Block b : end) {
            if (DrawBoard.getInstance().config()
                .getBoards().stream()
                .anyMatch(board -> board.isContains(b)))
            {
                player.sendMessage("§c[ERROR]: §eДанная доска пересекается с другой.");
                return null;
            }

            if (
                block1.getY() != b.getY() &&
                    (block1.getX() != b.getX() || block1.getZ() != b.getZ())) {
                block2 = b;
            }
        }

        if (block2 == null) {
            error();
        } else {
            return success(
                block1.getLocation(),
                block2.getLocation()
            );
        }
        return null;
    }

    private Board success(Location p1, Location p2) {
        Board board = new Board();
        board.setP1(p1);
        board.setP2(p2);
        board.initBoard();
        player.sendMessage("§2[DrawBoard]: §aДоска успешно создана, можно приступать к рисованию!");
        return board;
    }

    private void error() {
        player.sendMessage("§c[ERROR]: §eНеудалось создать доску для рисования.\n" +
            "Вероятнее всего, ошибка в том, что Вы кликнули инструментом создания на край доски.");
    }
}
