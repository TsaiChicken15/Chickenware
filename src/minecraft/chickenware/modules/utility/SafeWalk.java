package chickenware.modules.utility;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.utils.KeybindUtil;
import net.minecraft.entity.Entity;

public class SafeWalk extends Module 
{
	public SafeWalk() {super("SafeWalk", Keyboard.KEY_NONE, Category.UTILITY, false, "Prevents you from walking off the edge of blocks"); }
	public void onEnable() 
	{
		_gs.keyBindSneak.pressed = KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode()); 
	}
	public void onDisable() 
	{
		_gs.keyBindSneak.pressed = KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode()); 
	} 
	public void onEvent(Event e) 
	{
		if (e instanceof EventUpdate) 
		{		
			if(KeybindUtil.isDown(_gs.keyBindJump.getKeyCode()) && mc.thePlayer.movementInput.moveForward > 0) 
			{
				_gs.keyBindSneak.pressed = false;
			}
			for(double x = -0.25; x <= 0.25; x += 0.25) 
			{
				for(double z = -0.25; z <= 0.25; z += 0.25) 
				{
					if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x, 0.0D, z)).isEmpty() && 
							mc.thePlayer.onGround &&
							!KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode())) 
					{
						_gs.keyBindSneak.pressed = true;
						return;
					}
				}
			}
			if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.125D, 0.0D)).isEmpty() && 
					!KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode())) 
			{
				_gs.keyBindSneak.pressed = false;
			}
		}
	}
}

