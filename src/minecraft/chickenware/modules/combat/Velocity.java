package chickenware.modules.combat;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module implements MCHook
{
	public Velocity() 
	{
	    super("Velocity", Keyboard.KEY_NONE, Category.COMBAT, false, "Reduces knockback");
	    this.addSettings(modeValue,horizontalValue,verticalValue,chanceValue,spoofValue,onlyAirValue,disableInWaterValue);	
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Simple","Simple");
	public static NumberSetting horizontalValue = new NumberSetting("Horizontal", 100, 0, 100, 1);
	public static NumberSetting verticalValue = new NumberSetting("Vertical", 100, 0, 100, 1);
	public static NumberSetting chanceValue = new NumberSetting("Chance", 100, 1, 100, 1);
	public static BooleanSetting onlyAirValue = new BooleanSetting("AirOnly",false);
	public static BooleanSetting disableInWaterValue = new BooleanSetting("NoWater",false);
	public static BooleanSetting spoofValue = new BooleanSetting("Spoof",false);
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(horizontalValue.get()) + ", " + String.valueOf(verticalValue.get());
		}
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventPacket && Client.nullCheck() && ((EventPacket)e).packet instanceof S12PacketEntityVelocity && e.isINCOMING() &&
				mc.theWorld.getEntityByID(((S12PacketEntityVelocity)(((EventPacket)e).packet)).field_149417_a) == mc.thePlayer) 
		{
			if((onlyAirValue.get() && mc.thePlayer.onGround) || (mc.thePlayer.isInWater() && disableInWaterValue.get()))
			{
				return;
			}
	    	if(modeValue.is("Simple")) 
	    	{
	    		double h = horizontalValue.get();
	    		double v = verticalValue.get();
	    		
	    		if (h == 0F && v == 0F) 
	    		{
	    			e.cancelEvent();
                }
	    		
				((S12PacketEntityVelocity)(((EventPacket)e).packet)).x *= h / 100; 
	            ((S12PacketEntityVelocity)(((EventPacket)e).packet)).y *= v / 100;
	            ((S12PacketEntityVelocity)(((EventPacket)e).packet)).z *= h / 100;
	            
	            if(spoofValue.get())
	            {
	            	mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + ((S12PacketEntityVelocity)(((EventPacket)e).packet)).x / 8000.0, 
	            			mc.thePlayer.posY + ((S12PacketEntityVelocity)(((EventPacket)e).packet)).y / 8000.0, 
	            			mc.thePlayer.posZ + ((S12PacketEntityVelocity)(((EventPacket)e).packet)).z / 8000.0, false));
	            }
	    	}
	    }
	}
}