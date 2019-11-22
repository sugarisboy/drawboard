package ru.englishcraft.drawboard.board;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Board {

    @Expose
    @SerializedName("position1")
    private Location p1;

    @Expose
    @SerializedName("position2")
    private Location p2;

    private transient Map<Player, List<DrawAction>> actions = new HashMap<>();

    private static transient final double K = 2D;
    private static transient final long DROP_LINE_TIME = 175L;
    private static transient final Material COLOR = Material.BLACK_WOOL;
    private static transient final int DEPTH_ACTION_STORY = 30;

    public boolean isContains(Block block) {
        Location location = block.getLocation();
        if (p1 == null || p2 == null)
            return false;

        if (!p1.getWorld().equals(p2.getWorld()))
            return false;

        if (!location.getWorld().equals(p1.getWorld()))
            return false;

        return
            between(p1.getBlockX(), p2.getBlockX(), location.getBlockX()) &&
            between(p1.getBlockY(), p2.getBlockY(), location.getBlockY()) &&
            between(p1.getBlockZ(), p2.getBlockZ(), location.getBlockZ());
    }

    // A <= C <= B or B <= C <= A
    private boolean between(int a, int b, int c) {
        return a <= c && c <= b || b <= c && c <= a;
    }

    public void draw(Block block, Player player) {
        if (!isContains(block))
            return;

        DrawAction action = new DrawAction(player, block);
        DrawAction prev = getLastAction(player);
        addAction(action);

        if (prev == null) {
            return;
        }

        // DRAW
        long time = action.getTime() - prev.getTime();
        if (time < DROP_LINE_TIME + 50L && time > DROP_LINE_TIME) {
            drawLine(action, prev);
        } else {
            action.drawBlock(block, COLOR);
        }
    }

    private void drawLine(DrawAction action, DrawAction prev) {
        Block b1 = action.getMainBlock();
        Block b2 = prev.getMainBlock();

        double modifer = b1.getLocation().distanceSquared(b2.getLocation()) * K;
        Vector vector = new Vector(
            (b2.getX() - b1.getX()) / modifer,
            (b2.getY() - b1.getY()) / modifer,
            (b2.getZ() - b1.getZ()) / modifer
        );

        Block b;
        Location l = b1.getLocation().clone();

        for (int i = 0; i < modifer; i++) {
            l = l.add(vector);
            b = l.getBlock();
            action.drawBlock(b, COLOR);
        }
    }

    private DrawAction getLastAction(Player player) {
        if (!actions.containsKey(player)) {
            actions.put(player, new ArrayList<>());
            return null;
        } else {
            List<DrawAction> actions = this.actions.get(player);
            return actions.size() != 0 ? actions.get(actions.size() - 1) : null;
        }
    }

    private void addAction(DrawAction action) {
        List<DrawAction> playerActions = actions.get(action.getPlayer());
        playerActions.add(action);
        if (playerActions.size() > DEPTH_ACTION_STORY) {
            playerActions.remove(0);
        }
    }

    private void dropAction(DrawAction action) {
        actions.get(action.getPlayer()).remove(action);
    }

    public Board create() {
        Config config = DrawBoard.getInstance().config();
        List<Board> boards = config.getBoards();
        boards.add(this);
        config.save();

        return this;
    }

    public void delete() {
        Config config = DrawBoard.getInstance().config();
        List<Board> boards = config.getBoards();
        boards.remove(this);
        config.save();
    }

    public void cancel(Block block, Player player) {
        if (!isContains(block))
            return;

        DrawAction lastAction = getLastAction(player);
        if (lastAction == null) {
            player.sendMessage("§c[ERROR]: §eБольше нечего отменять.");
            return;
        }

        lastAction.cancel();
        dropAction(lastAction);
    }

    public void clear(Block block, Player player) {
        if (!isContains(block))
            return;

        DrawAction action = new DrawAction(player, block);
        action.setType(DrawActionType.CLEAR);

        Vector subtract = p2.toVector().subtract(p1.toVector());
        Vector normalize = subtract.clone().divide(
            new Vector(
                Math.abs(subtract.getX() + .0000000000000000000000000000000001D),
                Math.abs(subtract.getY()),
                Math.abs(subtract.getZ() + .0000000000000000000000000000000001D)
            )
        );

        World world = p1.getWorld();
        Material whiteboard = DrawBoard.getInstance().config().getWhiteboardBlock();
        for (int offsetY = 0; offsetY <= Math.abs(subtract.getY()); offsetY++) {
            for (int offsetXZ = 0; offsetXZ <= Math.abs(subtract.getX()) + Math.abs(subtract.getZ()); offsetXZ++) {
                Block b = world.getBlockAt(
                    (int) (p1.getX() + offsetXZ * normalize.getX()),
                    (int) (p1.getY() + offsetY * normalize.getY()),
                    (int) (p1.getZ() + offsetXZ * normalize.getZ())
                );
                action.drawBlock(b, whiteboard);
            }
        }
    }
}
