package me.DMan16.ProfileGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayersData {
	private HashMap<String,Long> players;
	private String dir;
	
	public PlayersData() throws IOException {
		players = new HashMap<String,Long>();
		dir = "plugins/" + Main.pluginName + "/players";
		Files.createDirectories(Paths.get(dir), new FileAttribute[0]);
		for (File file : new File(dir).listFiles()) {
			try (InputStreamReader stream = new InputStreamReader(new FileInputStream(dir + "/" + file.getName()),"UTF-8")) {
				BufferedReader reader = new BufferedReader(stream);
				List<String> list = new ArrayList<String>();
				String line;
				while ((line = reader.readLine()) != null) if (!(line = line.trim()).isEmpty()) list.add(line.trim());
				String str = String.join("\n",list);
				try {
					long num = Long.parseLong(str);
					if (num < 0) throw new Exception();
					String ID = file.getName();
					players.put(ID,num);
					write(ID);
				} catch (Exception e) {
					file.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void write(String ID) {
		if (!players.containsKey(ID)) new File(dir,ID).delete();
		else {
			Path path = Paths.get(dir).resolve(ID);
			try {
				if (!Files.exists(path, new LinkOption[0])) {
					Files.createDirectories(path.getParent(), new FileAttribute[0]);
					Files.createFile(path, new FileAttribute[0]);
				}
				OutputStream fw = new FileOutputStream(dir + "/" + ID);
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(fw,"UTF-8"));
				pw.write(players.get(ID).toString()); 
				pw.flush(); 
				pw.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public long get(Player player) {
		if (players.containsKey(player.getUniqueId().toString())) return players.get(player.getUniqueId().toString());
		return 0;
	}
	
	public boolean update(Player player) {
		return update(player,1);
	}
	
	public boolean update(Player player, long amount) {
		if (amount <= 0) return false;
		String ID = player.getUniqueId().toString();
		if (!players.containsKey(ID)) players.put(ID,amount);
		else players.replace(ID,amount);
		write(ID);
		return true;
	}
	
	public boolean isEmpty() {
		return players == null || players.isEmpty();
	}
}