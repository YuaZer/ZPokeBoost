package io.github.yuazer.zpokeboost.Utils;

import io.github.yuazer.zaxlib.Utils.YamlUtils;
import io.github.yuazer.zpokeboost.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTime() {
        Date date = new Date();
        String time1 = new SimpleDateFormat("HHmm").format(date);
        return time1;
    }

    public static void CheckTime() throws IOException {
        if (getTime().equalsIgnoreCase(YamlUtils.getConfigMessage("Times.reset", Main.pluginName))) {

//            for (String path : YamlUtils.getAllFile("plugins/ZPokeBoost/Times", false)) {
//                File file = new File(path);
//                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
//                conf.set("times", YamlUtils.getConfigInt("Times.default", Main.pluginName));
//                conf.save(file);
//            }
            //TODO 重置所有玩家挑战次数
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (String towerName : Main.getInstance().getConfig().getConfigurationSection("Tower").getKeys(false)) {
                    DataUtils.setTimes(player, YamlUtils.getConfigInt("Tower." + towerName + ".default", Main.pluginName), towerName);
                }
            }
            //TODO 执行所有试炼塔的重置指令
            for (String towerName : Main.getInstance().getConfig().getConfigurationSection("Tower").getKeys(false)) {
                for (String cmd : YamlUtils.getConfigStringList("Tower." + towerName + ".commands", Main.pluginName)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }
    }
}
