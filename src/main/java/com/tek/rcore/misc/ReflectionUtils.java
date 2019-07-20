package com.tek.rcore.misc;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;

public class ReflectionUtils {
	
	public static void applyProfile(SkullMeta skullMeta, GameProfile profile) throws ReflectiveOperationException {
		Class<?> craftMetaSkullClass = getCraftBukkitClass("inventory.CraftMetaSkull");
		Field profileField = craftMetaSkullClass.getDeclaredField("profile");
		profileField.setAccessible(true);
		profileField.set(skullMeta, profile);
		profileField.setAccessible(false);
	}
	
	public static CommandMap getCommandMap(Server server) throws ReflectiveOperationException {
		Class<?> craftServerClass = getCraftBukkitClass("CraftServer");
		Field commandMapField = craftServerClass.getDeclaredField("commandMap");
		return (CommandMap) commandMapField.get(server);
	}
	
	public static Class<?> getCraftBukkitClass(String path) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + path);
	}
	
	public static Class<?> getMinecraftServerClass(String path) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + getVersion() + "." + path);
	}
	
	public static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
	
}
