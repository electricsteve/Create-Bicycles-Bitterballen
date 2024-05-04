package createbicyclesbitterballen.block.mechanicalfryer;


import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import createbicyclesbitterballen.index.RecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.*;



public class DeepFryingRecipe extends ProcessingRecipe<SmartInventory> {

    public static boolean match(BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe) {
        FilteringBehaviour filter = basin.getFilter();
        if (filter == null)
            return false;

        boolean filterTest = filter.test(recipe.getResultItem());
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
    public static boolean apply(SmartInventory inv, BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe) {
        return apply(basin, fryer, recipe, false);
    }


    private static boolean apply(BasinBlockEntity basin, MechanicalFryerEntity fryer, Recipe<?> recipe, boolean test) {
        boolean isDeepFryingRecipe = recipe instanceof DeepFryingRecipe;
        IItemHandler availableItems = fryer.inputInv;
        IFluidHandler availableFluids = basin.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);

        if (availableItems == null || availableFluids == null)
            return false;


        BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(basin.getLevel()
                .getBlockState(basin.getBlockPos().below(1)));
        if (isDeepFryingRecipe && !((DeepFryingRecipe) recipe).getRequiredHeat().testBlazeBurner(heat))
            return false;

        ItemStack inputStack = availableItems.getStackInSlot(0);
        int itemCount = inputStack.getCount(); // Get initial item count

        if (itemCount <= 0)
            return false;  // No items to process

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
                    // Generate and handle outputs
                    List<ItemStack> recipeOutputItems = generateOutputs(recipe, basin, 1);  // Generate outputs per item
                    for (ItemStack itemStack : recipeOutputItems) {
                        if (!ItemHandlerHelper.insertItemStacked(fryer.outputInv, itemStack, false).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            simulate = !simulate; // Toggle simulate after first real run
        } while (!test && simulate);

        return true;
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
            ItemStack result = recipe.getResultItem().copy();
            result.setCount(result.getCount() * quantity);
            outputs.add(result);
        }
        return outputs;
    }
    private static boolean consumeFluids(FluidIngredient fluidIngredient, IFluidHandler fluidHandler, boolean simulate) {
        for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
            FluidStack fluidInTank = fluidHandler.getFluidInTank(tank);
            if (fluidIngredient.test(fluidInTank)) {
                int amountRequired = fluidIngredient.getRequiredAmount();
                FluidStack drained = fluidHandler.drain(new FluidStack(fluidInTank, amountRequired), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
                return drained.getAmount() == amountRequired;  // Not enough fluid was drained.
// Required fluid was successfully consumed.
            }
        }
        return false;  // No matching fluid found in the tanks.
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

    @Override
    public boolean matches(SmartInventory inv, @Nonnull Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }


}
