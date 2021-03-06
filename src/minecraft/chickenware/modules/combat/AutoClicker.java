package chickenware.modules.combat;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventAttack;
import chickenware.events.listeners.EventRender3D;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MCHook;
import chickenware.utils.RandomUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;


public class AutoClicker extends Module implements MCHook
{
	public AutoClicker() 
	{
		super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT, false, "Automatically clicks for you while holding down the attack button");
		this.addSettings(modeValue, itemValue, blockHitValue, minCPSValue, maxCPSValue, jitterValue, blockHitDurationValue);
	}
	public static NumberSetting minCPSValue = new NumberSetting("MinCPS", 5, 1, 50, 1);
	public static NumberSetting maxCPSValue = new NumberSetting("MaxCPS", 8, 1, 50, 1);
	public static NumberSetting jitterValue = new NumberSetting("Jitter", 0, 0, 5, 0.5);
	public static BooleanSetting blockHitValue = new BooleanSetting("BlockHit",false);
	public static NumberSetting blockHitDurationValue = new NumberSetting("Duration", 200, 0, 1000, 1);
	public static ModeSetting itemValue = new ModeSetting("LimitItem","None","None","Sword","Axe","Sword & Axe");
	public static ModeSetting modeValue = new ModeSetting("Mode","Legit","Legit","Packet");
	private long rightDelay = 1000 / RandomUtil.randomInt((int)minCPSValue.get(), (int)maxCPSValue.get());
    	private long rightLastSwing = 0L;
    	private long leftDelay = 1000 / RandomUtil.randomInt((int)minCPSValue.get(), (int)maxCPSValue.get());
    	private long leftLastSwing = 0L;
    	private long blockHitDelay = (long)(blockHitDurationValue.get() * 2);
    	private long lastblockHit = 0L;
	private boolean blocking;
	public void onEvent2(Event e)
	{
		if (e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(minCPSValue.get()) + ", " + String.valueOf(maxCPSValue.get());
			if(minCPSValue.get() > maxCPSValue.get()) 
			{
				double temp = minCPSValue.get();
				minCPSValue.setValue(maxCPSValue.get());
				maxCPSValue.setValue(temp);
			}
		}
	}
	public void onEvent(Event e) 
	{
		if (e instanceof EventRender3D) 
		{
			if(_mu.isDown(1) && !blocking)
			{
				blocking = true;
			}
			else if(!_mu.isDown(1) && blocking)
			{
				blocking = false;
			}
			
			if(!blocking && mc.thePlayer.isBlocking() && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && System.currentTimeMillis() - lastblockHit >= blockHitDelay)
			{
				KeybindUtil.state(_gs.keyBindUseItem.getKeyCode(), false);
				lastblockHit = System.currentTimeMillis();
				leftLastSwing = System.currentTimeMillis();
			}
			
			if(itemValue.is("None")) {}
			else if(mc.thePlayer.inventory.getCurrentItem() != null)
			{
				if(itemValue.is("Sword & Axe")) 
				{
					if((!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe)) || mc.thePlayer.getHeldItem().getItem() == null)
					{
						return;
					}
				}
				else if(itemValue.is("Sword")) 
				{
					if(!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) || mc.thePlayer.getHeldItem().getItem() == null)
					{
						return;
					}
				}
				else if(itemValue.is("Axe")) 
				{
					if(!(mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe) || mc.thePlayer.getHeldItem().getItem() == null)
					{
						return;
					}
				}
			}
			else
			{
				return;
			}
			
			if (!_gs.keyBindUseItem.getIsKeyPressed() && _gs.keyBindAttack.getIsKeyPressed() && System.currentTimeMillis() - leftLastSwing >= leftDelay && !mc.thePlayer.isBlocking() && !mc.thePlayer.isUsingItem()) 
			{
				if(modeValue.is("Legit"))
					if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY || mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
						KeybindUtil.onTick(_gs.keyBindAttack.getKeyCode());
					else
						mc.thePlayer.swingItem();
				else if(modeValue.is("Packet"))
				{
					mc.thePlayer.swingItem();
					
					if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
						mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
					else if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
						mc.playerController.func_180511_b(mc.objectMouseOver.func_178782_a(), mc.objectMouseOver.field_178784_b);
					else
						if (mc.playerController.isNotCreative())
							mc.leftClickCounter = 10;
				}
				
				leftLastSwing = System.currentTimeMillis();
			    leftDelay = 1000 / RandomUtil.randomInt((int)minCPSValue.get(), (int)maxCPSValue.get());
	            if (jitterValue.get() > 0 && mc.playerController.curBlockDamageMP == 0f && 
	            		mc.thePlayer.rotationPitch - jitterValue.get() * 2 - jitterValue.get() > -90 && 
	            		mc.thePlayer.rotationPitch + jitterValue.get() * 2 - jitterValue.get() < 90) 
	            {
	            	mc.thePlayer.rotationYaw += shake();
					mc.thePlayer.rotationPitch += shake();
				}
	        }
		} 
		if(e instanceof EventAttack)
		{
			if(((EventAttack)e).entity instanceof EntityPlayer || 
					((EventAttack)e).entity instanceof EntityMob || 
					((EventAttack)e).entity instanceof EntityAnimal || 
					((EventAttack)e).entity instanceof EntityVillager || 
					((EventAttack)e).entity instanceof EntityGolem)
			{
				EntityLivingBase t = (EntityLivingBase) ((EventAttack)e).entity;
				if(blockHitValue.get())
				{
					if(t != null)
					{
						if(mc.thePlayer.inventory.getCurrentItem() != null)
						{
							if(!mc.thePlayer.isBlocking() && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && System.currentTimeMillis() - lastblockHit >= blockHitDelay / 2)
							{
								KeybindUtil.state(_gs.keyBindUseItem.getKeyCode(), true);
								leftLastSwing = System.currentTimeMillis();
								t = null;
							}
						}
					}
				}
			}
		}
    	}
	
	public double shake() 
	{
		return Math.random() * jitterValue.get() * 2 - jitterValue.get();
	}
}
