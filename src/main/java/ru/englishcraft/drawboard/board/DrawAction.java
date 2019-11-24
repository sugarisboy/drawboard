package ru.englishcraft.drawboard.board;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DrawAction {

    @Getter
    private HashMap<Block, Material> blockUpdated;

    @Getter
    private Player player;

    @Getter
    private long time;

    @Getter
    private boolean cancel;

    @Getter
    private Block mainBlock;

    @Getter
    @Setter
    private DrawActionType type = DrawActionType.DRAW;

    public DrawAction(Player player, Block block) {
        this.player = player;
        this.time = System.currentTimeMillis();
        this.blockUpdated = new HashMap<>();
        this.cancel = false;
        this.mainBlock = block;
    }

    public void changeBlock(Block block, DrawColor color) {
        Material material = color.getMaterial();
        if (material.equals(block.getType()))
            return;

        blockUpdated.put(block, block.getType());
        block.setType(material);
    }

    public void cancel() {
        blockUpdated.forEach(Block::setType);
        cancel = true;
    }
}
