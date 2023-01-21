package io.github.yuazer.zpokeboost.Listener;

import io.github.yuazer.zaxlib.Utils.YamlUtils;
import io.github.yuazer.zpokeboost.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        File file = new File("plugins/ZPokeBoost/Times/" + player.getName() + ".yml");
        if (!file.getParentFile().exists()) {
            Files.createDirectory(file.getParentFile().toPath());
        }
        if (!file.exists()) {
            file.createNewFile();
            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
            conf.set("times", YamlUtils.getConfigInt("Times.default", Main.pluginName));
            conf.save(file);
        }
    }
}
