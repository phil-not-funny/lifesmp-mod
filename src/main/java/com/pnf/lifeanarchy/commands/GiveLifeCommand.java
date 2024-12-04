package com.pnf.lifeanarchy.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.managers.CommandManager;
import com.pnf.lifeanarchy.misc.MessageUtils;
import com.pnf.lifeanarchy.misc.ScoreboardUtils;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

import java.util.HashSet;
import java.util.Set;

public class GiveLifeCommand {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity executer = context.getSource().getPlayer();
        if (executer == null)
            return 0;
        if (PlayerDataManager.loadPlayerInt(executer, "lives") < 2) {
            context.getSource().sendError(Text.literal("You may only give lives if you have 2 or more yourself!"));
            return 0;
        }

        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        if (PlayerDataManager.loadPlayerInt(player, "lives") <= 0) {
            if (!player.getServer().getGameRules().getBoolean(CommandManager.GR_ALLOW_GIVELIFE_TO_GREYS)) {
                context.getSource().sendError(Text.literal("You may not give lives to a player that's out!"));
                return 0;
            } else {
                player.requestRespawn();
                var spawn = player.getWorld().getSpawnPos();
                player.teleport((ServerWorld) player.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ(), new HashSet<>(), 0, 0, true);
                player.changeGameMode(GameMode.SURVIVAL);
            }
        }

        // add Life
        PlayerDataManager.savePlayerint(player, PlayerDataManager.loadPlayerInt(player, "lives") + 1, "lives");
        // take life
        PlayerDataManager.savePlayerint(executer, PlayerDataManager.loadPlayerInt(executer, "lives") - 1, "lives");

        context.getSource().sendFeedback(
                () -> Text.literal(executer.getName().getString() + " gave a life to " + player.getName().getString()),
                true);
        MessageUtils.displayTitle(player, "You recieved a life!", Formatting.GREEN);
        MessageUtils.displayTitle(executer, "You gave a life!", Formatting.DARK_GREEN);
        ScoreboardUtils.updatePlayerTeam(player);
        ScoreboardUtils.updatePlayerTeam(executer);
        return 1;
    }
}
