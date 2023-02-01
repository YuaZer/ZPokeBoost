package io.github.yuazer.zpokeboost.Listener;

import catserver.api.bukkit.ForgeEventV2;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.github.yuazer.zaxlib.Utils.YamlUtils;
import io.github.yuazer.zpokeboost.Main;
import io.github.yuazer.zpokeboost.Utils.BattleUtils;
import io.github.yuazer.zpokeboost.Utils.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.IOException;

public class BattleListener implements Listener {
    private boolean allDead(Player player) {
        for (Pokemon pokemon : StorageProxy.getParty(player.getUniqueId()).getTeam()) {
            if (pokemon.getHealth() > 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * 监听无尽试炼期间对战事件
     * */
    @EventHandler(priority = EventPriority.HIGH)
    public void onForge(ForgeEventV2 event) throws IOException {
        if (event.getForgeEvent() instanceof BeatTrainerEvent) {
            BeatTrainerEvent e = (BeatTrainerEvent) event.getForgeEvent();
            Player player = Bukkit.getPlayer(e.player.func_110124_au());
            //判断处于无尽试炼状态
            if (!DataUtils.noTower(player.getUniqueId())) {
                //赢了一轮无尽试炼后
                for (String times : Main.getInstance().getConfig().getConfigurationSection("Commands.win").getKeys(false)) {
                    int integer = Integer.parseInt(times);
                    if (integer == DataUtils.getPlayerWinTimes().getOrDefault(player.getUniqueId(), 0)) {
                        for (String cmd : YamlUtils.getConfigStringList("Commands.win." + integer, Main.pluginName)) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()).replace("%tower%",DataUtils.getPlayerState().get(player.getUniqueId())));
                        }
                        break;
                    }
                }
                //判断玩家精灵没死光
                if (!allDead(player)) {
                    player.sendMessage(YamlUtils.getConfigMessage("Message.next", Main.pluginName));
                    //执行下一轮无尽试炼
                    BattleUtils.battlePokemon(player, DataUtils.getTeam_NBT(DataUtils.getPlayerState().get(player.getUniqueId())));
                    //增加挑战成功轮数
                    DataUtils.getPlayerWinTimes().put(player.getUniqueId(), DataUtils.getPlayerWinTimes().getOrDefault(player.getUniqueId(), 0)+1);
                }
            }
        }
        if (event.getForgeEvent() instanceof LostToTrainerEvent) {
            LostToTrainerEvent e = (LostToTrainerEvent) event.getForgeEvent();
            Player player = Bukkit.getPlayer(e.player.func_110124_au());
            //判断玩家处于无尽试炼状态
            if (!DataUtils.noTower(player.getUniqueId())) {
                //执行挑战失败战令
                for (String cmd : YamlUtils.getConfigStringList("Commands.lose", Main.pluginName)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()).replace("%tower%",DataUtils.getPlayerState().get(player.getUniqueId())));
                }
                //退出无尽试炼状态
                DataUtils.getPlayerState().put(player.getUniqueId(), null);
                player.sendMessage(YamlUtils.getConfigMessage("Message.quitSuccess", Main.pluginName));
                //重置挑战成功轮数
                DataUtils.getPlayerWinTimes().put(player.getUniqueId(), 0);
            }
        }
    }
}
