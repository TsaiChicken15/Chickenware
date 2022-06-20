package chickenware.modules.utility;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.utils.ItemUtil;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MCHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Scaffold extends Module implements MCHook
{
	public Scaffold() 
	{
		super("Scaffold", Keyboard.KEY_NONE, Category.UTILITY,true,"Assists with bridging");
		this.addSettings(autoSwitchValue);
	}
	public static BooleanSetting autoSwitchValue =  new BooleanSetting("AutoSwitch", true);
	private boolean placeTried = false;
	private double lastY;
	private int oldSlot = -1;
	public void onEnable() 
	{
		if(autoSwitchValue.get())
		{
			oldSlot = mc.thePlayer.inventory.currentItem;
			for (int index = 36; index <= 44; ++index) 
			{
	            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if(stack == null)
	            {
	            	continue;
	            }
	            if (stack.getItem() instanceof ItemBlock && !ItemUtil.isBadBlock(stack)) 
	            {
	            	KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[index - 36].getKeyCode());
	            	//mc.thePlayer.inventory.currentItem = index - 36;
	            }
	        }
		}
		placeTried = false;
	}
	public void onDisable() 
	{
		KeybindUtil.state(_gs.keyBindSneak.getKeyCode(), KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode()));
		if(autoSwitchValue.get() && oldSlot > -1)
		{
			KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[oldSlot].getKeyCode());
		}
	}
	public void onEvent(Event e) 
	{
	    if (e instanceof EventUpdate && e.isPRE())
	    {
	    	if(KeybindUtil.isDown(_gs.keyBindJump.getKeyCode()) && mc.thePlayer.movementInput.moveForward > 0) 
			{
				_gs.keyBindSneak.pressed = false;
			}
	    	if (mc.thePlayer.onGround) 
	    	{
	    		lastY = mc.thePlayer.posY;
	    	}
	    	if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0 && mc.thePlayer.rotationPitch > 0 && mc.thePlayer.movementInput.jump && placeTried)
	    	{
	    		KeybindUtil.onTick(_gs.keyBindUseItem.getKeyCode(), 50);
    			placeTried = true;
	    	}
	    	else if(mc.thePlayer.onGround)
	    	{
				placeTried = false;
	    	}
	    	for (double x = -0.25D; x <= 0.25D; x += 0.25D) 
	    	{
	    		for (double z = -0.25D; z <= 0.25D; z += 0.25D) 
	    		{
	    			if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x, 0.0D, z)).isEmpty()) 
	    			{
	    				for (double x1 = -0.045D; x1 <= 0.045D; x1 += 0.045D) 
	    				{
	    					for (double z1 = -0.045D; z1 <= 0.045D; z1 += 0.045D) 
	    					{
	    						if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) 
	    						{
	    							break;
	    						}
	    						if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x1, 0.0D, z1)).isEmpty() && !placeTried)
	    						{
	    							KeybindUtil.onTick(_gs.keyBindUseItem.getKeyCode(), 50);
	    		        			placeTried = true;
	    						}
	    						else 
	    						{
	    							placeTried = false;
	    						}
	    					}
	    				}
	    			}
	    			if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x, 0.0D, z)).isEmpty() && mc.thePlayer.posY % 0.015625 < 0.0001) 
	    			{
	    				KeybindUtil.state(_gs.keyBindSneak.getKeyCode(), true);
	    				return;
	    			}
	    			if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.125D, 0.0D)).isEmpty() && !KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode())) 
	    			{
	    				KeybindUtil.state(_gs.keyBindSneak.getKeyCode(), false);
	    			}
	    		} 
	    	} 
	    } 
	}
}