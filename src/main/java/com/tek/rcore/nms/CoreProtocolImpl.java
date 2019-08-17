package com.tek.rcore.nms;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.tek.rcore.misc.SignMenu;
import com.tek.rcore.nms.ProtocolReflection.MethodInvoker;

import io.netty.channel.Channel;

/**
 * A class using TinyProtocol in order
 * to capture packets for diverse utilities.
 * 
 * @author RedstoneTek
 */
public class CoreProtocolImpl extends TinyProtocol {

	//Cached fields/classes/methods
	private static Class<Object> PACKETPLAYINUPDATESIGN_CLASS = ProtocolReflection.getUntypedClass("{nms}.PacketPlayInUpdateSign");
	private static MethodInvoker C_GET_LINES_METHOD = ProtocolReflection.getTypedMethod(PACKETPLAYINUPDATESIGN_CLASS, "c", String[].class);
	
	/**
	 * Creates a core protocol implementation instance.
	 * 
	 * @param plugin The plugin
	 */
	public CoreProtocolImpl(Plugin plugin) {
		super(plugin);
	}
	
	/**
	 * Invoked when the server has received a packet from a given player.
	 * <p>
	 * Use {@link Channel#remoteAddress()} to get the remote address of the client.
	 * 
	 * @param sender - the player that sent the packet, NULL for early login/status packets.
	 * @param channel - channel that received the packet. Never NULL.
	 * @param packet - the packet being received.
	 * @return The packet to recieve instead, or NULL to cancel.
	 */
	@Override
	public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
		if(packet.getClass().equals(PACKETPLAYINUPDATESIGN_CLASS)) {
			String[] lines = (String[]) C_GET_LINES_METHOD.invoke(packet);
			
			Iterator<SignMenu> signMenuIterator = SignMenu.activeMenus.iterator();
			while(signMenuIterator.hasNext()) {
				SignMenu menu = signMenuIterator.next();
				if(menu.getPlayer().equals(sender)) {
					menu.finish(lines);
					signMenuIterator.remove();
				}
			}
		}
		
		return super.onPacketInAsync(sender, channel, packet);
	}

}
