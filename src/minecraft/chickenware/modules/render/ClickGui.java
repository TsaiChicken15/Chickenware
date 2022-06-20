package chickenware.modules.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.ModeSetting;
import chickenware.ui.clickgui.button.Button;
import chickenware.utils.MSTimer;
import chickenware.utils.RenderUtil;
import net.minecraft.client.gui.FontRenderer;

public class ClickGui extends Module
{
	public ClickGui() 
	{
		super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER, false,"");
		this.addSettings(colorValue);
	}
	public static ModeSetting colorValue = new ModeSetting("Color","White","White","Light Gray","Gray","Dark Gray","Black","Red","Pink","Orange","Yellow","Green","Magenta","Cyan","Blue","Rainbow");
	public void onEnable() 
	{
		if(mc.currentScreen == null) 
		{
			mc.displayGuiScreen(Client.ClickGui);
			this.toggle();
		}
	}
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			FontRenderer fr = mc.fontRendererObj;
			if(colorValue.is("White"))
			{
				Button.color = Color.WHITE.getRGB();
			}
			else if(colorValue.is("Light Gray"))
			{
				Button.color = Color.LIGHT_GRAY.getRGB();
			}
			else if(colorValue.is("Gray"))
			{
				Button.color = Color.GRAY.getRGB();
			}
			else if(colorValue.is("Dark Gray"))
			{
				Button.color = Color.DARK_GRAY.getRGB();
			}
			else if(colorValue.is("Black"))
			{
				Button.color = Color.BLACK.getRGB();
			}
			else if(colorValue.is("Red"))
			{
				Button.color = Color.RED.getRGB();
			}
			else if(colorValue.is("Pink"))
			{
				Button.color = Color.PINK.getRGB();
			}
			else if(colorValue.is("Orange"))
			{
				Button.color = Color.ORANGE.getRGB();
			}
			else if(colorValue.is("Yellow"))
			{
				Button.color = Color.YELLOW.getRGB();
			}
			else if(colorValue.is("Green"))
			{
				Button.color = Color.GREEN.getRGB();
			}
			else if(colorValue.is("Magenta"))
			{
				Button.color = Color.MAGENTA.getRGB();
			}
			else if(colorValue.is("Cyan"))
			{
				Button.color = Color.CYAN.getRGB();
			}
			else if(colorValue.is("Blue"))
			{
				Button.color = Color.BLUE.getRGB();
			}
			else if(colorValue.is("Rainbow"))
			{
				Button.color = RenderUtil.getRainbow(4, 0.8f, 1);
			}
		}
	}
	public static void disable()
	{
        for (Module m : Client.getModule()) 
        {
            if (m.getName() == "ClickGui")
            {
            	m.toggled = false;
            }
        } 
	}
}