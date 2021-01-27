package me.DMan16.ProfileGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
	private List<String> permissions;
	
	public Permissions() {
		this.permissions = new ArrayList<String>();
		registerPermissions();
	}
	
	public boolean hasPermission(Player player, String perm, boolean includeOP) {
		if (perm == null || perm.trim().isEmpty()) return true;
		Permission permission = getPermission(perm);
		return ((includeOP && player.isOp()) || (permission != null && player.hasPermission(permission)));
	}
	
	private Permission getPermission(String perm) {
		if (perm == null || perm.isEmpty()) return null;
		return new Permission(perm.toLowerCase(),"",PermissionDefault.FALSE);
	}
	
	public void registerPermissions() {
		unregisterPermissions();
		Server server = Main.main.getServer();
		for (String perm : Main.Config.perms) if (perm != null && !perm.isEmpty()) {
			Permission permission = getPermission(perm);
			if (permission == null) continue;
			try {
				server.getPluginManager().addPermission(permission);
				this.permissions.add(perm);
			} catch (Exception e) {}
		}
	}
	
	public void unregisterPermissions() {
		Server server = Main.main.getServer();
		for (String perm : permissions) {
			try {
				server.getPluginManager().removePermission(getPermission(perm));
			} catch (Exception e) {}
		}
		this.permissions = new ArrayList<String>();
	}
}