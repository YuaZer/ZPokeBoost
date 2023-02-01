package io.github.yuazer.zpokeboost.Hook;

import io.github.yuazer.zpokeboost.Utils.DataUtils;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TimesHook extends PlaceholderHook {
    private static final String hook_name = "zpokeboost";

    @Override
    public String onPlaceholderRequest(Player p, String indentifier) {
        if (p == null) {
            return "";
        }
        if (indentifier.contains("_")) {
            String[] args = indentifier.split("_");
            if (args[0].equalsIgnoreCase("times")) {
                try {
                    return String.valueOf(DataUtils.getTimes(p, args[1]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "error";
    }

    public static void hook() {
        me.clip.placeholderapi.PlaceholderAPI.registerPlaceholderHook(hook_name, new TimesHook());
    }

    public static void unhook() {
        me.clip.placeholderapi.PlaceholderAPI.unregisterPlaceholderHook(hook_name);
    }
}
