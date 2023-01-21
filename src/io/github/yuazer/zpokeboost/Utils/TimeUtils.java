package io.github.yuazer.zpokeboost.Utils;

import io.github.yuazer.zaxlib.Utils.YamlUtils;
import io.github.yuazer.zpokeboost.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

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
            for (String path : YamlUtils.getAllFile("plugins/ZPokeBoost/Times", false)) {
                File file = new File(path);
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
                conf.set("times", YamlUtils.getConfigInt("Times.default", Main.pluginName));
                conf.save(file);
            }
            for (String cmd : YamlUtils.getConfigStringList("Times.commands", Main.pluginName)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
    }
}
