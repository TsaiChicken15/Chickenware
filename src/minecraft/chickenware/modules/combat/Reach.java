package chickenware.modules.combat;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventGetReach;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import net.minecraft.entity.Entity;

public class Reach extends Module implements MCHook
{
	public Reach() 
	{
		super("Reach", Keyboard.KEY_NONE, Category.COMBAT, false, "Increases your attack distance");
		this.addSettings(attackRangeValue, buildRangeValue, onlySprintValue, verticalCheckValue, disableInWaterValue);
	}
	public static NumberSetting attackRangeValue = new NumberSetting("AttackRange", 3.0, 0.0, 6.0, 0.01);
	public static NumberSetting buildRangeValue = new NumberSetting("BuildRange", 4.5, 0.0, 6.0, 0.01);
	public static BooleanSetting onlySprintValue = new BooleanSetting("OnlySpirnt",false);
	public static BooleanSetting verticalCheckValue = new BooleanSetting("NoVertical",false);
	public static BooleanSetting disableInWaterValue = new BooleanSetting("NoWater",false);
	public static Entity entity;
	public void onEvent2(Event e)
	{
		if (e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(attackRangeValue.get()) + ", " + String.valueOf(buildRangeValue.get());
			if(attackRangeValue.get() > buildRangeValue.get()) 
			{
				double temp = attackRangeValue.get();
				attackRangeValue.setValue(buildRangeValue.get());
				buildRangeValue.setValue(temp);
			}
		}
	}
	public void onEvent(Event e) 
	{
	    if(e instanceof EventGetReach) 
	    {
	    	if((mc.thePlayer.isInWater() && disableInWaterValue.get()) || (!mc.thePlayer.isSprinting() && onlySprintValue.get()))
	    	{
	    		return;
	    	}
	    	
	    	if(mc.pointedEntity != null)
	    	{
	    		if(verticalCheckValue.get())
	    		{
	    			if(Math.abs(mc.pointedEntity.posY - mc.thePlayer.posY) > 2.0f)
	    			{
	    				return;
	    			}
	    		}
	    	}
	    	((EventGetReach)e).d = buildRangeValue.get();
	    	e.cancelEvent();
	    }
	}
}