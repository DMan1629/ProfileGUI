package me.DMan16.ProfileGUI;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor,TabCompleter {
	public CommandListener() {
		PluginCommand command = Main.main.getCommand("profilegui");
		command.setExecutor(this);
		command.setTabCompleter(this);
		command.setDescription(Utils.chatColors(Main.pluginNameColors + " &fcommand"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player) && !Main.PermissionsManager.hasPermission((Player) sender,Main.Config.commandUsePermission(),Main.Config.permissionsOp()))
			return false;
		if (args.length == 0) commands(sender);
		else if (args[0].equalsIgnoreCase("config")) displayConfig(sender);
		else if (args[0].equalsIgnoreCase("reload")) reloadConfig(sender);
		else commands(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if ((sender instanceof Player) && !Main.PermissionsManager.hasPermission((Player) sender,Main.Config.commandUsePermission(),Main.Config.permissionsOp()))
			return null;
		List<String> base = Arrays.asList("config","reload");
		List<String> reusltList = new ArrayList<String>();
		for (String cmd : base) if (contains(args[0],cmd)) reusltList.add(cmd);
		return reusltList;
	}
	
	private void commands(CommandSender sender) {
		Utils.chatColorsPlugin(sender,"&fAvailable commands:\n" + Main.pluginNameColors + " &econfig" +
				" &b- &fdisplay the config options\n" + Main.pluginNameColors + " &ereload &b- &freload the config");
	}

	private void displayConfig(CommandSender sender) {
		HashMap<String,Method> methods = new HashMap<String,Method>();
		for (Method method : Main.Config.getClass().getDeclaredMethods()) {
			if (Modifier.isPublic(method.getModifiers())) methods.put(method.getName(),method);
		}
		List<String> config = new ArrayList<String>();
		for (Field field : Main.Config.getClass().getDeclaredFields()) {
			boolean isString = field.getGenericType().getTypeName().toLowerCase().contains("string");
			try {
				config.add("&a" + field.getName() + "&f: " + (isString ? "\"" : "") + methods.get(field.getName()).invoke(Main.Config) + (isString ? "&f\"" : ""));
			} catch (Exception e) {}
		}
		Utils.chatColors(sender,String.join("\n",config));
	}
	
	private void reloadConfig(CommandSender sender) {
		try {
			Main.Config.readConfig();
			Main.PermissionsManager.registerPermissions();
			Utils.chatColorsPlugin(sender,"&aconfig reloaded!");
		} catch (IOException e) {
			Utils.chatColorsPlugin(sender,"&cunable to reload config!");
		}
	}
	
	private boolean contains(String arg1, String arg2) {
		return (arg1 == null || arg1.isEmpty() || arg2.toLowerCase().contains(arg1.toLowerCase()));
	}
}