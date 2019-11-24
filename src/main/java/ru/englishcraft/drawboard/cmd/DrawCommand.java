package ru.englishcraft.drawboard.cmd;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.englishcraft.drawboard.DrawBoard;
import ru.englishcraft.drawboard.config.Config;
import ru.englishcraft.drawboard.utils.PlayerUtils;

import java.util.Arrays;

public class DrawCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            PlayerInventory inventory = player.getInventory();
            Config config = DrawBoard.getInstance().config();

            Material materialMagicWand = config.getMagicWand();
            Material materialCancel = config.getCancelWand();
            Material materialClear = config.getClearWand();

            ItemStack magicwand = PlayerUtils.generateItem(materialMagicWand, "Кисточка",
                Arrays.asList(
                    "Зажми правую кнопку мыши для рисования на холсте,",
                    "нажми на правую кнопку мыши, чтобы закрасить один блок.",
                    "",
                    "Нажми левую кнопку мыши с курсором на доске,",
                    "чтобы сменить цвет на другой."
                )
            );

            ItemStack cancel = PlayerUtils.generateItem(materialCancel, "Отмена",
                Arrays.asList(
                    "Нажми на правую кнопку мыши, смотря на доску,",
                    "для отмены последнего действия."
                )
            );

            ItemStack clear = PlayerUtils.generateItem(materialClear, "Очистка",
                Arrays.asList(
                    "Нажми на правую кнопку мыши, смотря на доску,",
                    "для очистки всего холста."
                )
            );

            player.getInventory().setItem(0, magicwand);
            player.getInventory().setItem(1, cancel);
            player.getInventory().setItem(2, clear);

        } else {
            commandSender.sendMessage("Command only for players!");
        }
        return true;
    }
}
