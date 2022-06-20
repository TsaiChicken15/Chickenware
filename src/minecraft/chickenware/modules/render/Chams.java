package chickenware.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import chickenware.events.Event;
import chickenware.events.listeners.EventRenderItem;
import chickenware.events.listeners.EventRenderPlayer;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.RenderUtil;

public class Chams extends Module
{
	public Chams() 
	{
		super("Chams", Keyboard.KEY_NONE, Category.RENDER, false,"Renders players through walls");
		this.addSettings(invisibleValue, itemValue, playerModeValue, colorRedValue, colorGreenValue, colorBlueValue, colorAlphaValue);
	}
	public static BooleanSetting itemValue = new BooleanSetting("Item", false);
	public static BooleanSetting invisibleValue = new BooleanSetting("Invisible", true);
	public static ModeSetting playerModeValue = new ModeSetting("Color","Default","Default","Color","Rainbow");
	public static NumberSetting colorRedValue = new NumberSetting("Red", 255, 0, 255, 1);
	public static NumberSetting colorGreenValue = new NumberSetting("Green", 255, 0, 255, 1);
	public static NumberSetting colorBlueValue = new NumberSetting("Blue", 255, 0, 255, 1);
	public static NumberSetting colorAlphaValue = new NumberSetting("Alpha", 200, 0, 255, 1);
	public void onEvent(Event e) 
	{	
		if (e instanceof EventRenderPlayer) 
		{
			if(playerModeValue.is("Default"))
			{
				if (e.isPRE()) 
				{
	                GL11.glEnable(32823);
	                GL11.glPolygonOffset(1.0f, -1100000.0f);
				}
				if (e.isPOST()) 
				{
	                GL11.glDisable(32823);
	                GL11.glPolygonOffset(1.0f, 1100000.0f);
				}
			}
		}
		if (e instanceof EventRenderItem) 
		{
			if(itemValue.get()) 
			{
				if (e.isPRE()) 
				{
	                GL11.glEnable(32823);
	                GL11.glPolygonOffset(1.0f, -1100000.0f);
				}
				if (e.isPOST()) 
				{
	                GL11.glDisable(32823);
	                GL11.glPolygonOffset(1.0f, 1100000.0f);
				}
			}
		}
	}
	public static void setColor() 
	{
		if(playerModeValue.is("Rainbow"))
		{
			RenderUtil.glColor(RenderUtil.getRainbow(4, 0.8f, 1));
		}
		else if(playerModeValue.is("Color"))
		{
			GL11.glColor4f((float)(colorRedValue.get() / 255f), (float)(colorGreenValue.get() / 255f), (float)(colorBlueValue.get() / 255f), (float)(colorAlphaValue.get() / 255f));
		}
	}
}