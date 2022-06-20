package chickenware.events.listeners;

import chickenware.events.Event;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EventRenderPlayer extends Event<EventRenderPlayer> {
	public AbstractClientPlayer entity;
	  
	public EventRenderPlayer(AbstractClientPlayer entity) {
	    this.entity = entity;
	}
}