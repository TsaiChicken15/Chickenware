package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventMove;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.settings.NewLine;
import chickenware.settings.NumberSetting;
import chickenware.utils.CombatUtil;
import chickenware.utils.ItemUtil;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MSTimer;
import chickenware.utils.MovementUtil;
import chickenware.utils.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class Scaffold extends Module
{
	public Scaffold() 
	{
		super("Scaffold",Keyboard.KEY_Z ,Category.BLATANT, true, "Assists with bridging");
		this.addSettings(minDelayValue,maxDelayValue,blockTimingValue,silentRotationValue,noSprintValue,timerValue,
				a,minTurnValue,maxTurnValue,swingValue,autoBlockValue,safewalkValue,sameYValue);
	}
	public static NumberSetting minDelayValue = new NumberSetting("MinDelay", 0, 0, 1000, 1);
	public static NumberSetting maxDelayValue = new NumberSetting("MaxDelay", 0, 0, 1000, 1);
	public static NumberSetting minTurnValue = new NumberSetting("MinTurn", 169, 1, 180, 1);
	public static NumberSetting maxTurnValue = new NumberSetting("MaxTurn", 180, 1, 180, 1);
	public static BooleanSetting silentRotationValue = new BooleanSetting("SilentRotation", true);
	public static BooleanSetting sameYValue =  new BooleanSetting("SameY", false);
	public static BooleanSetting noSprintValue =  new BooleanSetting("NoSprint", true);
	public static NumberSetting timerValue = new NumberSetting("Timer", 1f, 0f, 10f, 0.1);
	public static ModeSetting swingValue = new ModeSetting("Swing", "Normal", "Normal", "Packet");
	public static ModeSetting blockTimingValue = new ModeSetting("BlockTiming", "Pre", "Pre", "Post", "Both");
	public static ModeSetting autoBlockValue = new ModeSetting("AutoBlock", "Switch", "Switch", "Off");
	public static ModeSetting safewalkValue = new ModeSetting("Safewalk", "ZeroXZ", "Legit", "ZeroXZ","Off");
	private Vec3i[] vecs = new Vec3i[] {
			new Vec3i(0, 0, 0),
			new Vec3i(-1, 0, 0), 
			new Vec3i(1, 0, 0),
			new Vec3i(0, 0, 1), 
			new Vec3i(0, 0, -1),
			new Vec3i(0, -1, 0),
			new Vec3i(1, -1, 0), 
			new Vec3i(-1, -1, 0),
			new Vec3i(0, -1, -1), 
			new Vec3i(0, -1, 1),
			};
	private EnumFacing[] facings = new EnumFacing[] {
			EnumFacing.UP,
			EnumFacing.EAST,
			EnumFacing.WEST,
			EnumFacing.NORTH,
			EnumFacing.SOUTH
	};
	public static NewLine a = new NewLine();
	private BlockPos lastPlace;
	public MSTimer timer = new MSTimer();
	private long delay = RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get());
	private boolean doSpoof = false;
	static boolean scaffolding = false;
	private double launchY;
	private ItemStack itemStack;
	private int oldSlot = -1;
	private int slot = -1;
	public void onEnable() 
	{
		vecs = new Vec3i[] {
				new Vec3i(0, 0, 0),
				new Vec3i(-1, 0, 0), 
				new Vec3i(1, 0, 0),
				new Vec3i(0, 0, 1), 
				new Vec3i(0, 0, -1),
				new Vec3i(0, -1, 0),
				new Vec3i(1, -1, 0), 
				new Vec3i(-1, -1, 0),
				new Vec3i(0, -1, -1), 
				new Vec3i(0, -1, 1),
				};
		scaffolding = true;
		mc.timer.timerSpeed = (float) timerValue.get();
		launchY = mc.thePlayer.posY;
		{
			oldSlot = mc.thePlayer.inventory.currentItem;
			if(mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock &&
					!ItemUtil.isBadBlock(mc.thePlayer.inventory.getCurrentItem())) 
			{
		        itemStack = mc.thePlayer.inventory.getCurrentItem();
				return;
			}
			for (int index = 36; index <= 44; ++index) 
			{
	            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if(stack == null)
	            {
	            	continue;
	            }
	            if(autoBlockValue.is("Switch"))
	            {
		            if (stack.getItem() instanceof ItemBlock && !ItemUtil.isBadBlock(stack)) 
		            {
		            	KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[index - 36].getKeyCode());
		            	itemStack = mc.thePlayer.inventoryContainer.getSlot(index - 36).getStack();
		            	break;
		            }
	            }
	        }
		}
	}
	public void onDisable() 
	{
		itemStack = null;
		scaffolding = false;
		mc.timer.timerSpeed = 1;
		KeybindUtil.state(_gs.keyBindSneak.getKeyCode(), KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode()));
		if(autoBlockValue.is("Switch") && oldSlot > -1)
		{
			KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[oldSlot].getKeyCode());
		}
	}
	public void onEvent2(Event e)
    {
		this.additionalInfo = String.valueOf(minDelayValue.get()) + ", " + String.valueOf(maxDelayValue.get());
		if (minDelayValue.get() > maxDelayValue.get()) 
		{
	        double temp = minDelayValue.get();
	        minDelayValue.setValue(maxDelayValue.get());
	        maxDelayValue.setValue(temp);
	    }
		if (minTurnValue.get() > maxTurnValue.get()) 
		{
	        double temp = minTurnValue.get();
	        minTurnValue.setValue(maxTurnValue.get());
	        maxTurnValue.setValue(temp);
	    } 
    }
	
	public void onEvent(Event e) 
	{
		if(e instanceof EventMove)
		{
			if(safewalkValue.is("ZeroXZ"))
			{
				if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, -0.5D, mc.thePlayer.motionZ)).isEmpty() && 
						mc.thePlayer.onGround) 
				{
					((EventMove)e).isSafewalk = true;
				}
			}
			else if(safewalkValue.is("Legit"))
			{
				if(KeybindUtil.isDown(_gs.keyBindJump.getKeyCode()) && mc.thePlayer.movementInput.moveForward > 0) 
				{
					_gs.keyBindSneak.pressed = false;
				}
				for(double x = -0.25; x <= 0.25; x += 0.25) 
				{
					for(double z = -0.25; z <= 0.25; z += 0.25) 
					{
						if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x, 0.0D, z)).isEmpty() && 
								mc.thePlayer.onGround &&
								!KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode())) 
						{
							_gs.keyBindSneak.pressed = true;
							return;
						}
					}
				}
				if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.125D, 0.0D)).isEmpty() && 
						!KeybindUtil.isDown(_gs.keyBindSneak.getKeyCode())) 
				{
					_gs.keyBindSneak.pressed = false;
				}
			}
		}
		/*if(e instanceof EventYawHead)
		{
			if(lastPlace != null)
			{
				((EventYawHead)e).yaw = CombatUtil.getAngle(lastPlace)[0];
			}
		}*/
		if(e instanceof EventUpdate)
		{
			EventUpdate event = ((EventUpdate)e);
			if(silentRotationValue.get())
			{
				if(lastPlace != null)
				{
					//CombatUtil.rotate(event, mc.thePlayer.rotationYaw - 180, 82);
					if(mc.thePlayer.onGround)
						CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw, CombatUtil.getAngle(lastPlace)[0], RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), event.pitch + CombatUtil.updateRotation(event.pitch, 80, RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
					else if(MovementUtil.isMoving())
						CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw, CombatUtil.getAngle(lastPlace)[0], RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), event.pitch + CombatUtil.updateRotation(event.pitch, 84, RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
					else
						CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw, CombatUtil.getAngle(lastPlace)[0], RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), event.pitch + CombatUtil.updateRotation(event.pitch, 90, RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
				}
			}
		}
		if(e instanceof EventUpdate)
		{
			if((blockTimingValue.is("Pre") && !e.isPRE()) || (blockTimingValue.is("Post") && !e.isPOST())) return;
			if(noSprintValue.get())
			{
				mc.thePlayer.setSprinting(false);
			}
			
			BlockPos belowPlayer = new BlockPos(mc.thePlayer).offsetDown();
			
			
			if(!mc.theWorld.getBlockState(belowPlayer).getBlock().getMaterial().isReplaceable())
				return;			
					
			BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
			final BlockData data = getBlockData(underPos);
			
			if(timer.hasTimePassed(delay)) 
	        {
				if(mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock &&
						!ItemUtil.isBadBlock(mc.thePlayer.inventory.getCurrentItem())) 
				{
			        itemStack = mc.thePlayer.inventory.getCurrentItem();
				}
				else
				{
					if(!autoBlockValue.is("Off"))
		            {
						for (int index = 36; index <= 44; ++index) 				
						{
				            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
				            if(stack == null)
				            {
				            	continue;
				            }
				            if(autoBlockValue.is("Switch"))
				            {
					            if (mc.thePlayer.inventory.getCurrentItem() == null || !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock) || ItemUtil.isBadBlock(mc.thePlayer.inventory.getCurrentItem())) 
					            {	
					            	if((stack.getItem() instanceof ItemBlock) && !ItemUtil.isBadBlock(stack)) 
					            	{
						            	KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[index - 36].getKeyCode());
						            	itemStack = mc.thePlayer.inventoryContainer.getSlot(index - 36).getStack();
						            	break;
					            	}
					            }
				            }
						}
		            }
				}
				try
				{
					if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock && !ItemUtil.isBadBlock(mc.thePlayer.inventory.getCurrentItem()))
		            {
						for(double x = -0.25; x <= 0.25; x += 0.25) 
						{
							for(double z = -0.25; z <= 0.25; z += 0.25) 
							{
								if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(x, 0.0D, z)).isEmpty())
								{
									mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, itemStack, data.position, data.face, getVec3(data.position, data.face));
									runSwing();
									lastPlace = data.position;
								}
							}
						}
		            }
					delay = RandomUtil.randomInt((int)minDelayValue.get(), (int)maxDelayValue.get());
					timer.reset();
				}
				catch(NullPointerException ex)
				{
					
				}
	        }
		}
	}
	private void runSwing() 
	{
        if (swingValue.is("Packet")) 
        {
            mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
        } 
        else if (swingValue.is("Normal")) 
        {
            mc.thePlayer.swingItem();
        }
    }
	private void findBlock() {
		BlockPos pos;
        if (mc.thePlayer.posY == (int)mc.thePlayer.posY + 0.5) 
        {
	        pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	    }
        return;
    }
	public static Vec3 getVec3(BlockPos pos, EnumFacing face) 
	{
	    double x = pos.getX() + 0.5;
	    double y = pos.getY() + 0.9;
	    double z = pos.getZ() + 0.5;
	    x += (double) face.getFrontOffsetX() / 2;
	    z += (double) face.getFrontOffsetZ() / 2;
	    y += (double) face.getFrontOffsetY() / 2;
	    return new Vec3(x, y, z);
	}
    private class BlockData 
    {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) 
        {
            this.position = position;
            this.face = face;
        }
    }
    private BlockData getBlockData(BlockPos pos) 
    {
    	BlockPos p = pos;
    	BlockPos p1 = p;
    	for(Vec3i v: vecs)
    	{
    		p1 = p.add(v);
    		if(!sameYValue.get()) 
    		{
    			if (isPosSolid(p1.add(0,-1,0))) 
    	        {
    	            return new BlockData(p1.add(0,-1,0), facings[0]);
    	        }
	    		for(int i=1; i<5; i++)
	    		{
	    			if (isPosSolid(p1.add(vecs[i]))) 
	    	        {
	    	            return new BlockData(p1.add(vecs[i]), facings[i]);
	    	        }
	    		}
    		}
    		if(sameYValue.get())
    			for(int i=1; i<5; i++)
	    		{
	    			if (isPosSolid(p1.add(vecs[i]))) 
	    	        {
	    	            return new BlockData(p1.add(vecs[i]), facings[i]);
	    	        }
	    		}
    	}
        return null;
    }
	private boolean isPosSolid(BlockPos pos) 
	{
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		if ((block.getMaterial().isSolid() 
				|| !block.isTranslucent() 
				|| block.isNormalCube() 
				|| block instanceof BlockLadder 
				|| block instanceof BlockCarpet
				|| block instanceof BlockSnow 
				|| block instanceof BlockSkull)
				&& !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) 
		{
			return true;
		}
	    return false;
	}
}
