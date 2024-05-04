package pyzpre.createbicyclesbitterballen.block.mechanicalfryer;

import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.ViewOnlyWrappedStorageView;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pyzpre.createbicyclesbitterballen.index.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import pyzpre.createbicyclesbitterballen.index.SoundsRegistry;


import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class MechanicalFryerEntity extends FryerOperatingBlockEntity implements SidedStorageBlockEntity {
    private static final Object DeepFryingRecipesKey = new Object();
    private boolean shouldRecalculateProcessingTicks;
    public SmartInventory inputInv;
    public SmartInventory outputInv;
    public FryerInventoryHandler capability;
    public int timer;
    private DeepFryingRecipe lastRecipe;
    public int runningTicks;
    public int processingTicks;
    public boolean running;


    public MechanicalFryerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv  = new SmartInventory(1, this);
        outputInv = new SmartInventory(999, this);
        capability = new FryerInventoryHandler();
        shouldRecalculateProcessingTicks = true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        super.addBehaviours(behaviours);
    }

    public float getRenderedHeadOffset(float partialTicks) {
        int localTick;
        float offset = 0;
        if (running && speed != 0) {
            if (runningTicks < 20) {
                localTick = runningTicks;
                float num = (localTick + partialTicks) / 20f;
                num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);
                offset = num - .5f;
            } else if (runningTicks <= 20) {
                offset = 1;
            } else {
                localTick = 40 - runningTicks;
                float num = (localTick - partialTicks) / 20f;
                num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);
                offset = num - .5f;
            }
        }
        return offset + 7 / 16f;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, -1.5, 0);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        timer = compound.getInt("Timer");
        inputInv.deserializeNBT(compound.getCompound("InputInventory"));
        outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
        shouldRecalculateProcessingTicks = compound.getBoolean("ShouldRecalculate");
        super.read(compound, clientPacket);

        if (clientPacket && hasLevel())
            getBasin().ifPresent(bte -> bte.setAreFluidsMoving(running && runningTicks <= 20));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        compound.putInt("Timer", timer);
        compound.put("InputInventory", inputInv.serializeNBT());
        compound.put("OutputInventory", outputInv.serializeNBT());
        compound.putBoolean("ShouldRecalculate", shouldRecalculateProcessingTicks); // Serialize the flag
        super.write(compound, clientPacket);
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
        // Return the same item storage for any side
        return capability;  // Replace 'itemStorage' with your combined input/output item storage instance
    }


    @Override
    public void tick() {
        super.tick();


        float speed = Math.abs(getSpeed());
        boolean canStartProcessing = hasMatchingRecipe();
        float recipeSpeed = 1;



        if (getSpeed() == 0) {
            if (running) { // If it was running, reset the state
                resetAnimationAndProcessing();
            }
        }

        if (!running && canStartProcessing) {
            running = true;
            runningTicks = 0;
            shouldRecalculateProcessingTicks = true;

            if (currentRecipe instanceof ProcessingRecipe) {
                int t = ((ProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                if (t != 0) {
                    recipeSpeed = t / 100f;
                }

            }
        }

        if (running) {
            if (processingTicks > 0) {
                if (runningTicks < 20) {
                    runningTicks++;
                }
                processingTicks--;
            } else {
                if (runningTicks < 40) {
                    runningTicks++;
                } else {
                    if (!canStartProcessing) {
                        running = false;
                        runningTicks = 0;
                        shouldRecalculateProcessingTicks = false;
                    } else {
                        shouldRecalculateProcessingTicks = true;
                        runningTicks = 0;
                    }
                }
            }
            if (runningTicks == 20 && processingTicks == 1) {
                Storage<ItemVariant> itemStorage = inputInv;  // Assuming inputInv is a Storage<ItemVariant>

                try (Transaction transaction = Transaction.openOuter()) {
                    final boolean[] foundIce = {false};

                    itemStorage.forEach((view) -> {
                        ItemStack stack = view.getResource().toStack((int) view.getAmount());
                        if (isIce(stack)) {
                            // Cause the explosion if ice is found
                            if (!foundIce[0]) {  // Ensure explosion only once
                                causeExplosion();
                                foundIce[0] = true;
                            }

                            // Attempt to remove the ice from the storage
                            long extracted = view.extract(view.getResource(), view.getAmount(), transaction);
                            if (extracted > 0) {
                                // Commit changes inside if block if you want to continue checking others
                                // Or just handle the ice without breaking because forEach cannot break early
                            }
                        }
                        // There's no return statement needed; continue checking all items
                    });

                    if (foundIce[0]) {
                        transaction.commit();  // Commit the transaction outside the forEach if ice was found and processed
                    }
                }
            }

            if (runningTicks == 20) {
                if (processingTicks == 1) {
                    Optional<BasinBlockEntity> basinOpt = getBasin();
                    basinOpt.ifPresent(basin -> {
                        if (lastRecipe == null || !lastRecipe.matches(inputInv, level)) {
                            Optional<DeepFryingRecipe> recipe = RecipeRegistry.DEEP_FRYING.find(inputInv, level);
                            if (!recipe.isPresent())
                                return;
                            lastRecipe = recipe.get();
                        }
                        if (lastRecipe != null && DeepFryingRecipe.apply(inputInv, basin, this, lastRecipe)) {
                            try (Transaction t = TransferUtil.getTransaction()) {
                                ItemStackHandlerSlot slot = inputInv.getSlot(0);
                                slot.extract(slot.getResource(), 1, t);

                                t.commit();
                                sendData();
                                setChanged();
                            }
                        }
                    });
                }


                if (runningTicks == 20 && shouldRecalculateProcessingTicks) {
                    if (canStartProcessing) {
                        if (lastRecipe != null) {
                            int duration = lastRecipe.getProcessingDuration();
                            if (duration != 0) recipeSpeed = duration / 100f;
                        }
                        processingTicks = Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512);
                        shouldRecalculateProcessingTicks = false;
                    }
                }

            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private boolean isIce(ItemStack stack) {
        TagKey<Item> iceTag = TagKey.create(Registries.ITEM, new ResourceLocation("create_bic_bit", "ice"));
        return stack.is(iceTag);
    }

    private void resetAnimationAndProcessing() {
        running = false;
        processingTicks = 0;
        runningTicks = 0;
    }


    private void grantAdvancementCriterion(ServerPlayer player) {
        PlayerAdvancements playerAdvancements = player.getAdvancements();
        Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation("create_bic_bit:fry_about_it"));

        if (advancement != null && advancement.getCriteria().containsKey("ice_exploded")) {
            AdvancementProgress advancementProgress = playerAdvancements.getOrStartProgress(advancement);

            if (!advancementProgress.isDone()) {
                playerAdvancements.award(advancement, "ice_exploded");
            }
        }
    }

    private void causeExplosion() {
        if (!level.isClientSide()) {
            level.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 4.0F, false, Level.ExplosionInteraction.MOB);
            double radius = 10.0;
            AABB area = new AABB(worldPosition).inflate(radius);
            List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, area);
            for (ServerPlayer player : players) {
                grantAdvancementCriterion(player);
            }
        }
    }


    private boolean hasMatchingRecipe() {
        if (inputInv.isEmpty()) {
            return false;
        }

        Optional<DeepFryingRecipe> recipeOpt = findMatchingRecipeForItem(inputInv.getStackInSlot(0), level);
        if (recipeOpt.isEmpty()) {
            return false;
        }

        Optional<BasinBlockEntity> basinOpt = getBasin();
        if (basinOpt.isEmpty()) {
            return false;
        }

        DeepFryingRecipe recipe = recipeOpt.get();
        if (!areBasinFluidsMatching(basinOpt.get(), recipe)) {
            return false;
        }

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (!isBlazeBurnerConfigured(requiredHeat)) {
            return false;
        }

        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    private boolean isBlazeBurnerConfigured(HeatCondition requiredHeat) {

        if (requiredHeat == HeatCondition.NONE) {
            return true;
        }

        if (level == null) {
            return false;
        }

        BlockPos posBelowBasin = worldPosition.below(3);
        BlockState blockStateBelow = level.getBlockState(posBelowBasin);


        if (!(blockStateBelow.getBlock() instanceof BlazeBurnerBlock)) {
            return false;
        }

        BlazeBurnerBlock.HeatLevel actualHeat = BlazeBurnerBlock.getHeatLevelOf(blockStateBelow);

        return requiredHeat.testBlazeBurner(actualHeat);
    }

    private Optional<DeepFryingRecipe> findMatchingRecipeForItem(ItemStack stack, Level level) {
        for (Recipe<?> recipe : level.getRecipeManager().getAllRecipesFor(RecipeRegistry.DEEP_FRYING.getType())) {
            if (recipe instanceof DeepFryingRecipe deepFryingRecipe && deepFryingRecipe.matches(inputInv, level)) {
                return Optional.of(deepFryingRecipe);
            }
        }
        return Optional.empty();
    }

    private boolean areBasinFluidsMatching(BasinBlockEntity basin, DeepFryingRecipe recipe) {
        // Get Fluid Storage capability from basin
        Storage<FluidVariant> availableFluids = basin.getFluidStorage(null);

        if (availableFluids == null) {
            return false;
        }

        for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
            long totalFluidAmount = 0;

            try (Transaction transaction = Transaction.openOuter()) {
                // Iterate directly over the storage views provided by the storage object
                for (StorageView<FluidVariant> view : availableFluids) {
                    // Check if the storage view is not empty and the fluid matches the ingredient
                    if (!view.isResourceBlank() && fluidIngredient.test(new FluidStack(view.getResource().getFluid(), 1))) {
                        // If we are not simulating, extract the fluid
                        long extracted = view.extract(view.getResource(), fluidIngredient.getRequiredAmount(), transaction);
                        if (extracted > 0) {
                            totalFluidAmount += extracted;
                            // If we've satisfied the ingredient requirement, we can break out of the loop
                            if (totalFluidAmount >= fluidIngredient.getRequiredAmount()) {
                                break;
                            }
                        }
                    }
                }

                if (totalFluidAmount < fluidIngredient.getRequiredAmount()) { // Assuming getRequiredAmount() is in bucket units
                    return false;
                }
            }
        }

        return true;
    }


    public void renderParticles() {
        Optional<BasinBlockEntity> basin = getBasin();
        if (basin.isEmpty() || level == null)
            return;

        for (SmartFluidTankBehaviour behaviour : basin.get()
                .getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }

    protected void spillParticle(ParticleOptions data) {
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.25f);
        offset = VecHelper.rotate(offset, angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Direction.Axis.Y)
                .add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == RecipeRegistry.DEEP_FRYING.getType();
    }

    @Override
    public void startProcessingBasin() {
        if (running && runningTicks <= 20)
            return;
        super.startProcessingBasin();
        running = true;
        runningTicks = 0;
    }

    @Override
    protected void onBasinRemoved() {
        if (!running)
            return;
        runningTicks = 40;
        running = false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return DeepFryingRecipesKey;
    }

    @Override
    protected boolean isRunning() {
        return running;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        boolean slow = Math.abs(getSpeed()) < 65;
        if (slow && AnimationTickHolder.getTicks() % 2 == 0)
            return;

        if (runningTicks == 20) {
            SoundsRegistry.FRYING.playAt(level, worldPosition, .75f, 1, true);
            renderParticles();
        }
    }


    public boolean canProcess(ItemStack stack) {
        if (lastRecipe != null && isItemValidForRecipe(lastRecipe, stack)) {
            return true;
        }
        List<DeepFryingRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(RecipeRegistry.DEEP_FRYING.get());
        for (DeepFryingRecipe recipe : recipes) {
            if (isItemValidForRecipe(recipe, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemValidForRecipe(DeepFryingRecipe recipe, ItemStack stack) {
        // This method needs to check if the ItemStack is valid for the given recipe.
        // This is a simplistic approach; you'll likely need to extend it based on your actual recipe requirements.
        return recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack));
    }


    public class FryerInventoryHandler extends CombinedStorage<ItemVariant, ItemStackHandler> {

        public FryerInventoryHandler() {
            super(List.of(inputInv, outputInv));
        }

        @Override
        public long insert(@NotNull ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (canProcess(resource.toStack())) {
                long inserted = inputInv.insert(resource, maxAmount, transaction);
                return inserted;
            }
            return 0;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            long extracted = outputInv.extract(resource, maxAmount, transaction);
            return extracted;
        }

        @Override
        public Iterator<StorageView<ItemVariant>> iterator() {
            return new FryerInventoryHandlerIterator();
        }

        private class FryerInventoryHandlerIterator implements Iterator<StorageView<ItemVariant>> {
            private boolean output = true;
            private Iterator<StorageView<ItemVariant>> wrapped;

            public FryerInventoryHandlerIterator() {
                wrapped = outputInv.iterator();
            }

            @Override
            public boolean hasNext() {
                return wrapped.hasNext();
            }

            @Override
            public StorageView<ItemVariant> next() {
                if (!output && !wrapped.hasNext()) {
                    wrapped = inputInv.iterator();
                    output = false;
                }
                StorageView<ItemVariant> view = wrapped.next();
                if (!output) view = new ViewOnlyWrappedStorageView<>(view);
                return view;
            }
        }
    }
}
