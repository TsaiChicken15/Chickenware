package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;

public class VClip extends Module implements MCHook
{
	public VClip() 
	{
		super("VClip", Keyboard.KEY_NONE, Category.BLATANT, true, "");
		this.addSettings(heightValue);
	}
	public static NumberSetting heightValue = new NumberSetting("Height", 3, 0, 10, 0.1);
	public void onEnable() 
	{
		if(_gs.keyBindSneak.getIsKeyPressed())
		{
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - heightValue.get(), mc.thePlayer.posZ);
		}
		else
		{
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + heightValue.get(), mc.thePlayer.posZ);
		}
		this.toggle();
	}
}