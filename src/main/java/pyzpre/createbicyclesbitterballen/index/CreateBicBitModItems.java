
package pyzpre.createbicyclesbitterballen.index;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import pyzpre.createbicyclesbitterballen.effect.ModEffects;
import pyzpre.createbicyclesbitterballen.item.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

import static pyzpre.createbicyclesbitterballen.CreateBitterballen.REGISTRATE;

public class CreateBicBitModItems {
	public static final ItemEntry<ChocolateGlazedStroopwafelItem> CHOCOLATE_GLAZED_STROOPWAFEL = REGISTRATE.item("chocolate_glazed_stroopwafel", ChocolateGlazedStroopwafelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(9).saturationMod(0.7f).build()))
			.register();
	public static final ItemEntry<StroopwafelItem> STROOPWAFEL = REGISTRATE.item("stroopwafel", StroopwafelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<WrappedStroopwafelItem> WRAPPED_STROOPWAFEL = REGISTRATE.item("wrapped_stroopwafel", WrappedStroopwafelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.5f).meat().build()))
			.register();

	public static final ItemEntry<WrappedCoatedStroopwafelItem> WRAPPED_COATED_STROOPWAFEL = REGISTRATE.item("wrapped_chocolate_glazed_stroopwafel", WrappedCoatedStroopwafelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(9).saturationMod(0.7f).meat().build()))
			.register();
	public static final ItemEntry<UnbakedStroopwafelItem> UNBAKED_STROOPWAFEL = REGISTRATE.item("unbaked_stroopwafel", UnbakedStroopwafelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.3f).build()))
			.register();
	public static final ItemEntry<Item> SWEET_DOUGH = REGISTRATE.item("sweet_dough", Item::new).register();
	public static final ItemEntry<RawFrikandelItem> RAW_FRIKANDEL = REGISTRATE.item("raw_frikandel", RawFrikandelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.2f).meat().effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build()))
			.register();
	public static final ItemEntry<FrikandelItem> FRIKANDEL = REGISTRATE.item("frikandel", FrikandelItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.2f).meat().build()))
			.register();
	public static final ItemEntry<FrikandelSandwichItem> FRIKANDEL_SANDWICH = REGISTRATE.item("frikandel_sandwich", FrikandelSandwichItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(10).saturationMod(0.3f).meat().build()))
			.register();
	public static final ItemEntry<Item> CRUSHED_SUNFLOWER_SEEDS = REGISTRATE.item("crushed_sunflower_seeds", Item::new).register();
	public static final ItemEntry<SunflowerSeedsItem> SUNFLOWER_SEEDS = REGISTRATE.item("sunflower_seeds", SunflowerSeedsItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1f).build()))
			.register();
	public static final ItemEntry<Item> UNRIPE_CHEESE_WEDGE = REGISTRATE.item("unripe_cheese_wedge", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.3f).build()))
			.register();
	public static final ItemEntry<Item> YOUNG_CHEESE_WEDGE = REGISTRATE.item("young_cheese_wedge", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<Item> AGED_CHEESE_WEDGE = REGISTRATE.item("aged_cheese_wedge", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.8f).build()))
			.register();
	public static final ItemEntry<RoastedSunflowerSeedsItem> ROASTED_SUNFLOWER_SEEDS = REGISTRATE.item("roasted_sunflower_seeds", RoastedSunflowerSeedsItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.2f).build()))
			.register();
	public static final ItemEntry<StamppotBowlItem> STAMPPOT_BOWL = REGISTRATE.item("stamppot_bowl", StamppotBowlItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(10).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<SpeculaasItem> SPECULAAS = REGISTRATE.item("speculaas", SpeculaasItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(5).saturationMod(0.3f).build()))
			.register();
	public static final ItemEntry<RawBitterballenItem> RAW_BITTERBALLEN = REGISTRATE.item("raw_bitterballen", RawBitterballenItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.2f).meat().effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build()))
			.register();
	public static final ItemEntry<BitterballenItem> BITTERBALLEN = REGISTRATE.item("bitterballen", BitterballenItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.2f).meat().build()))
			.register();
	public static final ItemEntry<KroketItem> KROKET = REGISTRATE.item("kroket", KroketItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.2f).meat().build()))
			.register();
	public static final ItemEntry<KroketSandwichItem> KROKET_SANDWICH = REGISTRATE.item("kroket_sandwich", KroketSandwichItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(10).saturationMod(0.3f).meat().build()))
			.register();
	public static final ItemEntry<RawKroketItem> RAW_KROKET = REGISTRATE.item("raw_kroket", RawKroketItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.2f).meat().effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build()))
			.register();
	public static final ItemEntry<OliebollenItem> OLIEBOLLEN = REGISTRATE.item("oliebollen", OliebollenItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(5).saturationMod(0.2f).build()))
			.register();
	public static final ItemEntry<RawFriesItem> RAW_FRIES = REGISTRATE.item("raw_fries", RawFriesItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.3f).build()))
			.register();
	public static final ItemEntry<FriesItem> FRIES = REGISTRATE.item("fries", FriesItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<WrappedFriesItem> WRAPPED_FRIES = REGISTRATE.item("wrapped_fries", WrappedFriesItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<Item> RAW_CHURROS = REGISTRATE.item("raw_churros", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3f).build()))
			.register();
	public static final ItemEntry<Item> CHURROS = REGISTRATE.item("churros", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(5).saturationMod(0.6f).build()))
			.register();

	public static final ItemEntry<WrappedChurrosItem> WRAPPED_CHURROS = REGISTRATE.item("wrapped_churros", WrappedChurrosItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(5).saturationMod(0.6f).build()))
			.register();
	public static final ItemEntry<Item> DIRTY_PAPER = REGISTRATE.item("dirty_paper", Item::new).register();

	public static final ItemEntry<Item> BASKET = REGISTRATE.item("andesite_basket", Item::new)
			.register();

	public static final ItemEntry<Item> FRYING_OIL_BUCKET = REGISTRATE.item("frying_oil_bucket", Item::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.register();
	public static final ItemEntry<Item> CURDLED_MILK_BUCKET = REGISTRATE.item("curdled_milk_bucket", Item::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.register();

	public static final ItemEntry<Item> KETCHUP_BUCKET = REGISTRATE.item("ketchup_bucket", Item::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.register();
	public static final ItemEntry<Item> MAYONNAISE_BUCKET = REGISTRATE.item("mayonnaise_bucket", Item::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.register();
	public static final ItemEntry<Item> CRUSHED_NETHERWART = REGISTRATE.item("crushed_nether_wart", Item::new).register();
	public static final ItemEntry<Item> RAW_HERRING = REGISTRATE.item("raw_herring", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.2f).meat().build()))
			.register();
	public static final ItemEntry<Item> COOKED_HERRING = REGISTRATE.item("cooked_herring", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.4f).build()))
			.register();
	public static final ItemEntry<Item> KRUIDNOTEN = REGISTRATE.item("kruidnoten", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.4f).build()))
			.register();

	public static final ItemEntry<WrappedKetchup> WRAPPED_KETCHUP_TOPPED_FRIES = REGISTRATE.item("wrapped_ketchup_topped_fries", WrappedKetchup::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(6).saturationMod(0.5f).effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<Ketchup> KETCHUP_TOPPED_FRIKANDEL_SANDWICH = REGISTRATE.item("ketchup_topped_frikandel_sandwich", Ketchup::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<Ketchup> KETCHUP_TOPPED_KROKET_SANDWICH = REGISTRATE.item("ketchup_topped_kroket_sandwich", Ketchup::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();
	public static final ItemEntry<WrappedMayo> WRAPPED_MAYONNAISE_TOPPED_FRIES = REGISTRATE.item("wrapped_mayonnaise_topped_fries", WrappedMayo::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(6).saturationMod(0.5f).effect( new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<Mayo> MAYONNAISE_TOPPED_FRIKANDEL_SANDWICH = REGISTRATE.item("mayonnaise_topped_frikandel_sandwich", Mayo::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect( new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<KetchupMayo> MAYONNAISE_TOPPED_KROKET_SANDWICH = REGISTRATE.item("mayonnaise_topped_kroket_sandwich", KetchupMayo::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).build()))
			.register();
	public static final ItemEntry<FrikandelSandwichItem> MAYONNAISE_KETCHUP_TOPPED_FRIKANDEL_SANDWICH = REGISTRATE.item("mayonnaise_ketchup_topped_frikandel_sandwich", FrikandelSandwichItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).effect( new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<KetchupMayo> MAYONNAISE_KETCHUP_TOPPED_KROKET_SANDWICH = REGISTRATE.item("mayonnaise_ketchup_topped_kroket_sandwich", KetchupMayo::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.3f).meat().effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).effect( new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<WrappedKetchupMayo> WRAPPED_MAYONNAISE_KETCHUP_TOPPED_FRIES = REGISTRATE.item("wrapped_mayonnaise_ketchup_topped_fries", WrappedKetchupMayo::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(6).saturationMod(0.5f).effect( new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1F).effect( new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1F).build()))
			.register();

	public static final ItemEntry<EnderballItem> ENDERBALL = REGISTRATE.item("enderball", EnderballItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().nutrition(10).saturationMod(0.10f).effect(new MobEffectInstance(ModEffects.UNANCHORED, 600, 0), 1F).build()))
			.register();
	public static final ItemEntry<MayoBottleItem> MAYONNAISE_BOTTLE = REGISTRATE.item("mayonnaise_bottle", MayoBottleItem::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.properties(p -> p.stacksTo(16))
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.2f).build()))
			.lang("Mayonnaise Bottle")
			.register();
	public static final ItemEntry<MayoBottleItem> KETCHUP_BOTTLE = REGISTRATE.item("ketchup_bottle", MayoBottleItem::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.properties(p -> p.stacksTo(16))
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.2f).build()))
			.lang("Ketchup Bottle")
			.register();
	public static final ItemEntry<FryingBottleItem> FRYING_OIL_BOTTLE = REGISTRATE.item("frying_oil_bottle", FryingBottleItem::new)
			.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
			.properties(p -> p.stacksTo(16))
			.properties(p -> p.food((new FoodProperties.Builder()).alwaysEat().effect(new MobEffectInstance(ModEffects.OILED_UP, 400, 0), 1F).effect(new MobEffectInstance(MobEffects.POISON, 200, 0), 1F).build()))
			.lang("Frying Oil Bottle")
			.register();
	public static final ItemEntry<WrappedChurrosItem> WRAPPED_COATED_CHURROS = REGISTRATE.item("wrapped_coated_churros", WrappedChurrosItem::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.8f).build()))
			.register();
	public static final ItemEntry<Item> COATED_CHURROS = REGISTRATE.item("coated_churros", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.8f).build()))
			.register();
	public static final ItemEntry<Item> RAW_EGGBALL = REGISTRATE.item("raw_eggball", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.2f).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build()))
			.register();
	public static final ItemEntry<Item> EGGBALL = REGISTRATE.item("eggball", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.2f).build()))
			.register();
	public static final ItemEntry<Item> RAW_CHEESE_SOUFFLE = REGISTRATE.item("raw_cheese_souffle", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.5f).build()))
			.register();
	public static final ItemEntry<Item> CHEESE_SOUFFLE = REGISTRATE.item("cheese_souffle", Item::new)
			.properties(p -> p.food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.5f).build()))
			.register();
	public static void register() {
	}
}



