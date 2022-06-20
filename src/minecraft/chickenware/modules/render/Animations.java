package chickenware.modules.render;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.ModeSetting;
import chickenware.utils.AnimationUtil;
import chickenware.utils.MCHook;
import net.minecraft.util.MovingObjectPosition;

public class Animations extends Module implements MCHook
{
	public Animations() 
	{
		super("Animations", Keyboard.KEY_NONE, Category.RENDER, false, "");
		this.addSettings(modeValue);
		this.toggled = true;
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Tap","None","1.7","Tap");
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate)
		{
			if(!modeValue.is("None"))
			{
				if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && _gs.keyBindAttack.getIsKeyPressed())
				{
					if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= mc.thePlayer.getArmSwingAnimationEnd() / 2 || mc.thePlayer.swingProgressInt < 0)
			        {
						mc.thePlayer.swingProgressInt = -1;
						mc.thePlayer.isSwingInProgress = true;
			        }
				}
			}
		}
	}
	public static void renderAnimation(float f)
	{
		if(modeValue.is("None"))
		{
			AnimationUtil.animNone(f);
		}
		else if(modeValue.is("1.7"))
		{
			AnimationUtil.anim18(f);
		}
		else if(modeValue.is("Tap"))
		{
			AnimationUtil.animTap(f);
		}
	}
}