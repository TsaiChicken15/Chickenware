package chickenware.modules.utility;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.blatant.Scaffold;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;

public class AntiVoid extends Module implements MCHook
{
	public AntiVoid() 
	{
		super("AntiVoid", Keyboard.KEY_NONE, Category.UTILITY, false, "");
		this.addSettings(modeValue,autoScaffoldValue,heightValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","NoMotion","NoMotion","VClip");
	public static BooleanSetting autoScaffoldValue = new BooleanSetting("AutoScaffold",true);
	public static NumberSetting heightValue = new NumberSetting("Height", 3, 1, 10, 1);
	boolean tried = false;
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = modeValue.get();
		}
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate)
		{
			if(mc.thePlayer.fallDistance > heightValue.get() && checkVoid() && !tried)
			{
				if(modeValue.is("NoMotion"))
				{
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionY = 0;
					mc.thePlayer.motionZ = 0;
					if(Client.isEnabled("Scaffold") == null)
					{
						Client.getModuleByName("Scaffold").toggled = true;
						Client.getModuleByName("Scaffold").onEnable();
					}
				}
				else if(modeValue.is("VClip"))
				{
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + heightValue.get() + 2, mc.thePlayer.posZ);
					tried = true;
					if(Client.isEnabled("Scaffold") == null)
					{
						Client.getModuleByName("Scaffold").toggled = true;
						Client.getModuleByName("Scaffold").onEnable();
					}
				}
			}
			if(mc.thePlayer.onGround || mc.thePlayer.isCollidedVertically)
			{
				tried = false;
				if(Client.isEnabled("Scaffold") != null)
				{
					Client.getModuleByName("Scaffold").toggled = false;
					Client.getModuleByName("Scaffold").onDisable();
				}
			}
		}
	}
	private boolean checkVoid()
	{
		double i = (-(mc.thePlayer.posY-1.4857625));
	    boolean dangerous = true;
	    while (i <= 0) {
			dangerous = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX * 0.5, i, mc.thePlayer.motionZ * 0.5)).isEmpty();
			i++;
			if (!dangerous) break;
	    }
		return dangerous;
	}
}