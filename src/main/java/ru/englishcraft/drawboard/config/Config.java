package ru.englishcraft.drawboard.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import ru.englishcraft.drawboard.models.Board;

import java.util.List;

public class Config extends ConfigAdapter {

    @Expose
    @SerializedName("boards")
    @Getter
    private List<Board> boards;
}
