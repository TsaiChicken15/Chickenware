package chickenware.modules.combat;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.ItemUtil;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MCHook;
import chickenware.utils.MSTimer;
import chickenware.utils.RandomUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ThrowPot extends Module implements MCHook
{
	public ThrowPot() 
	{
		super("ThrowPot", Keyboard.KEY_NONE, Category.COMBAT, false, "Throws or consumes healing items upon health is too low");
		this.addSettings(modeValue, healthValue, minDelayValue, maxDelayValue, limitAngleValue, minAngleValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Potion","Potion","Soup");
	public static NumberSetting healthValue = new NumberSetting("Health", 15, 1, 20, 1);
	public static NumberSetting minDelayValue = new NumberSetting("MinDelay", 150, 100, 1000, 1);
	public static NumberSetting maxDelayValue = new NumberSetting("MaxDelay", 200, 100, 1000, 1);
	public static BooleanSetting limitAngleValue =  new BooleanSetting("LimitAngle", true);
	public static NumberSetting minAngleValue = new NumberSetting("MinAngle", 80, 0, 90, 1);
	private MSTimer timer = new MSTimer();
	private int oldSlot = -1, newSlot = -1;
	private boolean throwed = false;
	public void onEvent2(Event e)
	{
		if (e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(minDelayValue.get()) + ", " + String.valueOf(maxDelayValue.get());
			if(minDelayValue.get() > maxDelayValue.get()) 
			{
				double temp = minDelayValue.get();
				minDelayValue.setValue(maxDelayValue.get());
				maxDelayValue.setValue(temp);
			}
		}
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			if(mc.currentScreen != null)
			{
				return;
			}
			if(mc.thePlayer.getHealth() < healthValue.get())
			{
				if(timer.hasTimePassed(RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get())) && !throwed)
	        	{
					oldSlot = mc.thePlayer.inventory.currentItem;
					for (int index = 36; index <= 44; ++index) 
					{
						if(mc.thePlayer.inventoryContainer.getSlot(index).getHasStack())
						{
				            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
				            if (stack.getItem() == Items.potionitem && stack.getDisplayName().equalsIgnoreCase("Splash Potion of Healing") && modeValue.is("Potion")) 
			            	{
				            	if(limitAngleValue.get() && mc.thePlayer.rotationPitch <= minAngleValue.get()) 
				            	{
				            		return;
				            	}
				            	KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[index - 36].getKeyCode());
				            	//mc.thePlayer.inventory.currentItem = index - 36;
				            	KeybindUtil.onTick(_gs.keyBindUseItem.getKeyCode());
				            	throwed = true;
				            }
				            else if (stack.getItem() == Items.potionitem && stack.getDisplayName().equalsIgnoreCase("Mushroom Stew") && modeValue.is("Soup")) 
				            {
				            	KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[index - 36].getKeyCode());
				            	//mc.thePlayer.inventory.currentItem = index - 36;
				            	KeybindUtil.onTick(_gs.keyBindUseItem.getKeyCode());
				            	throwed = true;
				            }
						}
			        }
	        	}
				else if(throwed && oldSlot != -1)
				{
					KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[oldSlot].getKeyCode());
					//mc.thePlayer.inventory.currentItem = oldSlot;
					throwed = false;
					timer.reset();
				}
			}
		}
	}
}