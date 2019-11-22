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

    public void drawBlock(Block block, Material material) {
        if (material.equals(block.getType()))
            return;

        blockUpdated.put(block, block.getType());
        /*blockUpdated.forEach((key, value) ->
            Bukkit.broadcastMessage(String.format("[%d] %d %d %d %s: %s",
                id,
                key.getX(), key.getY(), key.getZ(),
                key.getType().name(),
                value.name()
            )));*/
        block.setType(material);
    }

    public void cancel() {
        blockUpdated.forEach(Block::setType);
        cancel = true;
    }
}
