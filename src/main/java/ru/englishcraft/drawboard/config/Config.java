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

    @Expose
    @SerializedName("clear-wand")
    @Getter
    private Material clearWand;

    @Expose
    @SerializedName("cancel-wand")
    @Getter
    private Material cancelWand;

    @Expose
    @SerializedName("drop-line-time")
    @Getter
    private long dropLineTime = 175L;

    @Expose
    @SerializedName("depth-action-story")
    @Getter
    private int depthActionStory = 30;

    @Expose
    @SerializedName("use-chinese-name")
    @Getter
    private boolean useChineseName = true;

    @Expose
    @SerializedName("max-width-board")
    @Getter
    private int maxWidthBoard = 100;

    @Expose
    @SerializedName("max-height-board")
    @Getter
    private int maxHeightBoard = 100;
}
