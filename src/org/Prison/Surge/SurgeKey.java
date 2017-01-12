package org.Prison.Surge;

import java.util.UUID;

public class SurgeKey {
	
	private Double multiply;
	private UUID playerUUID;
	private String playerName;
	private Integer id;
	private Integer time;
	
	public SurgeKey(Double multiply, UUID playerUUID, String playerName, Integer id, Integer time){
		this.multiply = multiply;
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.id = id;
		this.time = time;
	}
	
	public Double getMultiply(){
		return multiply;
	}
	
	public UUID getPlayerUUID(){
		return playerUUID;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public Integer getId(){
		return id;
	}
	
	public Integer getTime(){
		return time;
	}
	
	public static SurgeKey getSurgeKey(UUID playerUUID, Integer id){
		String uuid = playerUUID.toString();
		if (Files.getDataFile().contains("SurgeKeys." + uuid + ".Surges")){
			for (String s : Files.getDataFile().getStringList("SurgeKeys." + uuid + ".Surges")){
				String idutil = s.split(",")[0];
				String timeutil = s.split(",")[1];
				String multiplyutil = s.split(",")[2];
				
				int idint = Integer.parseInt(idutil);
				int timeint = Integer.parseInt(timeutil);
				double multiplydouble = Double.parseDouble(multiplyutil);
				
				if (idint == id){
					return new SurgeKey(multiplydouble, playerUUID, (String) org.Prison.Main.Files.getDataFile().get("Players." + uuid + ".Name"), id, timeint);
				}
			}
		}
		return null;
	}
	
}
