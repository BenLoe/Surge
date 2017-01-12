package org.Prison.Surge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.BenLoe.Gadgets.Types.DeviceType;


public class Events implements Listener{
	
	@EventHandler
	public void inventory(InventoryClickEvent event){
		Player p = (Player) event.getWhoClicked();
		if (p.getOpenInventory().getTopInventory().getTitle().contains("Surges")){
			event.setCancelled(true);
			if (getSlot(event.getRawSlot()) > 0 && event.getCurrentItem().getType().equals(Material.PRISMARINE_SHARD)){
				List<String> util = Files.getDataFile().getStringList("SurgeKeys." + p.getUniqueId().toString() + ".Surges");
				List<String> util2 = new ArrayList<String>();
				for (String s : util){
					boolean add = true;
					for (String s1 : Files.getDataFile().getStringList("Queue")){
						int util3 = Integer.parseInt(s1.split(",")[1]);
						if (Integer.parseInt(s.split(",")[0]) == util3){
							add = false;
						}
					}
					if (add) util2.add(s);
				}
				String s = util2.get(getSlot(event.getRawSlot()) - 1);
				boolean was = SurgeAPI.isSurged();
				SurgeAPI.activateSurge(p.getUniqueId().toString(), Integer.parseInt(s.split(",")[0]));
				p.closeInventory();
				if (!was){
					p.sendMessage("§3Your surge key has been §aactivated§3.");
				}else{
					p.sendMessage("§3Your surge key has been §aqueued§3.");
				}
			}
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractAtEntityEvent event){
		if(event.getRightClicked().getName().equals("surge") && event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)){
			Player p = event.getPlayer();
			if (SurgeAPI.isSurged()){
				if (!SurgeAPI.getSurgeName().equals(p.getName())){
					p.sendMessage("§cYou cannot thank yourself.");
				}else{
					if (Files.getDataFile().contains(p.getUniqueId().toString())){
						if (Files.getDataFile().getInt(p.getUniqueId().toString()) == Files.getDataFile().getInt("UtilID")){
							p.sendMessage("§aYou have already thanked this person for this surge.");
							return;
						}
					}
					DeviceType.SURGE.addAmount(1, p);
					p.sendMessage("§aYou were given 1 surge present for thanking §b" + SurgeAPI.getSurgeName() + "§a.");
					DeviceType.SURGE.addAmount(1, SurgeAPI.getSurgeUUID().toString());
					String name = SurgeAPI.getSurgeName();
					String uuid = SurgeAPI.getSurgeUUID().toString();
					if (Bukkit.getPlayer(name) != null){
						Bukkit.getPlayer(name).sendMessage("§aThanked by §b" + p.getName() + "§a.");
					}else{
						if (Files.getDataFile().contains("Offline." + uuid)){
							Files.getDataFile().set("Offline." + uuid, Files.getDataFile().getInt("Offline." + uuid) + 1);
						}else{
							Files.getDataFile().set("Offline." + uuid, 1);
						}
					}
					Files.getDataFile().set(uuid, Files.getDataFile().getInt("UtilID"));
					Files.saveDataFile();
				}
			}
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event){
		Player p = event.getPlayer();
		if (Files.getDataFile().contains("Offline." + p.getUniqueId().toString())){
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
				public void run(){
					int amount = Files.getDataFile().getInt("Offline." + p.getUniqueId().toString());
					p.sendMessage("§aWhile offline you were thanked §b" + amount + "§a times.");
					Files.getDataFile().set("Offline." + p.getUniqueId().toString(), null);
					Files.saveDataFile();
				}
			}, 3 * 20l);
		}
	}
	
	public static int getSlot(int i){
		if (i == 19) return 1;
		if (i == 20) return 2;
		if (i == 21) return 3;
		if (i == 22) return 4;
		if (i == 23) return 5;
		if (i == 24) return 6;
		if (i == 25) return 7;
		if (i == 28) return 8;
		if (i == 29) return 9;
		if (i == 30) return 10;
		if (i == 31) return 11;
		if (i == 32) return 12;
		if (i == 33) return 13;
		if (i == 34) return 14;
		if (i == 37) return 15;
		if (i == 38) return 16;
		if (i == 39) return 17;
		if (i == 40) return 18;
		if (i == 41) return 19;
		if (i == 42) return 20;
		if (i == 43) return 21;
		return -1;
	}
	
}
