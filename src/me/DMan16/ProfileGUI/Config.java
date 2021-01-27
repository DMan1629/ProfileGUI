package me.DMan16.ProfileGUI;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Config extends ConfigLoader {
	private String commandUsePermission;
	private String enderChestPermission;
	private String craftingTablePermission;
	private boolean permissionsOp;
	private String menuFriendsButton;
	private String menuSkillsButton;
	private String menuBalanceButton;
	private String menuIslandButton;
	private String menuTimeButton;
	private String menuCloseButton;
	
	public final List<String> perms;
	public Config() throws IOException  {
		super("config.yml",Main.pluginName + " config file");
		readConfig();
		perms = Arrays.asList(commandUsePermission,enderChestPermission,craftingTablePermission);
	}
	
	public void readConfig() throws IOException {
		loadConfig();
		
		String permPrefix = Main.pluginName.toLowerCase();
		String[] startMSG = {"Please read each option's information and input type before changing","Default values are in []",
				"Any option that is filled wrong or removed will be replaced by the [default] value!",
				"Disabled permissions will still use the Op option if it is turned on","","",
				"Permissions - leave permissions blank to allow their specific functionality to everyone"," ",
				"commandUsePermission - the permission required to use the command [" + permPrefix + ".command]",
				"enderChestPermission - the permission required to have an Ender Chest in the menu [" + permPrefix + ".enderchest]",
				"craftingTablePermission - the permission required to have a Crafting Table in the menu [" + permPrefix + ".craftingtable]",
				"permissionsOp - do Op players have access to these permissions by default (true/false) [true]"};
		
		commandUsePermission  = ((String) addNewConfigOption(config,"commandUsePermission",permPrefix + ".command",startMSG)).toLowerCase();
		enderChestPermission = ((String) addNewConfigOption(config,"enderChestPermission",permPrefix + ".enderchest",null)).toLowerCase();
		craftingTablePermission = ((String) addNewConfigOption(config,"craftingTablePermission",permPrefix + ".craftingtable",null)).toLowerCase();
		permissionsOp = ((Boolean) addNewConfigOption(config,"permissionsOp",Boolean.valueOf(true),null)).booleanValue();

		String[] buttonsMSG = {"Buttons - names of buttons (with color codes)"," ",
				"menuFriendsButton - the button that opens the Friends menu Friends plugin [&aFriends]",
				"menuSkillsButton - the button that opens the Skills menu from Aurelium Skills plugin [&bAurelium &6Skills]",
				"menuBalanceButton - the button that displays the player's balance [&aBalance]",
				"menuIslandButton - the button that opens the Island menu from Fabled Skyblock plugin [&3Island]",
				"menuPlayerButton - the button displaying the player's information [&dPlayer]",
				"menuTimeButton - the button displaying the world's time [&eTime]",
				"menuCloseButton - the button that closes the menu [&cClose]"};
		
		menuFriendsButton = ((String) addNewConfigOption(config,"menuFriendsButton","&aFriends",buttonsMSG));
		menuSkillsButton = ((String) addNewConfigOption(config,"menuSkillsButton","&bAurelium &6Skills",null));
		menuBalanceButton = ((String) addNewConfigOption(config,"menuBalanceButton","&aBalance",null));
		menuIslandButton = ((String) addNewConfigOption(config,"menuIslandButton","&3Island",null));
		menuTimeButton = ((String) addNewConfigOption(config,"menuTimeButton","&eTime",null));
		menuCloseButton = ((String) addNewConfigOption(config,"menuCloseButton","&cClose",null));
		
		writeConfig();
	}
	
	public String commandUsePermission() {
		return commandUsePermission;
	}
	
	public String enderChestPermission() {
		return enderChestPermission;
	}
	
	public String craftingTablePermission() {
		return craftingTablePermission;
	}
	
	public boolean permissionsOp() {
		return permissionsOp;
	}
	
	public String menuFriendsButton() {
		return menuFriendsButton;
	}
	
	public String menuSkillsButton() {
		return menuSkillsButton;
	}
	
	public String menuBalanceButton() {
		return menuBalanceButton;
	}
	
	public String menuIslandButton() {
		return menuIslandButton;
	}
	
	public String menuTimeButton() {
		return menuTimeButton;
	}
	
	public String menuCloseButton() {
		return menuCloseButton;
	}
}