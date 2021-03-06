package chickenware.modules.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventRenderPlayer;
import chickenware.modules.Module;
import chickenware.settings.ModeSetting;
import chickenware.utils.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module
{
	public ESP() 
	{
		super("ESP", Keyboard.KEY_NONE, Category.RENDER, false, "Renders a box around players");
		this.addSettings(colorValue);
	}
	public static ModeSetting colorValue = new ModeSetting("Color","White","White","Light Gray","Gray","Dark Gray","Black","Red","Pink","Orange","Yellow","Green","Magenta","Cyan","Blue","Rainbow");
	public void onEvent(Event e) 
	{	
		if (e instanceof EventRenderPlayer) 
		{
			if(((EventRenderPlayer)e).entity instanceof EntityPlayer)
			{
				EntityPlayer p = (EntityPlayer) ((EventRenderPlayer)e).entity;
				int color = 0;
				if(colorValue.is("White"))
				{
					color = Color.WHITE.getRGB();
				}
				else if(colorValue.is("Light Gray"))
				{
					color = Color.LIGHT_GRAY.getRGB();
				}
				else if(colorValue.is("Gray"))
				{
					color = Color.GRAY.getRGB();
				}
				else if(colorValue.is("Dark Gray"))
				{
					color = Color.DARK_GRAY.getRGB();
				}
				else if(colorValue.is("Black"))
				{
					color = Color.BLACK.getRGB();
				}
				else if(colorValue.is("Red"))
				{
					color = Color.RED.getRGB();
				}
				else if(colorValue.is("Pink"))
				{
					color = Color.PINK.getRGB();
				}
				else if(colorValue.is("Orange"))
				{
					color = Color.ORANGE.getRGB();
				}
				else if(colorValue.is("Yellow"))
				{
					color = Color.YELLOW.getRGB();
				}
				else if(colorValue.is("Green"))
				{
					color = Color.GREEN.getRGB();
				}
				else if(colorValue.is("Magenta"))
				{
					color = Color.MAGENTA.getRGB();
				}
				else if(colorValue.is("Cyan"))
				{
					color = Color.CYAN.getRGB();
				}
				else if(colorValue.is("Blue"))
				{
					color = Color.BLUE.getRGB();
				}
				else if(colorValue.is("Rainbow"))
				{
					color = RenderUtil.getRainbow(4, 0.8f, 1);
				}
				RenderUtil.drawPlayerBox(((double)p.lastTickPosX + (p.posX - p.lastTickPosX) * (double) mc.timer.renderPartialTicks + p.field_71079_bU), 
						((double)p.lastTickPosY + (p.posY - p.lastTickPosY) * (double) mc.timer.renderPartialTicks + p.field_71082_cx), 
						((double)p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double) mc.timer.renderPartialTicks + p.field_71089_bV), color);
			}
		}
	}
}