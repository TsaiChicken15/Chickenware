package chickenware.events.listeners;

import chickenware.events.Event;
import net.minecraft.entity.item.EntityItem;

public class EventRenderItem extends Event<EventRenderItem> 
{
	public EntityItem item;
	  
	public EventRenderItem(EntityItem item) 
	{
	    this.item = item;
	}
}