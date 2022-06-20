package chickenware.modules.other;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.utils.MCHook;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module implements MCHook
{
	public NoRotate() 
	{
		super("NoRotate", Keyboard.KEY_NONE, Category.OTHER, false, "");
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventPacket && mc.thePlayer != null) 
		{
			if((((EventPacket)e).packet instanceof S08PacketPlayerPosLook))
			{
				Packet packet = ((EventPacket)e).packet;
	        	S08PacketPlayerPosLook p = (S08PacketPlayerPosLook) packet;
	            p.field_148936_d = mc.thePlayer.rotationYaw;
	            p.field_148937_e = mc.thePlayer.rotationPitch;
			}
		}
	}
}