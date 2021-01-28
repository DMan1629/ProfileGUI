package me.DMan16.ProfileGUI;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class MainListener implements Listener {
	String menuName = "&b&lSkyBlock &8&lMenu";
	private ItemStack menuItem;
	private final int menuLines = 6;
	private final int menuFriends = 13;
	private final int menuCompass = 18;
	private final int menuSkills = 21;
	private final int menuBalance = 22;
	private final int menuIsland = 23;
	private final int menuPlayer = 26;
	private final int menuServer = 29;
	private final int menuClock = 33;
	private final int menuEnderChest = 37;
	private final int menuCraftingTable = 43;
	private final int menuClose = 49;
	
	public MainListener() {
		menuItem = getItem(Material.NETHER_STAR,menuName);
		menuItem.addUnsafeEnchantment(Enchantment.DURABILITY,1);
		ItemMeta meta = menuItem.getItemMeta();
		meta.getPersistentDataContainer().set(Utils.namespacedKey("creativecraft_skyblock"),PersistentDataType.STRING,menuName);
		menuItem.setItemMeta(meta);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onMenuClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if (!menuName.equals(Utils.chatColorsToString(event.getView().getTitle(),"&"))) return;
		event.setCancelled(true);
		if (slot >= menuLines * 9) return;
		if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;
		Inventory inv = event.getInventory();
		ItemStack item = inv.getItem(slot);
		if (Utils.isNull(item)) return;
		Player player = (Player) event.getWhoClicked();
		if (slot == menuClose) player.closeInventory();
		else if (slot == menuFriends) Bukkit.dispatchCommand(player,"friends");
		else if (slot == menuSkills) Bukkit.dispatchCommand(player,"skills");
		else if (slot == menuIsland) Bukkit.dispatchCommand(player,"island");
		else if (slot == menuEnderChest) player.openInventory(player.getEnderChest());
		else if (slot == menuCraftingTable) player.openInventory(Bukkit.createInventory(player,InventoryType.WORKBENCH));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMenuItemClickCreative(InventoryCreativeEvent event) {
		ItemStack cursor = event.getCursor().clone();
		cursor.setAmount(1);
		if (cursor.equals(menuItem)) event.setCursor(null);
		while (event.getWhoClicked().getInventory().contains(menuItem)) event.getWhoClicked().getInventory().remove(menuItem);
		event.getWhoClicked().getInventory().setItem(8,menuItem);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMenuItemClick(InventoryClickEvent event) {
		if ((event.getClick() == ClickType.NUMBER_KEY && event.getHotbarButton() == 8) || (event.getClickedInventory() != null &&
				event.getClickedInventory().getType() == InventoryType.PLAYER && event.getSlot() == 8)) event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMenuItemDrop(PlayerDropItemEvent event) {
		if (event.isCancelled()) return;
		if (event.getItemDrop().getItemStack().equals(menuItem)) event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMenuItemSwap(PlayerSwapHandItemsEvent event) {
		if (event.isCancelled()) return;
		if (event.getOffHandItem().equals(menuItem)) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteractMenu(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
		if (!event.hasItem() || Utils.isNull(event.getItem())) return;
		if (!event.getItem().equals(menuItem)) return;
		event.setCancelled(true);
		event.setUseInteractedBlock(Result.DENY);
		event.setUseItemInHand(Result.DENY);
		openMenu(event.getPlayer());
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		while (event.getPlayer().getInventory().contains(menuItem)) event.getPlayer().getInventory().remove(menuItem);
		event.getPlayer().getInventory().setItem(8,menuItem);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		while (event.getPlayer().getInventory().contains(menuItem)) event.getPlayer().getInventory().remove(menuItem);
		event.getPlayer().getInventory().setItem(8,menuItem);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		while (event.getPlayer().getInventory().contains(menuItem)) event.getPlayer().getInventory().remove(menuItem);
		event.getPlayer().getInventory().setItem(8,menuItem);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.getDrops().remove(menuItem);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled() || event.getPlayer() == null || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		Main.PlayersDataManager.update(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommandPlayer(PlayerCommandPreprocessEvent event) {
		String cmd = event.getMessage().toLowerCase().replace("/","");
		String[] args = cmd.split(" ");
		if (!args[0].toLowerCase().startsWith("clear") && !args[0].toLowerCase().startsWith("clean")) return;
		Player tempPlayer = null;
		if (args.length == 1) tempPlayer = event.getPlayer();
		else if (args[1].startsWith("*")) {
			new BukkitRunnable() {
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().setItem(8,menuItem));
				}
			}.runTaskLater(Main.main,5);
			return;
		} else tempPlayer = Bukkit.getServer().getPlayer(args[1]);
		if (tempPlayer == null) return;
		final Player player = tempPlayer;
		new BukkitRunnable() {
			public void run() {
				player.getInventory().setItem(8,menuItem);
			}
		}.runTaskLater(Main.main,5);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(ServerCommandEvent event) {
		String cmd = event.getCommand().toLowerCase().replace("/","");
		String[] args = cmd.split(" ");
		if (!args[0].toLowerCase().startsWith("clear") && !args[0].toLowerCase().startsWith("clean")) return;
		Player tempPlayer = null;
		if (args.length == 1) return;
		if (args[1].startsWith("*")) {
			new BukkitRunnable() {
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().setItem(8,menuItem));
				}
			}.runTaskLater(Main.main,5);
			return;
		}
		tempPlayer = Bukkit.getServer().getPlayer(args[1]);
		if (tempPlayer == null) return;
		final Player player = tempPlayer;
		new BukkitRunnable() {
			public void run() {
				player.getInventory().setItem(8,menuItem);
			}
		}.runTaskLater(Main.main,5);
	}
	
	private void updateInventory(Inventory inv, Player player) {
		String rank = null;
		Permission perm = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		Chat chat = (Chat) Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
		if (perm != null) {
			rank = perm.getPrimaryGroup(player);
			if (chat != null) {
				String prefix = chat.getGroupPrefix(player.getWorld(),rank);
				String suffix = chat.getGroupSuffix(player.getWorld(),rank);
				rank = (prefix == null ? "" : prefix) + rank + (suffix == null ? "" : suffix);
			}
			rank = Utils.chatColorsToString(rank,"&");
		}
		inv.clear();
		ItemStack item = getItem(Material.PLAYER_HEAD,Main.Config.menuFriendsButton());
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		skullMeta.setOwningPlayer(player);
		item.setItemMeta(skullMeta);
		inv.setItem(menuFriends,item);
		inv.setItem(menuCompass,getItem(Material.COMPASS,null));
		item = getItem(Material.DIAMOND_SWORD,Main.Config.menuSkillsButton());
		item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
		inv.setItem(menuSkills,item);
		inv.setItem(menuBalance,getItem(Material.EMERALD,Main.Config.menuBalanceButton(),Arrays.asList("",
				Utils.chatColorsToString(Main.economy.format(Double.parseDouble((new DecimalFormat("0.00")).format(Main.economy.getBalance(player)))),"&"))));
		inv.setItem(menuIsland,worldItem(player.getWorld().getEnvironment()));
		inv.setItem(menuPlayer,getItem(Material.NAME_TAG,Utils.chatColorsToString(player.getDisplayName() == null ? player.getName() :
			player.getDisplayName(),"&"),Arrays.asList("",rank == null ? "" : "&fRank: " + rank + "\n","&fBlocks mined: &e" + Main.PlayersDataManager.get(player),
					"&fChests opened: &e" + (player.getStatistic(Statistic.OPEN_BARREL) + player.getStatistic(Statistic.CHEST_OPENED) +
							player.getStatistic(Statistic.ENDERCHEST_OPENED) + player.getStatistic(Statistic.SHULKER_BOX_OPENED)))));
		inv.setItem(menuServer,getItem(Material.BOOK,"&3&lCreativeCraft &b&lSkyBlock",Arrays.asList("","&fPlayers: &a" + Bukkit.getServer().getOnlinePlayers().size() +
				"&e/&b" + Bukkit.getServer().getMaxPlayers())));
		inv.setItem(menuClock,getItem(Material.CLOCK,Main.Config.menuTimeButton(),Arrays.asList("","&fTime: &e" + player.getWorld().getTime() + " &cticks")));
		if (Main.PermissionsManager.hasPermission(player,Main.Config.enderChestPermission(),Main.Config.permissionsOp()))
			inv.setItem(menuEnderChest,getItem(Material.ENDER_CHEST,null));
		if (Main.PermissionsManager.hasPermission(player,Main.Config.craftingTablePermission(),Main.Config.permissionsOp()))
			inv.setItem(menuCraftingTable,getItem(Material.CRAFTING_TABLE,null));
		inv.setItem(menuClose,getItem(Material.BARRIER,Main.Config.menuCloseButton()));
	}
	
	private <T,Z> ItemStack getItem(Material material, String name) {
		return getItem(material,name,null);
	}
	
	private <T,Z> ItemStack getItem(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (name != null) meta.setDisplayName(Utils.chatColors(name));
		meta.addItemFlags(ItemFlag.values());
		if (lore != null) meta.setLore(Utils.chatColors(lore));
		item.setItemMeta(meta);
		return item;
	}

	private void openMenu(Player player) {
		Inventory inv = Bukkit.createInventory(player,menuLines * 9,Utils.chatColors(menuName));
		player.closeInventory();
		updateInventory(inv,player);
		player.openInventory(inv);
	}
	
	public ItemStack worldItem(Environment type) {
		ItemStack item = getItem(Material.PLAYER_HEAD,Main.Config.menuIslandButton());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		String skin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM0NjdhNTMxOTc4ZDBiOGZkMjRmNTYyODVjNzI3MzRkODRm" +
				"NWVjODhlMGI0N2M0OTMyMzM2Mjk3OWIzMjNhZiJ9fX0=";
		if (type == Environment.NETHER) skin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgzNTcxZmY1ODlmMWE1OWJ" +
				"iMDJiODA4MDBmYzczNjExNmUyN2MzZGNmOWVmZWJlZGU4Y2YxZmRkZSJ9fX0=";
		else if (type == Environment.THE_END) skin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjYWM1OWIyYWFlN" +
				"Dg5YWEwNjg3YjVkODAyYjI1NTVlYjE0YTQwYmQ2MmIyMWViMTE2ZmE1NjljZGI3NTYifX19";
		try {
			Method metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile",GameProfile.class);
			metaSetProfileMethod.setAccessible(true);
			GameProfile profile = new GameProfile(UUID.randomUUID(),"");
			profile.getProperties().put("textures", new Property("textures",skin));
			metaSetProfileMethod.invoke(meta,profile);
		} catch (Exception e) {}
		item.setItemMeta(meta);
		return item;
	}
}