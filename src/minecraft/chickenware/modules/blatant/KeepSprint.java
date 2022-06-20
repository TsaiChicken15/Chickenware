package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.utils.MCHook;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class KeepSprint extends Module implements MCHook
{
	public KeepSprint() 
	{
		super("KeepSprint", Keyboard.KEY_NONE, Category.BLATANT, false, "¡±rPrevents you from losing sprint while attacking");
	}
	public void onEvent(Event e) 
	{
	    if(e instanceof EventPacket) 
	    {
	    	if(((EventPacket)e).packet instanceof C0BPacketEntityAction) 
	    	{
	    		if(((C0BPacketEntityAction)(((EventPacket)e).packet)).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING)
	    		{
	    			e.cancelEvent();
	    		}
	    	}
	    }
	}
}