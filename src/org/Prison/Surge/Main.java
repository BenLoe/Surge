package org.Prison.Surge;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.Prison.Main.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				TimeManager.check();
			}
		}, 20l, 20l);
		Bukkit.getScheduler().runTaskLater(this, new Runnable(){
			public void run(){
				Location loc;
				if (SurgeAPI.isSurged()){
					loc = new Location(Bukkit.getWorld("PrisonMap"), -265, 70.6, 281);
					TimeManager.time = Files.getDataFile().getInt("TimeSave");
				}else{
					loc = new Location(Bukkit.getWorld("PrisonMap"), -265, 70.3, 281);
				}
				Hologram h = HologramsAPI.createHologram(Main.getPlugin(Main.class),  loc);
				if (SurgeAPI.isSurged()){
					h.insertTextLine(0, "§3§lCurrently surged by §b" + SurgeAPI.getSurgeName());
					h.insertTextLine(1, "§e§l" + SurgeAPI.getSurgeMultiply() + "x §eMoney§7 - 0:00 remaining"); 
					h.insertTextLine(2, "§aClick to thank, gives you 1 surge present");
				}else{
					h.insertTextLine(0, "§7§lPrison currently not surged");
					h.insertTextLine(1, "§e/surge §7for more info");
				}
				h.getVisibilityManager().setVisibleByDefault(true);
				TimeManager.hologram = h;
			}
		}, 10l);
		if (!Files.getDataFile().contains("Queue")){
			Files.getDataFile().set("Queue", new ArrayList<String>());
			Files.saveDataFile();
		}
		if (!Files.getDataFile().contains("idutil")){
			Files.getDataFile().set("idutil", 1);
			Files.saveDataFile();
		}
	}
	
	public void onDisable(){
		TimeManager.hologram.delete();
		if (SurgeAPI.isSurged()){
			Files.getDataFile().set("TimeSave", TimeManager.time);
			Files.saveDataFile();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String Label, String[] args){
		if (Label.equalsIgnoreCase("giveSurge")){
			if (sender instanceof Player && !((Player) sender).hasPermission("Surge.give")){
				sender.sendMessage(ChatColor.RED + "No permission.");
				return true;
			}
			if (args.length != 3){
				sender.sendMessage(ChatColor.RED + "Usage: /givesurge (player) (multiply) (time)");
			}else{
				UUID uuid;
				if (Bukkit.getPlayer(args[0]) != null){
					uuid = Bukkit.getPlayer(args[0]).getUniqueId();
				}else{
					try {
						uuid = UUIDFetcher.getUUIDOf(args[0]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + "Player does not exist.");
						return true;
					}
				}
				int id = Files.getDataFile().getInt("idutil");
				List<String> surges = new ArrayList<String>();
				if (Files.getDataFile().contains("SurgeKeys." + uuid + ".Surges")){
					surges.addAll(Files.getDataFile().getStringList("SurgeKeys." + uuid + ".Surges"));
				}
				surges.add(0, id + "," + args[2] + "," + Double.parseDouble(args[1]));
				Files.getDataFile().set("SurgeKeys." + uuid + ".Surges", surges);
				Files.getDataFile().set("idutil", id + 1);
				Files.saveDataFile();
			}
		}
		if (Label.equalsIgnoreCase("activate")){
			Player p = (Player) sender;
			if (args.length != 1){
				p.sendMessage(ChatColor.RED + "Usage: /activate (id)");
			}else{
				String util = "";
				for (String s : Files.getDataFile().getStringList("SurgeKeys." + p.getUniqueId().toString() + ".Surges")){
					if (Integer.parseInt(s.split(",")[0]) == Integer.parseInt(args[0])){
						util = s;
					}
				}
				SurgeAPI.activateSurge(p.getUniqueId().toString(), Integer.parseInt(args[0]));
				List<String> utils = Files.getDataFile().getStringList("SurgeKeys." + p.getUniqueId().toString() + ".Surges");
				utils.remove(util);
				Files.getDataFile().set("SurgeKeys." + p.getUniqueId().toString() + ".Surges", utils);
				Files.saveDataFile();
			}
		}
		if (Label.equalsIgnoreCase("surge")){
			if (sender instanceof Player){
				Player p = (Player) sender;
				Menu.openMenu(p);
			}
		}
		return true;
	}
	
}
