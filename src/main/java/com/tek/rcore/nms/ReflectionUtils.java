package com.tek.rcore.nms;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.tek.rcore.RedstoneCore;
import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.nms.ProtocolReflection.ConstructorInvoker;
import com.tek.rcore.nms.ProtocolReflection.FieldAccessor;
import com.tek.rcore.nms.ProtocolReflection.MethodInvoker;

/**
 * A class which provides a few
 * methods only available through
 * NMS or craftbukkit.
 * 
 * @author RedstoneTek
 */
public class ReflectionUtils {
	
	//Cached fields/classes/methods
	private static Class<Object> BLOCKPOSITION_CLASS = ProtocolReflection.getUntypedClass("{nms}.BlockPosition");
	private static Class<Object> NBTTAGCOMPOUND_CLASS = ProtocolReflection.getUntypedClass("{nms}.NBTTagCompound");
	private static ConstructorInvoker BLOCKPOSITION_CONSTRUCTOR = ProtocolReflection.getConstructor(BLOCKPOSITION_CLASS, int.class, int.class, int.class);
	private static ConstructorInvoker PACKETPLAYOUTOPENSIGNEDITOR_CONSTRUCTOR = ProtocolReflection.getConstructor("{nms}.PacketPlayOutOpenSignEditor", BLOCKPOSITION_CLASS);
	private static ConstructorInvoker PACKETPLAYOUTTILEENTITYDATA_CONSTRUCTOR = ProtocolReflection.getConstructor("{nms}.PacketPlayOutTileEntityData", BLOCKPOSITION_CLASS, int.class, NBTTAGCOMPOUND_CLASS);
	private static ConstructorInvoker NBTTAGCOMPOUND_CONSTRUCTOR = ProtocolReflection.getConstructor(NBTTAGCOMPOUND_CLASS);
	private static MethodInvoker SETSTRING_METHOD = ProtocolReflection.getMethod(NBTTAGCOMPOUND_CLASS, "setString", String.class, String.class);
	private static MethodInvoker SETINT_METHOD = ProtocolReflection.getMethod(NBTTAGCOMPOUND_CLASS, "setInt", String.class, int.class);
	private static FieldAccessor<Object> PROFILE_FIELD = ProtocolReflection.getField("{obc}.inventory.CraftMetaSkull", "profile", Object.class);
	
	/**
	 * Applies a GameProfile to SkullMeta.
	 * <p>NOTE: This is used in the {@link SkullFactory} class,
	 * chances are that what you're looking to do is already doable in there.</p>
	 * 
	 * @param skullMeta The SkullMeta object
	 * @param profile The GameProfile to apply
	 */
	public static void applyProfile(SkullMeta skullMeta, GameProfile profile) {
		PROFILE_FIELD.set(skullMeta, profile);
	}
	
	/**
	 * Opens a sign menu editor for the specified player.
	 * 
	 * @param player The player
	 * @param contents The sign's initial contents
	 * @return The location at which a sign block change was sent
	 */
	public static Location openSignEditor(Player player, String[] contents) {
		Location location = player.getLocation();
		Location signLocation = location.clone().subtract(0, 5, 0).getBlock().getLocation();
		Object bpSignLocation = BLOCKPOSITION_CONSTRUCTOR.invoke(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ());
		Object packetPlayOutOpenSignEditor = PACKETPLAYOUTOPENSIGNEDITOR_CONSTRUCTOR.invoke(bpSignLocation);
		Object signNbt = NBTTAGCOMPOUND_CONSTRUCTOR.invoke();
		SETSTRING_METHOD.invoke(signNbt, "Color", "black");
		SETSTRING_METHOD.invoke(signNbt, "id", "minecraft:sign");
		SETINT_METHOD.invoke(signNbt, "x", signLocation.getBlockX());
		SETINT_METHOD.invoke(signNbt, "y", signLocation.getBlockY());
		SETINT_METHOD.invoke(signNbt, "z", signLocation.getBlockZ());
		for(int i = 0; i < contents.length; i++) { SETSTRING_METHOD.invoke(signNbt, "Text" + (i + 1), "{\"text\":\"" + contents[i] + "\"}"); }
		Object packetPlayOutTileEntityData = PACKETPLAYOUTTILEENTITYDATA_CONSTRUCTOR.invoke(bpSignLocation, 9, signNbt);
		
		player.sendBlockChange(signLocation, Material.BIRCH_WALL_SIGN.createBlockData());
		RedstoneCore.getInstance().getProtocolImplementation().sendPacket(player, packetPlayOutOpenSignEditor);
		RedstoneCore.getInstance().getProtocolImplementation().sendPacket(player, packetPlayOutTileEntityData);
		
		return signLocation;
	}
	
}
