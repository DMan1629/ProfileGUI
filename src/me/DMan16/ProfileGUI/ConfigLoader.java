package me.DMan16.ProfileGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;

@SuppressWarnings("rawtypes")
public abstract class ConfigLoader {
	protected ArrayList<ConfigOption<?>> configOptionsList;
	private File file;
	private final String fileName;
	private final String header;
	protected FileConfiguration config;
	
	public ConfigLoader(String fileName, String header) {
		this.fileName = fileName;
		this.header = header;
		loadConfig();
	}
	
	public void loadConfig() {
		this.file = null;
		this.file = new File(Main.main.getDataFolder(),fileName);
		InputStreamReader input;
		configOptionsList = null;
		configOptionsList = new ArrayList<ConfigOption<?>>();
		config = null;
		try {
			if (!this.file.exists()) {
				if (!this.file.getParentFile().exists()) this.file.getParentFile().mkdir();
				this.file.createNewFile();
			}
			input = new InputStreamReader(new FileInputStream(this.file),Charsets.UTF_8);
			config = YamlConfiguration.loadConfiguration(input);
		} catch (Exception e) {
			e.printStackTrace();
			config = new YamlConfiguration();
		}
	}
	
	protected <T> T addNewConfigOption(FileConfiguration config, String optionName, T defaultValue, String[] comment) {
		return addNewConfigOption(config,optionName,defaultValue,comment,false);
	}
	
	protected <T> T addNewConfigOption(FileConfiguration config, String optionName, T defaultValue, String[] comment, boolean useDefault) {
		ConfigOption<T> option = new ConfigOption<T>(config,optionName,defaultValue,comment,useDefault);
		configOptionsList.add(option);
		return (T) option.getValue();
	}
	
	protected void writeConfig() throws IOException {
		File dataFolder = Main.main.getDataFolder();
		if (!dataFolder.exists()) dataFolder.mkdir();
		if (!file.exists()) file.createNewFile();
		else {
			file.delete();
			file.createNewFile();
		}
		OutputStream fw = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(fw,"UTF-8"));
		if (header != null) pw.print("## " + header + "\n\n");
		for (int i = 0; i < configOptionsList.size(); i++) 
			pw.print((i == 0 ? "" : "\n") + ((ConfigOption) configOptionsList.get(i)).toString() + ((i == configOptionsList.size() - 1) ||
					((i < configOptionsList.size() - 1) && (((ConfigOption) configOptionsList.get(i + 1)).getComment() == null)) ? "" : "\n"));
		pw.flush();
		pw.close();
	}
}