package io.github.yuazer.zpokeboost;

import io.github.yuazer.zaxlib.Utils.YamlUtils;
import io.github.yuazer.zpokeboost.Hook.TimesHook;
import io.github.yuazer.zpokeboost.Listener.BattleListener;
import io.github.yuazer.zpokeboost.Listener.JoinListener;
import io.github.yuazer.zpokeboost.Utils.BattleUtils;
import io.github.yuazer.zpokeboost.Utils.DataUtils;
import io.github.yuazer.zpokeboost.Utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static String pluginName;

    @Override
    public void onEnable() {
        instance = this;
        pluginName = this.getDescription().getName();
        getLogger().info("§a精灵无尽试炼[1.16.5]");
        getLogger().info("§b作者:Z菌[QQ:1109132]");
        getLogger().info("§b版本:§e" + getDescription().getVersion());
        saveDefaultConfig();
        Bukkit.getPluginCommand("zpokeboost").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new BattleListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        TimesHook.hook();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    TimeUtils.CheckTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20 * 60L);
    }

    @Override
    public void onDisable() {
        DataUtils.getPlayerWinTimes().clear();
        DataUtils.getPlayerState().clear();
        getLogger().info("§b[精灵无尽试炼] §c已经卸载");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("zpokeboost")) {
            if (args.length == 0 || ("reload".equalsIgnoreCase(args[0]) && sender.hasPermission("zpokeboost.admin"))) {
                reloadConfig();
                sender.sendMessage(YamlUtils.getConfigMessage("Message.reload", pluginName));
                return true;
            }
            if (args[0].equalsIgnoreCase("test") && sender.isOp()) {
                System.out.println(DataUtils.getPlayerState());
                return true;
            }
            if (args[0].equalsIgnoreCase("setTeam") && sender.isOp()) {
                Player player = (Player) sender;
                try {
                    DataUtils.setTeam_NBT(player, args[1]);
                    player.sendMessage(YamlUtils.getConfigMessage("Message.successSave", pluginName));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e1) {
                    player.sendMessage("§c参数不完整!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§c精灵无尽试炼 - §a指令帮助");
                sender.sendMessage("§e/zpokeboost §a简写-> §e/zpbt");
                sender.sendMessage("§e/zpokeboost join [试炼塔名] §a- §b加入该试炼塔");
                sender.sendMessage("§e/zpokeboost check §a- §b查看自己剩余挑战次数");
                if (sender.isOp()) {
                    sender.sendMessage("§e/zpokeboost setTeam [试炼塔名] §a- §b将背包中的精灵快速存储为试炼塔精灵队伍(试炼塔最多6只精灵)");
                    sender.sendMessage("§e/zpokeboost setTimes [玩家ID] [次数] §a- §b设置该玩家挑战次数");
                    sender.sendMessage("§e/zpokeboost reload §a- §b重载配置文件");
                }
            }
            if (args[0].equalsIgnoreCase("check")) {
                sender.sendMessage(String.valueOf(DataUtils.getTimes((Player) sender)));
                return true;
            }
            if (args[0].equalsIgnoreCase("setTimes") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                try {
                    DataUtils.setTimes(player, Integer.parseInt(args[2]));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e1) {
                    player.sendMessage("§c参数不完整!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String towerName = args[1];
                    try {
                        if (DataUtils.getTimes(player) > 0) {
                            BattleUtils.battlePokemon(player, DataUtils.getTeam_NBT(towerName));
                            DataUtils.setState(player.getUniqueId(), args[1]);
                        } else {
                            player.sendMessage(YamlUtils.getConfigMessage("Message.noTimes", Main.pluginName));
                        }
                    } catch (IOException e) {
                        player.sendMessage("§c该试炼塔配置出错,请查看是否存在该配置!");
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("quit") && sender.hasPermission("zpokeboost.quit")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (!DataUtils.noTower(player.getUniqueId())) {
                        DataUtils.getPlayerState().put(player.getUniqueId(), null);
                        player.sendMessage(YamlUtils.getConfigMessage("Message.quitSuccess", pluginName));
                    } else {
                        player.sendMessage(YamlUtils.getConfigMessage("Message.quitFail", pluginName));
                    }
                }
                return true;
            }
        }
        return false;
    }
}
