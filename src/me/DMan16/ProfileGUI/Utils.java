package me.DMan16.ProfileGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
	private static final Pattern unicode = Pattern.compile("\\\\u\\+[a-fA-F0-9]{4}");
	
	static String chatColors(String str) {
		str = chatColorsStrip(str);
		Matcher match = pattern.matcher(str);
		while (match.find()) {
			String color = str.substring(match.start(),match.end());
			str = str.replace(color,ChatColor.of(color.replace("&","")) + "");
			match = pattern.matcher(str);
		}
		match = unicode.matcher(str);
		while (match.find()) {
			String code = str.substring(match.start(),match.end());
			str = str.replace(code,Character.toString((char) Integer.parseInt(code.replace("\\u+",""),16)));
			match = unicode.matcher(str);
		}
		return ChatColor.translateAlternateColorCodes('&',str);
	}
	
	static List<String> chatColors(List<String> list) {
		List<String> newList = new ArrayList<String>();
		for (String str : list) if (str != null)
			if (str.trim().isEmpty()) newList.add("");
			else newList.add(chatColors(str));
		return newList;
	}
	
	static void chatColors(CommandSender sender, String str) {
		sender.sendMessage(chatColors(str));
	}
	
	static void chatColors(Player Player, String str) {
		Player.sendMessage(chatColors(str));
	}
	
	static void chatColorsLogPlugin(String str) {
		Bukkit.getLogger().info(chatColorsPlugin(str));
	}
	
	static void chatLogPlugin(String str) {
		Bukkit.getLogger().info(chatColorsPlugin("") + str);
	}
	
	static String chatColorsPlugin(String str) {
		return chatColors("&d[" + Main.pluginNameColors + "&d]&r " + str);
	}

	static void chatColorsPlugin(CommandSender sender, String str) {
		sender.sendMessage(chatColorsPlugin(str));
	}
	
	static void chatColorsActionBar(Player player, String str) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.chatColors(str)));
	}
	
	static String chatColorsStrip(String str) {
		return ChatColor.stripColor(str);
	}
	
	static String chatColorsToString(String str, String colorCode) {
		return str.replace("§",colorCode);
	}
	
	static NamespacedKey namespacedKey(String name) {
		return new NamespacedKey(Main.main,name);
	}
	
	static boolean isNull(ItemStack item) {
		return item == null || item.getType().isAir();
	}
}