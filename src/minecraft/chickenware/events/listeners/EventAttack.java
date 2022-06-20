package chickenware.events.listeners;

import chickenware.events.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event<EventAttack> {
	public Entity entity;
	
	public EventAttack(Entity entity) {
		this.entity = entity;
	}
}
