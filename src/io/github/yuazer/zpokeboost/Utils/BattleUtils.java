package io.github.yuazer.zpokeboost.Utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import io.github.yuazer.zaxlib.Utils.NMSUtils;
import io.github.yuazer.zaxlib.Utils.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class BattleUtils {
    public static void battlePokemon(Player player, List<Pokemon> pokemons) {
        NPCTrainer npcTrainer = new NPCTrainer(NMSUtils.bkToNmsWorld(player.getWorld()));
        for (int i = 0; i <= pokemons.size() - 1; i++) {
            npcTrainer.getPokemonStorage().set(i, pokemons.get(i));
        }
        BattleParticipant[] bp =
                {
                        new PlayerParticipant(PlayerUtil.getServerPlayerEntity(player),
                        StorageProxy.getParty(player.getUniqueId()).getAndSendOutFirstAblePokemon(PlayerUtil.getServerPlayerEntity(player)))
                };
        BattleParticipant[] tp = {new TrainerParticipant(npcTrainer, 1)};
        //tp.startBattle(bp.bc);
        BattleRegistry.startBattle(tp, bp,new BattleRules());
    }
}
