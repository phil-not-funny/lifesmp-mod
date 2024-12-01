package com.pnf.lifeanarchy.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ModConfigManager {
	private static final File CONFIG_FOLDER = new File("config/lifeanarchy");
	private static final Yaml YAML = new Yaml();
	
	private static File getConfigFile() {
		return new File(CONFIG_FOLDER, "config.yaml");
	}
	
	public static void saveString(String key, String value) {
		File confFile = getConfigFile();
		Map<String, Object> data = loadAllData();
		data.put(key, value);
		
		try (FileWriter fw = new FileWriter(confFile)) {
			YAML.dump(data, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveInt(String key, int value) {
		File confFile = getConfigFile();
		Map<String, Object> data = loadAllData();
		data.put(key, value);
		
		try (FileWriter fw = new FileWriter(confFile)) {
			YAML.dump(data, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String loadString(String key) {
		File confFile = getConfigFile();
		if(!confFile.exists()) return null;
		
		try (FileReader fr = new FileReader(confFile)) {
			Map<String, Object> data = YAML.load(fr);
			return (String) data.getOrDefault(key, null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int loadInt(String key) {
		File confFile = getConfigFile();
		if(!confFile.exists()) return -1;
		
		try (FileReader fr = new FileReader(confFile)) {
			Map<String, Object> data = YAML.load(fr);
			return (int) data.getOrDefault(key, -1);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void savePlayerList(String key, List<String> uuids) {
        File confFile = getConfigFile();
        Map<String, Object> data = loadAllData();

        data.put(key, uuids);

        try (FileWriter fw = new FileWriter(confFile)) {
            YAML.dump(data, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadPlayerList(String key) {
        Map<String, Object> data = loadAllData();
        Object value = data.get(key);

        if (value instanceof List<?>) {
            List<?> rawList = (List<?>) value;
            List<String> players = new ArrayList<>();
            for (Object item : rawList) {
                if (item instanceof String) {
                    players.add((String) item);
                }
            }
            return players;
        }
        return new ArrayList<>();
    }
	
	private static Map<String, Object> loadAllData() {
        File confFile = getConfigFile();
        if (!confFile.exists()) return new HashMap<>();

        try (FileReader fr = new FileReader(confFile)) {
            return YAML.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
