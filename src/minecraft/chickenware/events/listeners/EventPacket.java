package chickenware.events.listeners;

import chickenware.events.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event<EventPacket>
{
	public Packet packet;
	public EventPacket(Packet packet) 
	{
		this.packet = packet;
	}
	public Packet getPacket() 
	{
		return packet;
	}
}