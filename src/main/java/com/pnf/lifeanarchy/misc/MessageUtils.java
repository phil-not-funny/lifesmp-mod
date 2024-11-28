package com.pnf.lifeanarchy.misc;

import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageUtils {
	
	public static void displayTitle(ServerPlayerEntity p, String msg, Formatting format) {
		p.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(msg).formatted(format)));
	}
	
}
