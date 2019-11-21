package ru.englishcraft.drawboard.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.bukkit.Material;
import ru.englishcraft.drawboard.board.Board;
import ru.englishcraft.drawboard.config.adapters.ConfigAdapter;

import java.util.List;

public class Config extends ConfigAdapter {

    @Expose
    @SerializedName("boards")
    @Getter
    private List<Board> boards;

    @Expose
    @SerializedName("whiteboard-block")
    @Getter
    private Material whiteboardBlock;

    @Expose
    @SerializedName("magic-wand")
    @Getter
    private Material magicWand;


}
