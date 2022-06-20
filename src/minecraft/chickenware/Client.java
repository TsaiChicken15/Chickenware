package chickenware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.Display;

import chickenware.alt.AltManager;
import chickenware.events.Event;
import chickenware.events.listeners.EventKey;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.modules.blatant.BedNuker;
import chickenware.modules.blatant.Fly;
import chickenware.modules.blatant.GuiMove;
import chickenware.modules.blatant.Hitbox;
import chickenware.modules.blatant.KeepSprint;
import chickenware.modules.blatant.Killaura;
import chickenware.modules.blatant.LongJump;
import chickenware.modules.blatant.Speed;
import chickenware.modules.blatant.TargetStrafe;
import chickenware.modules.blatant.Timer;
import chickenware.modules.blatant.VClip;
import chickenware.modules.combat.AimAssist;
import chickenware.modules.combat.AutoClicker;
import chickenware.modules.combat.Reach;
import chickenware.modules.combat.Refill;
import chickenware.modules.combat.Sprint;
import chickenware.modules.combat.ThrowPot;
import chickenware.modules.combat.Velocity;
import chickenware.modules.combat.WTap;
import chickenware.modules.other.AntiBot;
import chickenware.modules.other.AutoDisable;
import chickenware.modules.other.Damage;
import chickenware.modules.other.NoRotate;
import chickenware.modules.render.Animations;
import chickenware.modules.render.Chams;
import chickenware.modules.render.ESP;
import chickenware.modules.render.FullBright;
import chickenware.modules.render.LowFire;
import chickenware.modules.render.NameTags;
import chickenware.modules.render.NoHurtCam;
import chickenware.modules.render.TextGUI;
import chickenware.modules.render.Tracer;
import chickenware.modules.render.ViewModel;
import chickenware.modules.utility.AntiVoid;
import chickenware.modules.utility.AutoArmor;
import chickenware.modules.utility.AutoTool;
import chickenware.modules.utility.ChestSteal;
import chickenware.modules.utility.Panic;
import chickenware.modules.utility.Parkour;
import chickenware.modules.utility.SafeWalk;
import chickenware.ui.Hud;
import chickenware.utils.MCHook;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Client implements MCHook 
{	
	public static final String name = "Chickenware";
	public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList();
	public static AltManager altManager = new AltManager();
	public static chickenware.ui.clickgui.ClickGui ClickGui = new chickenware.ui.clickgui.ClickGui();
	public static Hud hud = new Hud();
	public static void setup() 
	{
		System.out.println("[ " + name + " Client ] Client launched.");
		Display.setTitle(name);

		modules.add(new AimAssist());
		modules.add(new Animations());
		modules.add(new AntiBot());
		modules.add(new AntiVoid());
		modules.add(new AutoArmor());
		modules.add(new AutoClicker());
		modules.add(new AutoDisable());
		modules.add(new AutoTool());
		modules.add(new BedNuker());
		modules.add(new Chams());
		modules.add(new ChestSteal());
		modules.add(new chickenware.modules.render.ClickGui());
		modules.add(new Damage());
		modules.add(new ESP());
		modules.add(new Fly());
		modules.add(new FullBright());
		modules.add(new GuiMove());
		modules.add(new Hitbox());
		modules.add(new KeepSprint());
		modules.add(new Killaura());
		modules.add(new LongJump());
		modules.add(new LowFire());
		modules.add(new NameTags());
		modules.add(new NoHurtCam());
		modules.add(new NoRotate());
		modules.add(new Panic());
		modules.add(new Parkour());
		modules.add(new Reach());
		modules.add(new Refill());
		modules.add(new SafeWalk());
		modules.add(new chickenware.modules.blatant.Scaffold());
		modules.add(new chickenware.modules.utility.Scaffold());
		modules.add(new Speed());
		modules.add(new Sprint());
		modules.add(new TargetStrafe());
		modules.add(new TextGUI());
		modules.add(new ThrowPot());
		modules.add(new Timer());
		modules.add(new Tracer());
		modules.add(new VClip());
		modules.add(new ViewModel());
		modules.add(new Velocity());
		modules.add(new WTap());
	}
	public static void shutdown() {}
	public static Module getModuleByName(String s)
	{
		for(Module m: getModule()) 
		{
            if (s == m.getName()) 
            {
            	return m;
            }
        }
		return null;
	}
	public static Module isEnabled(String s)
	{
		for(Module m: getModule()) 
		{
            if (s == m.getName()) 
            {
            	if(m.isToggled())
            	{
            		return m;
            	}
            }
        }
		return null;
	}
	public static void onEvent(Event e) 
	{
		for(Module m: modules) 
		{
			m.onEvent2(e);
            if (!m.toggled || !nullCheck()) 
            {
            	continue;
            }
            m.onEvent(e);
        }
    }
	public static void keyPress(int key) 
	{
		EventKey e = new EventKey(key);
		Client.onEvent(e);
        if(!e.cancelled) 
        {
        	for(Module m: modules) 
        	{
        		if (m.getKey() == key) 
        		{
        			m.toggle();
        		}
        	}
        }
    }
	public static CopyOnWriteArrayList<Module> getModule() 
	{
        return modules;
    }
    public static List<Module> getModuleByCategory(Category c) 
    {
    	List<Module> mods = new ArrayList<Module>();
        for(Module m: modules) 
        {
        	if (m.category == c) 
        	{	
        		mods.add(m);
    		}
    	}
        return mods;
    }
    public static void addChatMessage(String s) 
    {
        mc.thePlayer.addChatMessage(new ChatComponentText("��f��l" + name + " ��f��l>> ��r��l" + s));
    }
    public static boolean nullCheck() 
    {
    	return (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null);
    }
}