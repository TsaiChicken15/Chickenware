package chickenware.modules.combat;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.blatant.Scaffold;
import chickenware.settings.ModeSetting;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MCHook;
import chickenware.utils.MovementUtil;
import net.minecraft.potion.Potion;

public class Sprint extends Module implements MCHook
{
	public Sprint() 
	{
		super("Sprint", Keyboard.KEY_NONE, Category.COMBAT, false, "Automatically activates sprinting for you");
		this.addSettings(modeValue);
	}
	public static ModeSetting modeValue = new ModeSetting("Mode","Keybind","Keybind","SetSprint");
	public void onDisable() 
	{
		KeybindUtil.state(_gs.keyBindSprint.getKeyCode(), KeybindUtil.isDown(_gs.keyBindSprint.getKeyCode()));
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate)
		{
			if(modeValue.is("Keybind"))
			{
				KeybindUtil.state(_gs.keyBindSprint.getKeyCode(), true);
			}	
			else if(modeValue.is("SetSprint"))
			{
				if(Client.isEnabled("Scaffold") != null)
				{
					if(Scaffold.noSprintValue.get())
					{
						mc.thePlayer.setSprinting(false);
			            return;
					}
	        	}
				
				if(Client.isEnabled("NoSlow") == null)
				{
					if(mc.thePlayer.isUsingItem())
					{
						mc.thePlayer.setSprinting(false);
			            return;
					}
				}
				
				if (!MovementUtil.isMoving() || 
		        		mc.thePlayer.isSneaking() ||
		        		mc.thePlayer.isPotionActive(Potion.blindness) || 
		                !(mc.thePlayer.getFoodStats().getFoodLevel() > 6.0f || mc.thePlayer.capabilities.allowFlying) ||
		                mc.thePlayer.isCollidedHorizontally) 
		        {
		        	mc.thePlayer.setSprinting(false);
		            return;
		        }
				
				if (mc.thePlayer.movementInput.moveForward >= 0.8F && !mc.thePlayer.isSprinting())
					mc.thePlayer.setSprinting(true);
			}
		}
	}
}