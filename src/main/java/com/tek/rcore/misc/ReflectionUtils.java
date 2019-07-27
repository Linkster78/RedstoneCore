package com.tek.rcore.misc;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;

/**
 * A class which provides a few
 * methods only available through
 * NMS or craftbukkit.
 * 
 * @author RedstoneTek
 */
public class ReflectionUtils {
	
	/**
	 * Applies a GameProfile to SkullMeta.
	 * <p>NOTE: This is used in the {@link SkullFactory} class,
	 * chances are that what you're looking to do is already doable in there.</p>
	 * 
	 * @param skullMeta The SkullMeta object
	 * @param profile The GameProfile to apply
	 * @throws ReflectiveOperationException Thrown if an issue arises while using Reflection
	 */
	public static void applyProfile(SkullMeta skullMeta, GameProfile profile) throws ReflectiveOperationException {
		Class<?> craftMetaSkullClass = getCraftBukkitClass("inventory.CraftMetaSkull");
		Field profileField = craftMetaSkullClass.getDeclaredField("profile");
		profileField.setAccessible(true);
		profileField.set(skullMeta, profile);
		profileField.setAccessible(false);
	}
	
	/**
	 * Fetches the server command map which
	 * contains default commands as well as
	 * plugin added commands.
	 * 
	 * @param server The Server instance
	 * @return The Server CommandMap
	 * @throws ReflectiveOperationException Thrown if an issue arises while using Reflection
	 */
	public static CommandMap getCommandMap(Server server) throws ReflectiveOperationException {
		Class<?> craftServerClass = getCraftBukkitClass("CraftServer");
		Field commandMapField = craftServerClass.getDeclaredField("commandMap");
		return (CommandMap) commandMapField.get(server);
	}
	
	/**
	 * Returns a craftbukkit class from it's relative path.
	 * 
	 * @param path The class relative path
	 * @return The craftbukkit class
	 * @throws ClassNotFoundException Thrown if the class does not exist
	 */
	public static Class<?> getCraftBukkitClass(String path) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + path);
	}
	
	/**
	 * Returns a minecraft server class from it's relative path.
	 * 
	 * @param path The class relative path
	 * @return The minecraft server class
	 * @throws ClassNotFoundException Thrown if the class does not exist
	 */
	public static Class<?> getMinecraftServerClass(String path) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + getVersion() + "." + path);
	}
	
	/**
	 * Returns the version's name, used in the package naming.
	 * Example: org.bukkit.craftbukkit.VERSION.someClass
	 * 
	 * @return The version name
	 */
	public static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
	
}
