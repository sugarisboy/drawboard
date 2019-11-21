package ru.englishcraft.drawboard.board;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.config.Config;

import java.util.ArrayList;
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

    private transient Map<Player, List<DrawAction>> actions;

    private static transient final double K = 2D;
    private static transient final long DROP_LINE_TIME = 75L;
    private static transient final Material COLOR = Material.BLACK_WOOL;

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
        if (action.getTime() - prev.getTime() < DROP_LINE_TIME) {
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
            return actions.get(player).get(0);
        }
    }

    private void addAction(DrawAction action) {
        actions.get(action.getPlayer()).add(action);
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
}
