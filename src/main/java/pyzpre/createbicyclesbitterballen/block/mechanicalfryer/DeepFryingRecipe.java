package pyzpre.createbicyclesbitterballen.block.mechanicalfryer;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import pyzpre.createbicyclesbitterballen.index.RecipeRegistry;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class DeepFryingRecipe extends ProcessingRecipe<SmartInventory> {

    public static boolean match(BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe) {
        FilteringBehaviour filter = basin.getFilter();
        if (filter == null)
            return false;

        boolean filterTest = filter.test(recipe.getResultItem(basin.getLevel()
                .registryAccess()));
        if (recipe instanceof BasinRecipe) {
            BasinRecipe basinRecipe = (BasinRecipe) recipe;
            if (basinRecipe.getRollableResults()
                    .isEmpty()
                    && !basinRecipe.getFluidResults()
                    .isEmpty())
                filterTest = filter.test(basinRecipe.getFluidResults()
                        .get(0));
        }

        if (!filterTest)
            return false;

        return apply(basin, fryer, recipe, true);
    }
    public static boolean apply(ItemStackHandlerContainer inputInv, BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe) {
        return apply(basin, fryer, recipe, false);
    }


    private static boolean apply(BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe, boolean test) {

        boolean isDeepFryingRecipe = recipe instanceof DeepFryingRecipe;
        ItemStackHandlerContainer availableItems = fryer.inputInv;
        Storage<FluidVariant> availableFluids = basin.getFluidStorage(null);


        if (availableItems == null || availableFluids == null) {
            return false;
        }

        BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(
                basin.getLevel().getBlockState(basin.getBlockPos().below(1))
        );


        if (isDeepFryingRecipe && !((DeepFryingRecipe) recipe).getRequiredHeat().testBlazeBurner(heat)) {
            return false;
        }

        ItemStack inputStack = availableItems.getStackInSlot(0);
        int itemCount = inputStack.getCount();

        if (itemCount <= 0) {
            return false;  // No items to process
        }

        List<FluidIngredient> fluidIngredients =
                isDeepFryingRecipe ? ((DeepFryingRecipe) recipe).getFluidIngredients() : Collections.emptyList();

        boolean simulate = false;
        do {
            for (int i = 0; i < itemCount; i++) {
                if (!test && !simulate) {
                    for (FluidIngredient fluidIngredient : fluidIngredients) {
                        if (!consumeFluids(fluidIngredient, availableFluids, false)) {
                            return false; // Failed to consume fluids, stop processing
                        }
                    }
                }

                if (!simulate) {
                    inputStack.shrink(1);  // Consume one item per cycle
                    List<ItemStack> recipeOutputItems = generateOutputs(recipe, basin, 1);  // Generate outputs per item
                    for (ItemStack itemStack : recipeOutputItems) {
                        if (!insertItemStacked(fryer.outputInv, itemStack)) {
                            return false;
                        }
                    }
                }
            }
            simulate = !simulate; // Toggle simulate after first real run
        } while (!test && simulate);

        return true;
    }
    private static boolean insertItemStacked(Storage<ItemVariant> storage, ItemStack stack) {
        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (inserted == stack.getCount()) {
                transaction.commit();
                return true;
            }
            return false;
        }
    }

    private static List<ItemStack> generateOutputs(Recipe<?> recipe, BasinBlockEntity basin, int quantity) {
        List<ItemStack> outputs = new ArrayList<>();
        if (recipe instanceof BasinRecipe) {
            // Ensure that each item in rollResults is multiplied by quantity
            for (ItemStack result : ((BasinRecipe) recipe).rollResults()) {
                ItemStack stack = result.copy();
                stack.setCount(result.getCount() * quantity);
                outputs.add(stack);
            }
        } else {
            ItemStack result = recipe.getResultItem(basin.getLevel().registryAccess()).copy();
            result.setCount(result.getCount() * quantity);
            outputs.add(result);
        }
        return outputs;
    }
    private static boolean consumeFluids(FluidIngredient fluidIngredient, Storage<FluidVariant> fluidStorage, boolean simulate) {
        long amountRequired = fluidIngredient.getRequiredAmount(); // Convert bucket units to droplets
        AtomicBoolean success = new AtomicBoolean(false);

        try (Transaction transaction = Transaction.openOuter()) {
            fluidStorage.forEach(view -> {
                FluidVariant variant = view.getResource();
                long currentAmount = view.getAmount(); // Amount currently in the view

                if (fluidIngredient.test(new FluidStack(variant.getFluid(), (int)(currentAmount / 81)))) { // Check if the fluid matches the ingredient's requirements
                    long extracted = view.extract(variant, amountRequired, transaction); // Attempt to extract the required amount

                    if (extracted == amountRequired) { // Check if the exact amount was extracted
                            success.set(true); // Set success to true if this is not a simulation

                    }
                }
            });

            if (success.get() && !simulate) {
                transaction.commit(); // Commit the transaction if not simulating and successful
                return true;
            }
        }
        return false;
    }







    protected DeepFryingRecipe(IRecipeTypeInfo type, ProcessingRecipeParams params) {
        super(type, params);
    }

    public DeepFryingRecipe(ProcessingRecipeParams params) {
        this(RecipeRegistry.DEEP_FRYING, params);
    }




    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    public boolean matches(ItemStackHandlerContainer inv, @Nonnull Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }


    @Override
    public boolean matches(SmartInventory inv, @Nonnull Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }
}
