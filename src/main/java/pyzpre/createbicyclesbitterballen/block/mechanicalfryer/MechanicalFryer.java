package pyzpre.createbicyclesbitterballen.block.mechanicalfryer;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import pyzpre.createbicyclesbitterballen.index.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalFryer extends HorizontalKineticBlock implements IBE<MechanicalFryerEntity> {
    public MechanicalFryer(Properties properties) {
        super(properties);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()));
    }
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(handIn);

        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;  // Only proceed on the server side.
        }

        withBlockEntityDo(worldIn, pos, fryer -> {
            if (!player.getItemInHand(handIn).isEmpty() && fryer.canProcess(player.getItemInHand(handIn))) {

                ItemStack stackInSlot = fryer.inputInv.getItem(0); // Get the current item in the slot

                if (stackInSlot.isEmpty()) {
                    // If the slot is empty, directly set the item in the slot
                    fryer.inputInv.setItem(0, itemInHand.copy());
                    player.setItemInHand(handIn, ItemStack.EMPTY); // Clear the player's hand
                }  else if (ItemStack.isSameItemSameTags(itemInHand, stackInSlot)) {
                    // If the item can stack with the current item in the slot, increase the count
                    int accept = Math.min(itemInHand.getCount(), stackInSlot.getMaxStackSize() - stackInSlot.getCount());
                    stackInSlot.grow(accept);
                    itemInHand.shrink(accept);

                    if (itemInHand.isEmpty()) {
                        player.setItemInHand(handIn, ItemStack.EMPTY); // Clear the player's hand if all items were moved
                    } else {
                        player.setItemInHand(handIn, itemInHand); // Set the remaining items back to the player's hand
                    }
                }

                fryer.setChanged(); // Mark the block entity as changed
                fryer.sendData(); // Send updates to clients
            }

            // If the output inventory is not empty, allow the player to retrieve items.
            boolean emptyOutput = true;
            ItemStackHandler outputInv = fryer.outputInv;
            for (int slot = 0; slot < outputInv.getSlotCount(); slot++) {
                ItemStack stackInSlot = outputInv.getStackInSlot(slot);
                if (!stackInSlot.isEmpty()) {
                    emptyOutput = false;
                    player.getInventory().placeItemBackInInventory(stackInSlot);
                    outputInv.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            // If the output was empty and the input inventory was not interacted with, check the input inventory.
            if (emptyOutput && itemInHand.isEmpty()) {
                ItemStackHandler inputInv = fryer.inputInv;
                for (int slot = 0; slot < inputInv.getSlotCount(); slot++) {
                    ItemStack stackInSlot = inputInv.getStackInSlot(slot);
                    player.getInventory().placeItemBackInInventory(stackInSlot);
                    inputInv.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            // Update the block entity to save changes.
            fryer.setChanged();
            fryer.sendData();
        });

        return InteractionResult.CONSUME;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext
                && ((EntityCollisionContext) context).getEntity() instanceof Player)
            return AllShapes.CASING_14PX.get(Direction.DOWN);

        return AllShapes.MECHANICAL_PROCESSOR_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide != null)
            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);
        return super.getStateForPlacement(context);
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public BlockEntityType<? extends MechanicalFryerEntity> getBlockEntityType() {
        return BlockEntityRegistry.MECHANICAL_FRYER.get();
    }

    @Override
    public Class<MechanicalFryerEntity> getBlockEntityClass() {
        return MechanicalFryerEntity.class;
    }


    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

}