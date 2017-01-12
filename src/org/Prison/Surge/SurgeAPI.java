package org.Prison.Surge;

import java.util.List;
import java.util.UUID;

import org.Prison.Main.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SurgeAPI {

	public static boolean isSurged(){
		if (Files.getDataFile().contains("SurgeInfo.Surged")){
			return Files.getDataFile().getBoolean("SurgeInfo.Surged");
		}
		return false;
	}
	
	public static String getSurgeName(){
		if (isSurged()){
			return Files.getDataFile().getString("SurgeInfo.Name");
		}
		return null;
	}
	
	public static UUID getSurgeUUID(){
		if (isSurged()){
			return UUID.fromString(Files.getDataFile().getString("SurgeInfo.UUID"));
		}
		return null;
	}
	
	public static Double getSurgeMultiply(){
		if (isSurged()){
			return Files.getDataFile().getDouble("SurgeInfo.Multiply");
		}
		return null;
	}
	
	public static void activateSurge(String uuid, int id){
		if (!isSurged()){
			Files.getDataFile().set("SurgeInfo.Surged", true);
			SurgeKey surge = SurgeKey.getSurgeKey(UUID.fromString(uuid), id);
			TimeManager.surged = true;
			Files.getDataFile().set("SurgeInfo.Name", surge.getPlayerName());
			Files.getDataFile().set("SurgeInfo.UUID", surge.getPlayerUUID().toString());
			Files.getDataFile().set("SurgeInfo.Multiply", surge.getMultiply());
			TimeManager.time = surge.getTime();
			TimeManager.changeSurge();
			ParticleEffect.FIREWORKS_SPARK.display(0.1f, 1.5f, 0.1f, 0.1f, 200, new Location(Bukkit.getWorld("PrisonMap"), -265, 67.6, 281), 20);
			String util = "";
			for (String s : Files.getDataFile().getStringList("SurgeKeys." + uuid + ".Surges")){
				if (Integer.parseInt(s.split(",")[0]) == id){
					util = s;
				}
			}
			List<String> utils = Files.getDataFile().getStringList("SurgeKeys." + uuid + ".Surges");
			utils.remove(util);
			if (utils.size() > 0){
				Files.getDataFile().set("SurgeKeys." + uuid + ".Surges", utils);
			}else{
				Files.getDataFile().set("SurgeKeys." + uuid, null);
			}
			if (Files.getDataFile().contains("UtilID")){
				Files.getDataFile().set("UtilID", Files.getDataFile().getInt("UtilID") + 1);
			}else{
				Files.getDataFile().set("UtilID", 0);
			}
			Files.saveDataFile();
			for (Player p : Bukkit.getOnlinePlayers()){
				p.sendMessage("§3§l>> §e" + surge.getPlayerName() + "'s §bsurge key has just been activated.");
				p.sendMessage("§3§l>> §e" + surge.getMultiply() + "x §bmoney for §e" + (surge.getTime()/60) + " minutes§b.");
			}
		}else{
			List<String> queue = Files.getDataFile().getStringList("Queue");
			queue.add(uuid + "," + id);
			Files.getDataFile().set("Queue", queue);
			Files.saveDataFile();
		}
	}
}
