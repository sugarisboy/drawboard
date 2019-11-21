package ru.englishcraft.drawboard.board;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

    public boolean isContains(Block block) {
        Location location = block.getLocation();
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
