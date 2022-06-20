package chickenware.modules.other;

import org.lwjgl.input.Keyboard;

import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.utils.MCHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module implements MCHook
{
	public AntiBot() 
	{
		super("AntiBot", Keyboard.KEY_NONE, Category.OTHER, false, "");
		this.toggled = true;
	}
	public static boolean isBot(Entity entity)
	{
		if(!(entity instanceof EntityPlayer))
		{
			return false;
		}
		EntityPlayer e = (EntityPlayer) entity;
        if (e.inventory.armorInventory[0] == null && 
        		e.inventory.armorInventory[1] == null &&
	            e.inventory.armorInventory[2] == null && 
	            e.inventory.armorInventory[3] == null &&
	            e.motionX == 0 &&
	            e.motionY == 0 &&
	            e.motionZ == 0) 
        {
            return true;
        }
        
        if(entity.getDisplayName().getFormattedText().toLowerCase().contains("shop") || entity.getDisplayName().getFormattedText().toLowerCase().contains("upgrade"))
        {
        	return true;
        }
        
        return entity.getName().isEmpty() || entity.getName() == mc.thePlayer.getName();
	}
}