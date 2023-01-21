package io.github.yuazer.zpokeboost.Utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.github.yuazer.zaxlib.Utils.PokeUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataUtils {
    private static HashMap<UUID, String> playerState = new HashMap();
    private static HashMap<UUID, Integer> playerWinTimes = new HashMap();

    public static HashMap<UUID, String> getPlayerState() {
        return playerState;
    }
    public static HashMap<UUID, Integer> getPlayerWinTimes() {
        return playerWinTimes;
    }
    public static int getTimes(Player player) {
        File file = new File("plugins/ZPokeBoost/Times/" + player.getName() + ".yml");
        return YamlConfiguration.loadConfiguration(file).getInt("times");
    }

    public static void setTimes(Player player, int times) throws IOException {
        File file = new File("plugins/ZPokeBoost/Times/" + player.getName() + ".yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        conf.set("times", times);
        conf.save(file);
    }

    public static boolean noTower(UUID playerUUID) {
        try {
            return playerState.get(playerUUID).isEmpty() || playerState.get(playerUUID) == null;
        } catch (NullPointerException e) {
            return true;
        }
    }

    public static void setState(UUID uuid, String towerName) {
        playerState.put(uuid, towerName);
    }

    public static void setTeam_NBT(Player player, String towerName) throws IOException {
        int i = 0;
        for (Pokemon pokemon : StorageProxy.getParty(player.getUniqueId()).getTeam()) {
            File file = new File("plugins/ZPokeBoost/" + towerName + "/" + i + ".zps");
            if (!file.getParentFile().exists()) {
                Files.createDirectory(file.getParentFile().toPath());
            }
            PokeUtils.setPokemonInFile_NBT(pokemon, file);
            ++i;
        }
    }

    public static List<Pokemon> getTeam_NBT(String towerName) throws IOException {
        List<Pokemon> pokemons = new ArrayList<>();
        File file1 = new File("plugins/ZPokeBoost/" + towerName);
        for (File file : file1.listFiles()) {
            pokemons.add(PokeUtils.getPokemonInFile_NBT(file));
        }
        return pokemons;
    }
}
