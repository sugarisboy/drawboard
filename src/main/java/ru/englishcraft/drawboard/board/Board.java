package ru.englishcraft.drawboard.board;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.config.Config;

import java.util.List;

@Data
public class Board {

    @Expose
    @SerializedName("position1")
    private Location p1;

    @Expose
    @SerializedName("position2")
    private Location p2;

    private transient Block lastDrawBlock;

    private transient final double K = 2;

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

    public void draw(Block block) {
        if (!isContains(block))
            return;

        if (lastDrawBlock == null)
            lastDrawBlock = block;

        // DRAW

        double modifer = block.getLocation().distanceSquared(lastDrawBlock.getLocation()) * K;
        Vector vector = new Vector(
            (lastDrawBlock.getX() - block.getX()) / modifer,
            (lastDrawBlock.getY() - block.getY()) / modifer,
            (lastDrawBlock.getZ() - block.getZ()) / modifer
        );
        Location l = block.getLocation().clone();
        Block b;
        for (int i = 0; i < modifer; i++) {
            Bukkit.broadcastMessage(vector +  "");
            l = l.add(vector);
            b = l.getBlock();
            b.setType(Material.BLACK_WOOL);
        }

        // END
        lastDrawBlock = block;
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
