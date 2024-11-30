package com.pnf.lifeanarchy.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerDataManager {
	private static final File PLAYERDATA_FOLDER = new File("config/lifeanarchy");
    private static final Yaml YAML = new Yaml();
    
	static {
		if (!PLAYERDATA_FOLDER.exists())
			PLAYERDATA_FOLDER.mkdirs();
	}
	
	private static File getPlayerFile(ServerPlayerEntity player) {
        return new File(PLAYERDATA_FOLDER, player.getUuidAsString() + ".yaml");
    }
	
	public static void savePlayerint(ServerPlayerEntity p, int value, String key) {
		File playerFile = getPlayerFile(p);
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put(key, value);
		
		try (FileWriter fw = new FileWriter(playerFile)) {
			YAML.dump(data, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int loadPlayerInt(ServerPlayerEntity p, String key) {
		File playerFile = getPlayerFile(p);
		
		if(!playerFile.exists()) return -1;
		
		try (FileReader fr = new FileReader(playerFile)) {
			Map<String, Object> data = YAML.load(fr);
			return (int) data.getOrDefault(key, -1);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
}
