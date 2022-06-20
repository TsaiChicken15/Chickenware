package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.NumberSetting;
import chickenware.utils.CombatUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedNuker extends Module {
	public BedNuker() 
	{
		super("BedNuker", Keyboard.KEY_NONE , Category.BLATANT, false, "");
		this.addSettings(rangeValue,smartNukeValue);
	}
	public static NumberSetting rangeValue = new NumberSetting("Range", 4 , 1 , 6 , 0.1);
	public static BooleanSetting smartNukeValue = new BooleanSetting("SmartNuke", true);
	static boolean nuking = false;
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(rangeValue.get());
		}
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventUpdate)
		{
			EventUpdate event = ((EventUpdate)e);
			
			BedInfo info = findBed();
	
			BlockPos bed = info.pos;
			EnumFacing bedFace = info.face;
	
			if (bed == null || bedFace == null) 
			{
				nuking = false;
				return;
			}
	
			if(smartNukeValue.get() && Scaffold.scaffolding)
				return;
			
			nuking = true;
			float[] rots = CombatUtil.getAngle(bed.getX(), bed.getY(), bed.getZ());
			//event.yaw = rots[0];
			//event.pitch = rots[1];
			CombatUtil.rotate(event, rots[0], rots[1]);
			mc.thePlayer.swingItem();
			mc.playerController.func_180512_c(bed, bedFace);
		}
	}

	public static BedInfo lastBed = null;

	public static BedInfo findBed() 
	{

		try 
		{

			if (lastBed != null && mc.thePlayer.getDistance(lastBed.pos.getX(), lastBed.pos.getY(), lastBed.pos.getZ()) <= rangeValue.get()) 
			{
				if (mc.theWorld.getBlockState(lastBed.pos).getBlock() instanceof BlockBed) 
				{
					return lastBed;
				}
			}

		} catch (Exception e) {

		}

		BlockPos bed = null;
		EnumFacing bedFace = null;

		for (EnumFacing face1 : EnumFacing.VALUES) 
		{
			BlockPos playerPos = mc.thePlayer.getPosition();
			if (mc.theWorld.getBlockState(playerPos.offset(face1)).getBlock() instanceof BlockBed) 
			{

				bed = playerPos.offset(face1);
				bedFace = face1.getOpposite();
				break;

			}

			for (EnumFacing face2 : EnumFacing.VALUES) 
			{

				BlockPos pos2 = playerPos.offset(face2);

				if (mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockBed) 
				{

					bed = pos2;
					bedFace = face2.getOpposite();
					break;

				}

				for (EnumFacing face3 : EnumFacing.VALUES) 
				{

					BlockPos pos3 = pos2.offset(face3);

					if (mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockBed) 
					{

						bed = pos3;
						bedFace = face3.getOpposite();
						break;

					}

					for (EnumFacing face4 : EnumFacing.VALUES) 
					{

						BlockPos pos4 = pos3.offset(face4);

						if (mc.theWorld.getBlockState(pos4).getBlock() instanceof BlockBed) 
						{

							bed = pos4;
							bedFace = face4.getOpposite();
							break;

						}

						for (EnumFacing face5 : EnumFacing.VALUES) 
						{

							BlockPos pos5 = pos4.offset(face5);

							if (mc.theWorld.getBlockState(pos5).getBlock() instanceof BlockBed) 
							{

								bed = pos5;
								bedFace = face5.getOpposite();
								break;

							}

						}

					}

				}

			}

		}

		lastBed = new BedInfo(bed, bedFace);
		return lastBed;


	}

	public static class BedInfo 
	{

		public BedInfo(BlockPos pos, EnumFacing face) 
		{
			this.pos = pos;
			this.face = face;
		}

		public BlockPos pos;
		public EnumFacing face;

	}
}
