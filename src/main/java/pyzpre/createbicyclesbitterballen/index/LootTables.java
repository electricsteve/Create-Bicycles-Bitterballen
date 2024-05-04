package pyzpre.createbicyclesbitterballen.index;


import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTables {
    public static final ResourceLocation FISHING_LOOT_TABLE_ID = new ResourceLocation("minecraft", "gameplay/fishing");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (FISHING_LOOT_TABLE_ID.equals(id)) {
                // Correctly setting up the LootItem entry.
                var entry = LootItem.lootTableItem(CreateBicBitModItems.RAW_HERRING.get())
                        .setWeight(1) // Lower weight for rarity
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                        .when(LootItemRandomChanceCondition.randomChance(0.2F)); // 20% chance to appear

                var poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(entry);

                // Adding the built pool to the table builder.
                tableBuilder.withPool(poolBuilder); // The adjustment is here, passing the builder directly.
            }
        });

    }
}


