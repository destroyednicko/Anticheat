package com.nicko.anticheat.util.message.mkremins.fanciful;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import com.nicko.anticheat.util.CC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Internal class: Represents a component of a JSON-serializable {@link FancyMessage}.
 */
final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable {

	CC color = CC.WHITE;
	ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
	String clickActionName = null, clickActionData = null, hoverActionName = null;
	JsonRepresentedObject hoverActionData = null;
	TextualComponent text = null;
	String insertionData = null;
	ArrayList<JsonRepresentedObject> translationReplacements = new ArrayList<JsonRepresentedObject>();

	MessagePart(final TextualComponent text) {
		this.text = text;
	}

	MessagePart() {
		this.text = null;
	}

	boolean hasText() {
		return text != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MessagePart clone() throws CloneNotSupportedException {
		MessagePart obj = (MessagePart) super.clone();
		obj.styles = (ArrayList<ChatColor>) styles.clone();
		if (hoverActionData instanceof JsonString) {
			obj.hoverActionData = new JsonString(((JsonString) hoverActionData).getValue());
		} else if (hoverActionData instanceof FancyMessage) {
			obj.hoverActionData = ((FancyMessage) hoverActionData).clone();
		}
		obj.translationReplacements = (ArrayList<JsonRepresentedObject>) translationReplacements.clone();
		return obj;

	}

	static final BiMap<ChatColor, String> stylesToNames;

	static {
		ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
		for (final ChatColor style : ChatColor.values()) {
			if (!style.isFormat()) {
				continue;
			}

			String styleName;
			switch (style) {
				case MAGIC:
					styleName = "obfuscated";
					break;
				case UNDERLINE:
					styleName = "underlined";
					break;
				default:
					styleName = style.name().toLowerCase();
					break;
			}

			builder.put(style, styleName);
		}
		stylesToNames = builder.build();
	}

	public void writeJson(JsonWriter json) {
		try {
			json.beginObject();
			text.writeJson(json);
			json.name("color").value(color.name().toLowerCase());
			for (final ChatColor style : styles) {
				json.name(stylesToNames.get(style)).value(true);
			}
			if (clickActionName != null && clickActionData != null) {
				json.name("clickEvent")
						.beginObject()
						.name("action").value(clickActionName)
						.name("value").value(clickActionData)
						.endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.name("hoverEvent")
						.beginObject()
						.name("action").value(hoverActionName)
						.name("value");
				hoverActionData.writeJson(json);
				json.endObject();
			}
			if (insertionData != null) {
				json.name("insertion").value(insertionData);
			}
			if (translationReplacements.size() > 0 && TextualComponent.isTranslatableText(text)) {
				json.name("with").beginArray();
				for (JsonRepresentedObject obj : translationReplacements) {
					obj.writeJson(json);
				}
				json.endArray();
			}
			json.endObject();
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
		}
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("text", text);
		map.put("styles", styles);
		map.put("color", color.getCode());
		map.put("hoverActionName", hoverActionName);
		map.put("hoverActionData", hoverActionData);
		map.put("clickActionName", clickActionName);
		map.put("clickActionData", clickActionData);
		map.put("insertion", insertionData);
		map.put("translationReplacements", translationReplacements);
		return map;
	}

	static {
		ConfigurationSerialization.registerClass(MessagePart.class);
	}

}
