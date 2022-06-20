package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventBlockBB;
import chickenware.events.listeners.EventJump;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.Description;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import chickenware.utils.MSTimer;
import chickenware.utils.MovementUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class Fly extends Module implements MCHook
{
	public Fly() 
	{
		super("Fly", Keyboard.KEY_F, Category.BLATANT, true, "Allows you to fly"); 
		this.addSettings(modeValue,vanillaDes,noGravityValue, noSameSpeedValue, motionResetValue, speedValue,verus3Des,airStrafeValue,airwalkDes,strafeValue,customSpeedValue,airWalkSpeedValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Vanilla","Vanilla","Verus3","AirWalk");
	public static Description vanillaDes = new Description("Vanilla");
	public static BooleanSetting noGravityValue = new BooleanSetting("NoGravity", false);
	public static BooleanSetting noSameSpeedValue = new BooleanSetting("NoSameSpeed", true);
	public static BooleanSetting motionResetValue = new BooleanSetting("MotionReset", true);
	public static NumberSetting speedValue = new NumberSetting("Speed", 2f, 0f, 10f, 0.1);
	public static Description verus3Des = new Description("Verus3");
	public static BooleanSetting airStrafeValue = new BooleanSetting("AirStrafe", true);
	public static Description airwalkDes = new Description("AirWalk");
	public static BooleanSetting strafeValue = new BooleanSetting("Strafe", true);
	public static BooleanSetting customSpeedValue = new BooleanSetting("CustomSpeed", false);
	public static NumberSetting airWalkSpeedValue = new NumberSetting("Speed", 0.6f, 0f, 1f, 0.01);
	protected static double accel, launchY;
	protected int tick = 0;
	private MSTimer timer = new MSTimer();
	public void onEnable()
	{
		tick = 0;
		launchY = mc.thePlayer.posY;
	}
	public void onDisable() 
	{
		if(modeValue.is("Vanilla"))
		{
			if(motionResetValue.get())
			{
				mc.thePlayer.motionX = 0.0;
				mc.thePlayer.motionY = 0.0;
				mc.thePlayer.motionZ = 0.0;
			}
		}
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
		if(e instanceof EventBlockBB)
		{
			if(modeValue.is("Verus3"))
			{
				if (((EventBlockBB)e).block instanceof BlockAir && ((EventBlockBB)e).y <= this.launchY) 
				{
					((EventBlockBB)e).blockBB = AxisAlignedBB.fromBounds(((EventBlockBB)e).x, ((EventBlockBB)e).y, ((EventBlockBB)e).z, ((EventBlockBB)e).x + 1.0, this.launchY, ((EventBlockBB)e).z + 1.0);
		        }
			}
			else if(modeValue.is("AirWalk"))
			{
				if (((EventBlockBB)e).block instanceof BlockAir && ((EventBlockBB)e).y <= this.launchY)
				{
					((EventBlockBB)e).blockBB = AxisAlignedBB.fromBounds(((EventBlockBB)e).x, ((EventBlockBB)e).y, ((EventBlockBB)e).z, ((EventBlockBB)e).x + 1.0, this.launchY, ((EventBlockBB)e).z + 1.0);
				}
			}
		}
		if (e instanceof EventUpdate) 
		{
			tick++;
			if(modeValue.is("Vanilla"))
			{
				accel = Math.random() * 0.001;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionY = 0;
				mc.thePlayer.motionZ = 0;
				MovementUtil.strafe(MovementUtil.isMovementInputing() ? speedValue.get() + (noSameSpeedValue.get() ? accel : 0) : 0);
				if(_gs.keyBindJump.getIsKeyPressed())
				{
					mc.thePlayer.motionY += 0.41999998688698;
				}
				if(_gs.keyBindSneak.getIsKeyPressed()) 
				{
					mc.thePlayer.motionY -= 0.41999998688698;
				}
				if(!noGravityValue.get() && !MovementUtil.isMoving())
				{
					mc.thePlayer.motionY -= 0.0784000015258789;
				}
			}	
			else if(modeValue.is("Verus3"))
			{
				mc.gameSettings.keyBindJump.pressed = false;
		        if (mc.thePlayer.onGround && MovementUtil.isMovementInputing()) 
		        {
		            mc.thePlayer.jump();
		            MovementUtil.strafe(0.48F);
		        } 
		        else if(airStrafeValue.get()) 
		        {
		            MovementUtil.strafe();
		        }
			}
			else if(modeValue.is("AirWalk"))
			{
				if(customSpeedValue.get() && mc.thePlayer.posY == launchY)
				{
					if(strafeValue.get())
					{
						MovementUtil.strafe((MovementUtil.isMovementInputing() ? airWalkSpeedValue.get() : 0));
					}
					else
					{
						if(MovementUtil.isMovementInputing())
						{
							mc.thePlayer.motionX = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * airWalkSpeedValue.get();
							mc.thePlayer.motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * airWalkSpeedValue.get();
						}
					}
				}					
			}
		}
		if (e instanceof EventJump) 
		{
			if(modeValue.is("AirWalk"))
			{
				e.cancelEvent();
			}
		}
	}
}