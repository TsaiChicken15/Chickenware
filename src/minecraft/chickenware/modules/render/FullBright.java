package chickenware.modules.render;

import org.lwjgl.input.Keyboard;

import chickenware.modules.Module;
import chickenware.utils.MCHook;

public class FullBright extends Module implements MCHook
{
	public FullBright() 
	{
		super("FullBright", Keyboard.KEY_NONE, Category.RENDER, false,"Removes darkness");
	}
	public void onEnable() 
	{
		_gs.gammaSetting = 100.0f;
	}
}