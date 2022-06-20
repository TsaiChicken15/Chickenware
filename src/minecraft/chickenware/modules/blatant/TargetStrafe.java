package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventMove;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.NumberSetting;
import chickenware.utils.CombatUtil;
import chickenware.utils.MCHook;
import chickenware.utils.MovementUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class TargetStrafe extends Module implements MCHook
{
	public TargetStrafe() 
	{
		super("Strafe", Keyboard.KEY_NONE, Category.BLATANT, false, "Assists with strafing around your opponent");
		this.addSettings(speedValue, rangeValue);
	}
	public static NumberSetting speedValue = new NumberSetting("Speed", 0, 0, 1, 0.01);
	public static NumberSetting rangeValue = new NumberSetting("Distance", 3, 0.1, 6, 0.1);
	
	public static transient boolean direction = false, forward = false, left = false, right = false, back = false;
	
	@Override
	public void onEnable() 
	{
		forward = mc.gameSettings.keyBindForward.pressed;
		left = mc.gameSettings.keyBindLeft.pressed;
		right = mc.gameSettings.keyBindRight.pressed;
		back = mc.gameSettings.keyBindBack.pressed;
	}
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(speedValue.get()) + ", " + String.valueOf(rangeValue.get());
		}
	}
	@Override
	public void onEvent(Event e) 
	{
		if (e instanceof EventMove && e.isPRE()) 
		{
			EventMove event = (EventMove)e;
			if (mc.thePlayer.isCollidedHorizontally) direction = !direction;
			if(Client.isEnabled("Killaura") == null) return;
			
			if (Killaura.target == null) return;
			else 
			{
				double currentSpeed = MovementUtil.getSpeed();
				
				double yawChange = 45;
				
				float f = (float) ((CombatUtil.getAngleHead(Killaura.target)[0] + (direction ? -yawChange : yawChange)) * 0.017453292F);
				double x2 = Killaura.target.posX, z2 = Killaura.target.posZ;
	            x2 -= (double)(MathHelper.sin(f) * (rangeValue.get()) * -1);
	            z2 += (double)(MathHelper.cos(f) * (rangeValue.get()) * -1);
	            
	            float currentSpeed1 = (float) MovementUtil.getSpeed();
	            
				double backupMotX = mc.thePlayer.motionX, backupMotZ = mc.thePlayer.motionZ;
	            event.setSpeed(((currentSpeed + speedValue.get()) / 100) * 90, CombatUtil.getAngle(x2, mc.thePlayer.posY, z2)[0]);
	            mc.thePlayer.motionX = backupMotX;
	            mc.thePlayer.motionZ = backupMotZ;
	            
	            if (currentSpeed > MovementUtil.getSpeed()) 
	            {
	            	direction = !direction;
	            }
			}
		}
	}
}