package chickenware.modules.combat;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import chickenware.utils.MSTimer;
import chickenware.utils.RandomUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class Refill extends Module implements MCHook
{
	public Refill() 
	{
		super("Refill", Keyboard.KEY_NONE, Category.COMBAT, false, "Refills your hotbar with healing items");
		this.addSettings(modeValue, minDelayValue, maxDelayValue, onlyInvValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Potion","Potion","Soup","Both");
	public static NumberSetting minDelayValue = new NumberSetting("MinDelay", 150, 0, 400, 1);
	public static NumberSetting maxDelayValue = new NumberSetting("MaxDelay", 200, 0, 400, 1);
	public static BooleanSetting onlyInvValue = new BooleanSetting("InvOnly",false);
	private MSTimer timer = new MSTimer();
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
			if(!(mc.currentScreen instanceof GuiContainer) && onlyInvValue.get()) 
			{
				return;
			}
			
			if (timer.hasTimePassed(RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get()))) 
			{
				refill();
				timer.reset();
			}
		}
	}
	public void refill() 
	{
		int space = hasSpace();
		int item = findHealingItem();
		if(space > -1 && item != -1) 
		{
			mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
		}
	}
	public static int findHealingItem() 
	{
        for(int i = 9; i < 36; i++) 
        {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if(item != null) 
            {
            	if(modeValue.is("Potion") && item.getItem() == Items.potionitem && item.getDisplayName().equalsIgnoreCase("Splash Potion of Healing")) 
            	{ 
            		return i;
            	}
            	else if(modeValue.is("Soup") && item.getItem() instanceof ItemFood && item.getDisplayName().equalsIgnoreCase("Mushroom Stew")) 
            	{
            		return i;
            	}
            	else if(modeValue.is("Both") && 
            			( ( item.getItem() instanceof ItemFood && item.getDisplayName().equalsIgnoreCase("Mushroom Stew") ) || 
            					( item.getItem() == Items.potionitem && item.getDisplayName().equalsIgnoreCase("Splash Potion of Healing") ) )) 
            	{
            		return i;
            	}
            }
        }
        return -1;
    }
	public static int hasSpace() 
	{
        for(int j = 36; j < 45; j++) 
        {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(j).getStack();
            if(item == null) 
            {
            	return j;
            }
        }
        return -1;
    }
}