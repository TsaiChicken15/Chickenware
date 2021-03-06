package chickenware.ui.clickgui;

import chickenware.ui.clickgui.button.Button;
import chickenware.utils.MCHook;
import net.minecraft.client.gui.Gui;

public class Panel extends Button implements MCHook
{
    protected int w;
    protected int h;
    public int x;
    public int y;
    public int color;
    public boolean hovered;

    public Panel(int x, int y, int widthIn, int heightIn, int color)
    {
        this.x = x;
        this.y = y;
        this.w = widthIn;
        this.h = heightIn;
        this.color = color;
    }
    public void draw(int mouseX, int mouseY) {
    	this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.w && mouseY < this.y + this.h;
    	Gui.drawRect(x, y, x + w, y + h, 0x90000000);
    	Gui.drawRect(x - 1, y - 1, x + w + 1, y, Button.color);
    	Gui.drawRect(x - 1, y + h, x + w + 1, y + h + 1, Button.color);
    	Gui.drawRect(x - 1, y - 1, x, y + h + 1, Button.color);
    	Gui.drawRect(x + w, y - 1, x + w + 1, y + h + 1, Button.color);
    }
}