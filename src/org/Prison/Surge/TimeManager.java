package org.Prison.Surge;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class TimeManager {

	public static int time = 0;
	public static boolean surged = SurgeAPI.isSurged();
	public static Hologram hologram;
	
	public static void check(){
		if (surged){
			int newtime = time - 1;
			if (newtime != 0){
				Location block = new Location(Bukkit.getWorld("PrisonMap"), -267, 65, 279);
				if (block.getBlock().getType().equals(Material.STAINED_GLASS)){
					changeSurge();
				}
				int sec = newtime;
				Date d = new Date(sec * 1000L);
				SimpleDateFormat df = new SimpleDateFormat("mm:ss"); 
				df.setTimeZone(TimeZone.getTimeZone("EST"));
				String formated = df.format(d);
				TextLine tl = (TextLine) hologram.getLine(1);
				tl.setText("§e§l" + SurgeAPI.getSurgeMultiply() + "x §eMoney§7 - " + formated + " remaining");
				time = newtime;
			}else{
				surged = false;
				Files.getDataFile().set("SurgeInfo.Surged", false);
				Files.saveDataFile();
				changeNoSurge();
				for (Player p : Bukkit.getOnlinePlayers()){
					p.sendMessage("§3§l>> §e" + Files.getDataFile().getString("SurgeInfo.Name") + "'s §bsurge has just worn off.");
				}
				if (Files.getDataFile().getStringList("Queue").size() > 0){
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable(){
						public void run(){
							String s = Files.getDataFile().getStringList("Queue").get(0);
							SurgeAPI.activateSurge(s.split(",")[0], Integer.parseInt(s.split(",")[1]));
							List<String> queue = Files.getDataFile().getStringList("Queue");
							queue.remove(0);
							Files.getDataFile().set("Queue", queue);
							Files.saveDataFile();
						}
					}, 40l);	
				}
			}
		}else{
			Location block = new Location(Bukkit.getWorld("PrisonMap"), -267, 65, 279);
			if (block.getBlock().getType().equals(Material.SEA_LANTERN)){
				changeNoSurge();
			}
		}
	}
	
	public static void changeSurge(){
		surged = true;
		boolean armorfound = false;
		for (ArmorStand as : Bukkit.getWorld("PrisonMap").getEntitiesByClass(ArmorStand.class)){
			if (as.getLocation().getBlockX() == -265 && as.getLocation().getBlockZ() == 281 && as.getCustomName().equals("surge")){
				armorfound = true;
				ItemStack skull = new ItemStack(397, 1, (byte)3);
				SkullMeta skullm = (SkullMeta) skull.getItemMeta();
				skullm.setOwner(SurgeAPI.getSurgeName());
                skull.setItemMeta(skullm);
                as.getEquipment().setHelmet(skull);
                
                ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta chestm = (LeatherArmorMeta) chest.getItemMeta();
                ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                LeatherArmorMeta leggingsm = (LeatherArmorMeta) leggings.getItemMeta();
                ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
                LeatherArmorMeta bootm = (LeatherArmorMeta) boot.getItemMeta();
                
                chestm.setColor(Color.fromRGB(3, 235, 235));
                leggingsm.setColor(Color.fromRGB(3, 235, 235));
                bootm.setColor(Color.fromRGB(3, 235, 235));
                chest.setItemMeta(chestm);
                leggings.setItemMeta(leggingsm);
                boot.setItemMeta(bootm);
                
                as.setArms(true);
                as.getEquipment().setChestplate(chest);
                as.getEquipment().setLeggings(leggings);
                as.getEquipment().setBoots(boot);
			}
		}
		if (!armorfound){
			ArmorStand as = (ArmorStand) Bukkit.getWorld("PrisonMap").spawnEntity(new Location(Bukkit.getWorld("PrisonMap"), -265, 67.6, 281).setDirection(new Vector(0.6, -0.5, 0.6)), EntityType.ARMOR_STAND);
			ItemStack skull = new ItemStack(397, 1, (byte)3);
			SkullMeta skullm = (SkullMeta) skull.getItemMeta();
			skullm.setOwner(SurgeAPI.getSurgeName());
            skull.setItemMeta(skullm);
            as.getEquipment().setHelmet(skull);
            
            ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestm = (LeatherArmorMeta) chest.getItemMeta();
            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta leggingsm = (LeatherArmorMeta) leggings.getItemMeta();
            ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta bootm = (LeatherArmorMeta) boot.getItemMeta();
            
            chestm.setColor(Color.fromRGB(3, 235, 235));
            leggingsm.setColor(Color.fromRGB(3, 235, 235));
            bootm.setColor(Color.fromRGB(3, 235, 235));
            chest.setItemMeta(chestm);
            leggings.setItemMeta(leggingsm);
            boot.setItemMeta(bootm);
            
            as.getEquipment().setChestplate(chest);
            as.getEquipment().setLeggings(leggings);
            as.getEquipment().setBoots(boot);
            as.setCustomName("Surge");
            as.setArms(true);
		}
		
		Location util1 = new Location(Bukkit.getWorld("PrisonMap"), -266, 72, 280);
		Location util2 = new Location(Bukkit.getWorld("PrisonMap"), -267, 65, 279);
		
		util1.getBlock().setType(Material.SEA_LANTERN);
		util2.getBlock().setType(Material.SEA_LANTERN);
		
		Location hloc = new Location(Bukkit.getWorld("PrisonMap"), -265, 70.6, 281);
		hologram.teleport(hloc);
		hologram.clearLines();
		hologram.insertTextLine(0, "§3§lCurrently surged by §b" + SurgeAPI.getSurgeName());
		hologram.insertTextLine(1, "§e§l" + SurgeAPI.getSurgeMultiply() + "x §eMoney§7 -" + (time / 60) + ":00 remaining"); 
		hologram.insertTextLine(2, "§aClick to thank, gives you 1 surge present");
	}
	
	public static void changeNoSurge(){
		boolean armorfound = false;
		for (ArmorStand as : Bukkit.getWorld("PrisonMap").getEntitiesByClass(ArmorStand.class)){
			if (as.getLocation().getBlockX() == -265 && as.getLocation().getBlockZ() == 281 && as.getCustomName().equals("surge")){
				armorfound = true;
				ItemStack skull = new ItemStack(397, 1, (byte)3);
				SkullMeta skullm = (SkullMeta) skull.getItemMeta();
				GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "http://textures.minecraft.net/texture/eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmMDg1YzZiM2NiMjI4ZTViYTgxZGY1NjJjNDc4Njc2MmYzYzI1NzEyN2U5NzI1Yzc3YjdmZDMwMWQzNyJ9fX0=").getBytes());
                profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
                Field profileField = null;
                try {
                    profileField = skullm.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullm, profile);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                skull.setItemMeta(skullm);
                as.getEquipment().setHelmet(skull);
                
                ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta chestm = (LeatherArmorMeta) chest.getItemMeta();
                ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                LeatherArmorMeta leggingsm = (LeatherArmorMeta) leggings.getItemMeta();
                ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
                LeatherArmorMeta bootm = (LeatherArmorMeta) boot.getItemMeta();
                
                chestm.setColor(Color.GRAY);
                leggingsm.setColor(Color.GRAY);
                bootm.setColor(Color.GRAY);
                chest.setItemMeta(chestm);
                leggings.setItemMeta(leggingsm);
                boot.setItemMeta(bootm);
                
                as.setArms(true);
                as.getEquipment().setChestplate(chest);
                as.getEquipment().setLeggings(leggings);
                as.getEquipment().setBoots(boot);
			}
		}
		if (!armorfound){
			ArmorStand as = (ArmorStand) Bukkit.getWorld("PrisonMap").spawnEntity(new Location(Bukkit.getWorld("PrisonMap"), -265, 67.6, 281).setDirection(new Vector(0.6, -0.5, 0.6)), EntityType.ARMOR_STAND);
			ItemStack skull = new ItemStack(397, 1, (byte)3);
			SkullMeta skullm = (SkullMeta) skull.getItemMeta();
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "http://textures.minecraft.net/texture/eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmMDg1YzZiM2NiMjI4ZTViYTgxZGY1NjJjNDc4Njc2MmYzYzI1NzEyN2U5NzI1Yzc3YjdmZDMwMWQzNyJ9fX0=").getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;
            try {
                profileField = skullm.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullm, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
            skull.setItemMeta(skullm);
            as.getEquipment().setHelmet(skull);
            
            ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestm = (LeatherArmorMeta) chest.getItemMeta();
            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta leggingsm = (LeatherArmorMeta) leggings.getItemMeta();
            ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta bootm = (LeatherArmorMeta) boot.getItemMeta();
            
            chestm.setColor(Color.GRAY);
            leggingsm.setColor(Color.GRAY);
            bootm.setColor(Color.GRAY);
            chest.setItemMeta(chestm);
            leggings.setItemMeta(leggingsm);
            boot.setItemMeta(bootm);
            
            as.getEquipment().setChestplate(chest);
            as.getEquipment().setLeggings(leggings);
            as.getEquipment().setBoots(boot);
            as.setCustomName("surge");
            as.setArms(true);
		}
		
		Location util1 = new Location(Bukkit.getWorld("PrisonMap"), -266, 72, 280);
		Location util2 = new Location(Bukkit.getWorld("PrisonMap"), -267, 65, 279);
		
		util1.getBlock().setType(Material.STONE);
		util2.getBlock().setType(Material.STAINED_GLASS);
		util2.getBlock().setData((byte)9);
		
		Location hloc = new Location(Bukkit.getWorld("PrisonMap"), -265, 70.3, 281);
		hologram.teleport(hloc);
		hologram.clearLines();
		hologram.insertTextLine(0, "§7§lPrison currently not surged");
		hologram.insertTextLine(1, "§e/surge §7for more info");
	}
	
}
