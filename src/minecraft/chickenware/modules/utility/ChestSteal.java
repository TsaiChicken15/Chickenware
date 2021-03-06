package chickenware.modules.utility;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.ItemUtil;
import chickenware.utils.MSTimer;
import chickenware.utils.RandomUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class ChestSteal extends Module {
	public ChestSteal() {
		super("ChestSteal", Keyboard.KEY_NONE, Category.UTILITY, false, "Takes items upon opening a chest");
		this.addSettings(minDelayValue, maxDelayValue, autoCloseValue, minAutoCloseDelayValue, maxAutoCloseDelayValue, takeRandomlyValue,smartStealValue);
	}
	public static NumberSetting minDelayValue = new NumberSetting("MinDelay", 15, 0, 400, 1);
	public static NumberSetting maxDelayValue = new NumberSetting("MaxDelay", 20, 0, 400, 1);
	public static BooleanSetting autoCloseValue =  new BooleanSetting("AutoClose", false);
	public static NumberSetting minAutoCloseDelayValue = new NumberSetting("MinAutoCloseDelay", 0, 0, 400, 1);
	public static NumberSetting maxAutoCloseDelayValue = new NumberSetting("MaxAutoCloseDelay", 0, 0, 400, 1);
	public static BooleanSetting takeRandomlyValue =  new BooleanSetting("TakeRandomly", false);
	public static BooleanSetting smartStealValue =  new BooleanSetting("SmartSteal", true);
	private List<Integer> items = new ArrayList<Integer>();
	private MSTimer autoCloseTimer = new MSTimer();
	private MSTimer timer = new MSTimer();
	public void onEvent2(Event e)
	{
		if (e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(minDelayValue.get()) + ", " + String.valueOf(maxDelayValue.get());
			if(minDelayValue.get() > maxDelayValue.get()) 
			{
				double temp = minDelayValue.get();
				minDelayValue.setValue(maxDelayValue.get());
				maxDelayValue.setValue(temp);
			}
		}
	}
	public void onEvent(Event e) 
	{
        if (e instanceof EventUpdate && e.isPRE() && mc.currentScreen instanceof GuiChest) 
        {
        	if(smartStealValue.get() && !findChest()) return;
            final GuiChest chest = (GuiChest) mc.currentScreen;
            if (autoCloseValue.enabled && 
            		autoCloseTimer.hasTimePassed(RandomUtil.randomInt((int)minAutoCloseDelayValue.get(), (int)maxAutoCloseDelayValue.get())) && 
            		(this.isChestEmpty(chest) || this.isInventoryFull())) {
                mc.thePlayer.closeScreen();
                return;
            }
            if(takeRandomlyValue.get())
            {
            	for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index)
            	{
	                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
	                if (isValidItem(stack) && chest.getLowerChestInventory().getStackInSlot(index) != null && !items.contains(index)) 
	                {
	                    items.add(index);
	                }
	            }
            	if(timer.hasTimePassed(RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get())) && !items.isEmpty())
            	{
            		int i = items.get(RandomUtil.randomInt(0, items.size()-1));
            		items.remove(items.indexOf(i));
            		mc.playerController.windowClick(chest.inventorySlots.windowId, i, 0, 1, mc.thePlayer);
                    timer.reset();
            	}
            }
            else
            {
	            for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index)
	            {
	                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
	                if (isValidItem(stack) && chest.getLowerChestInventory().getStackInSlot(index) != null && timer.hasTimePassed(RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get()))) 
	                {
	                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
	                    timer.reset();
	                    break;
	                }
	            }
            }
        }
    }    
	
	public static Boolean findChest() 
	{
		for (EnumFacing face1 : EnumFacing.VALUES) 
		{
			BlockPos playerPos = mc.thePlayer.getPosition();
			if (mc.theWorld.getBlockState(playerPos.offset(face1)).getBlock() instanceof BlockChest && mc.thePlayer.getDistanceSq(playerPos.offset(face1)) <= 3) 
			{
				return true;
			}

			for (EnumFacing face2 : EnumFacing.VALUES) 
			{

				BlockPos pos2 = playerPos.offset(face2);

				if (mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockChest && mc.thePlayer.getDistanceSq(playerPos.offset(face1)) <= 3) 
				{
					return true;
				}

				for (EnumFacing face3 : EnumFacing.VALUES) 
				{

					BlockPos pos3 = pos2.offset(face3);

					if (mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockChest && mc.thePlayer.getDistanceSq(playerPos.offset(face1)) <= 3) 
					{
						return true;
					}

					for (EnumFacing face4 : EnumFacing.VALUES) 
					{

						BlockPos pos4 = pos3.offset(face4);

						if (mc.theWorld.getBlockState(pos4).getBlock() instanceof BlockChest && mc.thePlayer.getDistanceSq(playerPos.offset(face1)) <= 3) 
						{
							return true;
						}

						for (EnumFacing face5 : EnumFacing.VALUES) 
						{

							BlockPos pos5 = pos4.offset(face5);

							if (mc.theWorld.getBlockState(pos5).getBlock() instanceof BlockChest && mc.thePlayer.getDistanceSq(playerPos.offset(face1)) <= 3) 
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
    private boolean isValidItem(final ItemStack stack) {
        return stack != null && ((ItemUtil.compareDamage(stack, ItemUtil.bestSword()) != null
                && ItemUtil.compareDamage(stack, ItemUtil.bestSword()) == stack) || stack.getItem() instanceof ItemBlock
                || (stack.getItem() instanceof ItemPotion && !ItemUtil.isBadPotion(stack))
                || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemAppleGold
                || stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemEnderPearl);
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null && this.isValidItem(stack)) {
                return false;
            }
        }
        autoCloseTimer.reset();
        return true;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        autoCloseTimer.reset();
        return true;
    }
}