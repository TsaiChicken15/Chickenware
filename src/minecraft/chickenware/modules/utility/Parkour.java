package chickenware.modules.utility;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.utils.MCHook;
import net.minecraft.entity.Entity;


public class Parkour extends Module implements MCHook
{
	public Parkour() {super("Parkour", Keyboard.KEY_NONE, Category.UTILITY, false, ""); }
	public void onEvent(Event e) 
	{
		if (e instanceof EventUpdate && e.isPRE())
		{
			if (!mc.thePlayer.isSneaking() && mc.thePlayer.onGround &&
					mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(0.0001D, 0.0D, 0.0001D)).isEmpty()) 
			{
				mc.thePlayer.jump();
			}
		}
	}
}
