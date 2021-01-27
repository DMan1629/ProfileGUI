package me.DMan16.ProfileGUI;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("unchecked")
public class ConfigOption<V> {
	private final FileConfiguration config;
	private final String option;
	private V value;
	private final V defaultValue;
	private final String[] comment;
	
	public ConfigOption(FileConfiguration config, String optionName, V defaultValue, String[] comment, boolean useDefault) {
		this.config = config;
		this.option = optionName;
		this.defaultValue = defaultValue;
		this.comment = comment;
		if (useDefault) value = defaultValue;
		else setValue();
	}
	
	private void setValue() {
		try {
			value = (V) config.get(option,defaultValue);
			if (value.getClass() == Integer.class) if ((Integer) value < 0) throw new Exception();
			if (value.getClass() == Double.class) if ((Double) value < 0.0) throw new Exception();
		} catch (Exception e) {
			if (config != null) Bukkit.getLogger().log(Level.WARNING,"Config: Failed to read value of: \"" + option + "\"! Using default value instead!");
			value = defaultValue;
		}
	}
	
	public void setValue(V v) {
		try {
			value = (V) config.get(option,v);
			if (value.getClass() == Integer.class) if ((Integer) value <= 0) throw new Exception();
			if (value.getClass() == Double.class) if ((Double) value < 0.0) throw new Exception();
		} catch (Exception e) {
			if (config != null) Bukkit.getLogger().log(Level.WARNING,"Config: Failed to read value of: \"" + option + "\"! Using default value instead!");
			value = v;
		}
	}
	
	public V getValue() {
		return (V) value;
	}
	
	public String[] getComment() {
		return comment;
	}
	
	@Override
	public String toString() {
		String string = "";
		if (comment != null) for (String comLine : comment) if (!comLine.isEmpty()) string = string + "# " + comLine + "\n";
		string += option + ": ";
		if (value instanceof List) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n");
			for (int index = 0; index < ((List<?>) value).size(); index++) {
				String val = ((List<?>) value).get(index).toString();
				if (((List<?>) value).get(index) instanceof Map) {
					int i = 0;
					for (Entry<?,?> entry : ((Map<?,?>) ((List<?>) value).get(index)).entrySet()) {
						builder.append("  " + (i == 0 ? "-" : " ") + " " + entry.getKey() + ": " + entry.getValue() +
								((i == ((Map<?,?>) ((List<?>) value).get(index)).size() - 1) && (index == ((List<?>) value).size() - 1) ? "" : "\n"));
						i++;
					}
				} else if (val.contains("\n")) {
					String[] vals = val.split("\n");
					if (vals.length == 1) builder.append("  - " + (index == ((List<?>) value).size() - 1 ? "" : "\n"));
					else {
						builder.append("  - " + vals[0] + "\n");
						for (int i = 1; i < vals.length; i++) builder.append("    " + vals[i] + ((i == vals.length - 1) && (index == ((List<?>) value).size() - 1) ? "" :
							"\n"));
					}
				} else builder.append("  - " + val + (index == ((List<?>) value).size() - 1 ? "" : "\n"));
			}
			string += builder.toString();
		} else if (value instanceof Map) {
			Map<?,?> tempMap = (Map<?,?>) value;
			StringBuilder builder = new StringBuilder();
			builder.append("\n");
			for (Entry<?,?> entry : tempMap.entrySet()) builder.append("  - " + entry.getKey() + ": " + entry.getValue());
			string += builder.toString();
		} else if (value.getClass().isAssignableFrom(String.class)) {
			String val = value.toString();
			if (val.contains("\n")) {
				String[] vals = val.split("\n");
				if (vals.length == 1) string += val;
				else {
					StringBuilder builder = new StringBuilder();
					builder.append("  - '" + vals[0] + "'");
					for (int i = 1; i < vals.length; i++) builder.append("    '" + vals[i] + (i == vals.length - 1 ? "'" : "'\n"));
					string += builder.toString();
				}
			} else string += "'" + value.toString() + "'";
		} else {
			String val = value.toString();
			if (val.contains("\n")) {
				String[] vals = val.split("\n");
				if (vals.length == 1) string += val;
				else {
					StringBuilder builder = new StringBuilder();
					builder.append("  - " + vals[0]);
					for (int i = 1; i < vals.length; i++) builder.append("    " + vals[i] + (i == vals.length - 1 ? "" : "\n"));
					string += builder.toString();
				}
			} else string += val;
		}
		return string;
	}
}