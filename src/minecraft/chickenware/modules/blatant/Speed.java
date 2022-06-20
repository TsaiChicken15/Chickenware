package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventMove;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.utils.MCHook;
import chickenware.utils.MovementUtil;

public class Speed extends Module implements MCHook
{
	public Speed() 
	{
		super("Speed", Keyboard.KEY_B, Category.BLATANT, true, "");
		this.addSettings(modeValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Verus","Strafe","Verus");
	boolean falling, state;
	public void onEnable() 
	{
		falling = false;
		state = false;
	}
	public void onDisable() 
	{
		mc.thePlayer.speedInAir = 0.02f;
		mc.timer.timerSpeed = 1f;
	}
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = modeValue.get();
		}
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate && e.isPRE()) 
		{
			if(mc.thePlayer.isInWater()) 
			{
				return;
			}
			if (MovementUtil.isMovementInputing()) 
			{
		        if (modeValue.is("Strafe")) 
		        {
		        	if (mc.thePlayer.onGround) 
		        	{
		        		mc.thePlayer.jump();
		        	}
		        	else 
		        	{
		        		MovementUtil.strafe(); 
		        	}
		        } 
		        else if(modeValue.is("Verus"))
		        {
		        	if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) 
		        	{
		                if (MovementUtil.isMoving()) 
		                {
		                    //mc.gameSettings.keyBindJump.pressed = false;
		                    if (mc.thePlayer.onGround) 
		                    {
		                        mc.thePlayer.jump();
		                        MovementUtil.strafe(0.48F);
		                    }
		                    MovementUtil.strafe();
		                }
		            }
				} 
		    } 
			else 
		    {
		        mc.thePlayer.motionX = 0.0D;
		        mc.thePlayer.motionZ = 0.0D;
		    } 
		}
	}
}