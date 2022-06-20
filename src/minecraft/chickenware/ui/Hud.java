package chickenware.ui;

import java.util.ArrayList;
import java.util.Comparator;

import chickenware.Client;
import chickenware.events.listeners.EventRenderGUI;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.ui.clickgui.button.Button;
import chickenware.utils.MCHook;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class Hud implements MCHook
{
	public void draw() 
	{
	    Client.onEvent(new EventRenderGUI());
	}
}