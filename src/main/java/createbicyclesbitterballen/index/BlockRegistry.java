package createbicyclesbitterballen.index;


import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import createbicyclesbitterballen.block.cheese.*;
import createbicyclesbitterballen.block.mechanicalfryer.MechanicalFryer;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import createbicyclesbitterballen.block.sunflower.SunflowerStem;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static createbicyclesbitterballen.CreateBicBitMod.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class BlockRegistry {
	public static final BlockEntry<MechanicalFryer> MECHANICAL_FRYER =
			REGISTRATE.block("mechanical_fryer", MechanicalFryer::new)
			.initialProperties(SharedProperties::copperMetal)
			.properties(p -> p.noOcclusion().strength(2.0f))
			.transform(pickaxeOnly())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(4.0))
			.item(AssemblyOperatorBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<UnripeCheeseBlock> UNRIPE_CHEESE =
			REGISTRATE.block("unripe_cheese", UnripeCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Unripe Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();
	public static final BlockEntry<WaxedUnripeCheeseBlock> WAXED_UNRIPE_CHEESE =
			REGISTRATE.block("waxed_unripe_cheese", WaxedUnripeCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Waxed Unripe Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();
	public static final BlockEntry<YoungCheeseBlock> YOUNG_CHEESE =
			REGISTRATE.block("young_cheese", YoungCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Young Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();
	public static final BlockEntry<WaxedYoungCheeseBlock> WAXED_YOUNG_CHEESE =
			REGISTRATE.block("waxed_young_cheese", WaxedYoungCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Waxed Young Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();
	public static final BlockEntry<AgedCheeseBlock> AGED_CHEESE =
			REGISTRATE.block("aged_cheese", AgedCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Aged Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();
	public static final BlockEntry<WaxedAgedCheeseBlock> WAXED_AGED_CHEESE =
			REGISTRATE.block("waxed_aged_cheese", WaxedAgedCheeseBlock::new)
					.initialProperties(() -> Blocks.CAKE)
					.properties(p -> p.sound(SoundType.WOOD))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Waxed Aged Cheese")
					.item(AssemblyOperatorBlockItem::new)
					.transform(customItemModel())
					.register();


	public static final BlockEntry<GlassBlock> CRYSTALLISED_OIL =
			REGISTRATE.block("crystallised_oil", GlassBlock::new)
					.properties(p -> p.lightLevel(s -> 10))
					.properties(p -> p.instrument(NoteBlockInstrument.HAT).strength(1F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((state, reader, pos, entity) -> false).isRedstoneConductor((state, world, pos) -> false) .isSuffocating((state, reader, pos) -> false).isViewBlocking((state, reader, pos) -> false))
					.transform(pickaxeOnly())
					.transform(BlockStressDefaults.setImpact(4.0))
					.item()
					.build()
					.lang("Crystallised Oil")
					.register();
	public static final BlockEntry<SunflowerStem> SUNFLOWERSTEM =
			REGISTRATE.block("sunflowerstem", SunflowerStem::new)
					.properties(p -> p.noOcclusion().strength(1.0f))
					.properties(p -> p.sound(SoundType.GRASS))
					.properties(p -> p.offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().instabreak().mapColor(MapColor.PLANT))
					.item()
					.build()
					.lang("Sunflower Stem")
					.register();


		public static void register() {

	}
}