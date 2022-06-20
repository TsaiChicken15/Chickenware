package chickenware.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import chickenware.events.Event;
import chickenware.events.listeners.EventRenderLivingLabel;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.BooleanSetting;
import chickenware.utils.MCHook;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class NameTags extends Module implements MCHook
{
	public NameTags() 
	{
		super("NameTags", Keyboard.KEY_NONE, Category.RENDER, false, "Renders name plates of entities through walls");
		this.addSettings(invisibleValue,moreInfoValue);
	}
	public static BooleanSetting invisibleValue = new BooleanSetting("Invisible",true);
	public static BooleanSetting moreInfoValue = new BooleanSetting("MoreInfo",true);
	public void onEvent(Event e) {
		if (e instanceof EventRenderLivingLabel) 
		{
			EventRenderLivingLabel nametag = (EventRenderLivingLabel)e;
			if(nametag.e instanceof EntityPlayer) 
			{
				if (e.isPRE()) 
				{
	                GL11.glEnable(32823);
	                GL11.glPolygonOffset(1.0f, -1100000.0f);
	                nametag.maxDistance = 1000;
				}
				else if (e.isPOST()) 
				{
	                GL11.glDisable(32823);
	                GL11.glPolygonOffset(1.0f, 1100000.0f);
				}
			}else {
				return;
			}
		}
		if (e instanceof EventUpdate && e.isPRE()) 
		{
			try {
				for (Object maybeEntity : mc.theWorld.loadedEntityList) 
				{
					if (maybeEntity instanceof EntityPlayer) 
					{
						EntityPlayer entity = ((EntityPlayer)maybeEntity);
						entity.setAlwaysRenderNameTag(true);
						entity.setCustomNameTag(entity.getName());
					}
					else {
						return;
					}
				}
			} catch (Exception e2) {}
		}
	}
}