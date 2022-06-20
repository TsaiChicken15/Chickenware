package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.notification.Notification;
import chickenware.notification.NotificationManager;
import chickenware.notification.NotificationType;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import chickenware.utils.MSTimer;
import chickenware.utils.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;



public class LongJump extends Module implements MCHook
{
	public LongJump() 
	{
		super("LongJump", Keyboard.KEY_G, Category.BLATANT, true, "Allows you to jump far"); 
		this.addSettings(modeValue,verusBoostValue,verusHeightValue,verusTimerValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","VerusDmg","OldBMC","OldBMC2","VerusDmg");
	public static NumberSetting verusBoostValue = new NumberSetting("VerusBoost", 4.25f, 0f, 10f, 0.01f);
	public static NumberSetting verusHeightValue = new NumberSetting("VerusHeight", 0.42f, 0f, 10f, 0.01f);
	public static NumberSetting verusTimerValue = new NumberSetting("VerusTimer", 1f, 0.05f, 10f, 0.01f);
	private MSTimer timer = new MSTimer();
	public boolean hasJumped = false, verusDmged = false;
	public void onEnable() 
	{
		verusDmged = false;
		double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
		if(modeValue.is("VerusDmg")) 
		{
			if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 4, 0).expand(0, 0, 0)).isEmpty()) {
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y + 4, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
            }
			else 
			{
				NotificationManager.show(new Notification(NotificationType.WARNING, "¡±lDamage failed", 2));
				this.toggle();
			}
		}
		hasJumped = false;
	}
	public void onDisable() 
	{
		mc.timer.timerSpeed = 1f;
		hasJumped = false;
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
		if (e instanceof EventUpdate && e.isPRE()) 
		{
			if(modeValue.is("OldBMC")) {
                mc.thePlayer.jumpMovementFactor = 0.1f;
                mc.thePlayer.motionY += 0.0132;
                mc.thePlayer.jumpMovementFactor = 0.09f;
                mc.timer.timerSpeed = 0.8f;
                MovementUtil.strafe();
            }
			else if(modeValue.is("OldBMC2"))
            {
                mc.thePlayer.motionY += 0.01554;
                MovementUtil.strafe(MovementUtil.getSpeed() * 1.114514f);
                mc.timer.timerSpeed = 0.917555f;
            }
			else if(modeValue.is("VerusDmg")) 
			{
				if (mc.thePlayer.hurtTime !=0 && !verusDmged) 
				{
	                verusDmged = true;
	                MovementUtil.strafe(verusBoostValue.get());
	                mc.thePlayer.motionY = verusHeightValue.get();
	                hasJumped = true;
	            }

	            if (verusDmged)
	            {
	                mc.timer.timerSpeed = (float) verusTimerValue.get();
	            }
	            else 
	            {
	                mc.thePlayer.movementInput.moveForward = 0F;
	                mc.thePlayer.movementInput.moveStrafe = 0F;
	            }
			}
			
			if (!hasJumped && mc.thePlayer.onGround && MovementUtil.isMoving()) {
				if(modeValue.is("OldBMC") || modeValue.is("OldBMC2"))
					mc.thePlayer.jump();
	            hasJumped = true;
	            timer.reset();
	        }
			if(hasJumped)
			{
				if(timer.hasTimePassed(5L) && mc.thePlayer.onGround)
				{
					this.toggle();
				}
			}
		}
	}
}