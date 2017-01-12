package org.Prison.Surge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Menu {

	public static void openMenu(Player p){
		Inventory inv = Bukkit.createInventory(null, 9 * 5, ChatColor.BLUE + "Surges");
		ItemStack book = new ItemStack(Material.BOOK);
		ItemMeta bookm = book.getItemMeta();
		List<String> lore = new ArrayList<String>();
		bookm.setDisplayName("§3§lSurges");
		lore.add("");
		lore.add("§bSurge Keys §7activates a surge over the");
		lore.add("§7entire prison, giving players more cash");
		lore.add("§7when selling items or playing arena games.");
		lore.add("");
		lore.add("§7Players can also thank you for using surge");
		lore.add("§7keys, this gives them and you 1 surge present,");
		lore.add("§7openable at the altar.");
		lore.add("");
		lore.add("§bSurge Keys §7obtainable at our store.");
		bookm.setLore(lore);
		book.setItemMeta(bookm);
		inv.setItem(4, book);
		
		boolean hasany = false;
		int slot = 19;
		if (Files.getDataFile().contains("SurgeKeys." + p.getUniqueId().toString() + ".Surges")){
			for (String s : Files.getDataFile().getStringList("SurgeKeys." + p.getUniqueId().toString() + ".Surges")){
				SurgeKey surge = SurgeKey.getSurgeKey(p.getUniqueId(), Integer.parseInt(s.split(",")[0]));
				boolean next = false;
				for (String s1 : Files.getDataFile().getStringList("Queue")){
					int util = Integer.parseInt(s1.split(",")[1]);
					if (surge.getId() == util){
						next = true;
					}
				}
				if (!next){
				ItemStack key = new ItemStack(Material.PRISMARINE_SHARD);
				ItemMeta keym = key.getItemMeta();
				keym.setDisplayName("§b" + (surge.getTime() / 60) + " Minute Surge Key");
				lore.clear();
				lore.add("");
				lore.add("§7Multiplier: §e" + surge.getMultiply() + "x");
				lore.add("");
				if (SurgeAPI.isSurged()){
					lore.add("§7Click to §aqueue §7this surge");
					lore.add("");
					lore.add("§7Since there is already a surge");
					lore.add("§7active, your surge will be queued.");
					int queue = Files.getDataFile().getStringList("Queue").size();
					lore.add("§7Surges currently in queue: §e" + queue);
				}else{
					lore.add("§7Click to §aactivate §7this surge");
				}
				keym.setLore(lore);
				key.setItemMeta(keym);
				hasany = true;
				inv.setItem(slot, key);
				slot += 1;
				if (slot == 26){
					slot = 28;
				}
				if (slot == 35){
					slot = 37;
				}
				if (slot == 44){
					break;
				}
				}
			}
		}
		if (!hasany){
			ItemStack none = new ItemStack(160, 1, (byte)14);
			ItemMeta nonem = none.getItemMeta();
			lore.clear();
			nonem.setDisplayName("§cYou have no surge keys");
			lore.add("");
			lore.add("§7You can buy surge keys on our website.");
			lore.add("§7Ranks also gain surge keys monthly and on");
			lore.add("§7purchase");
			nonem.setLore(lore);
			none.setItemMeta(nonem);
			inv.setItem(22, none);
		}
		p.openInventory(inv)
;	}
	
}
