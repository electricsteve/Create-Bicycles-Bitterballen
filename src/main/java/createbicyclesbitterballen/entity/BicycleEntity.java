package createbicyclesbitterballen.entity;

import createbicyclesbitterballen.index.EntityTypeRegistry;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BicycleEntity extends Entity {
    private static final Logger LOGGER = LogManager.getLogger();
    private int health = 2000;

    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputForward;

    public BicycleEntity(EntityType<? extends Entity> entityType, Level world) {
        super(entityType, world);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.health = compound.getInt("Health");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Health", this.health);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPushable() {
        return true;
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public boolean hasSpaceForPassengers() {
        return (this.getPassengers().size() < 2);
    }

    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide()) {
            if (!this.hasSpaceForPassengers()) {
                return InteractionResult.PASS;
            }

            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();

        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0)); // Simulate gravity

        if (this.isVehicle()) {
            LivingEntity controllingPassenger = this.getControllingPassenger();
            if (controllingPassenger != null) {
                this.setYRot(controllingPassenger.getYRot());

                // Set the bicycle's pitch for a complete match with the player's look direction (optional)
                // this.setXRot(controllingPassenger.getXRot());

                Vec3 controlVec = this.getControlVector();
                Vec3 forwardVec = Vec3.directionFromRotation(0, this.getYRot());
                Vec3 vec3 = this.getDeltaMovement();

                // Deceleration factor for natural slowdown
                final double deceleration = 0.78;

                // Acceleration factor for forward movement
                final double acceleration = controlVec.z > 0 ? 0.1 : controlVec.z < 0 ? -0.1 : 0;

                vec3 = vec3.add(forwardVec.scale(acceleration));

                // Apply natural slowdown or deceleration when there's no input
                vec3 = vec3.multiply(deceleration, 1.0, deceleration);

                this.setDeltaMovement(vec3.x, vec3.y, vec3.z);
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
        if (this.isVehicle() && canClimbBlock()) {
            this.setPos(this.getX(), this.getY() + 1.0, this.getZ()); // Simple 1 block climb
        }

        // Apply movement
        this.move(MoverType.SELF, this.getDeltaMovement());
    }
    private boolean canClimbBlock() {
        Direction direction = this.getMotionDirection();
        LOGGER.debug("Checking if can climb: Current motion direction: {}", direction);

        // Obtain the current precise position of the entity
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        LOGGER.debug("Entity precise position: x={}, y={}, z={}", x, y, z);

        // Obtain the entity's block position
        BlockPos currentPosition = this.blockPosition();
        LOGGER.debug("Entity block position: {}", currentPosition);

        // Calculate the BlockPos directly ahead
        BlockPos blockPosAhead = currentPosition.relative(direction);
        LOGGER.debug("Block position directly ahead: {}", blockPosAhead);

        AABB entityBox = this.getBoundingBox();
        boolean isTouchingBlockAhead = false;
        // Adjusted threshold for clarity and possible bug avoidance
        final double touchingThreshold = 1;
        switch (direction) {
            case NORTH:
                isTouchingBlockAhead = entityBox.minZ <= blockPosAhead.getZ() + 1 && entityBox.minZ > blockPosAhead.getZ();
                break;
            case SOUTH:
                isTouchingBlockAhead = blockPosAhead.getZ() - z < touchingThreshold;
                break;
            case WEST:
                isTouchingBlockAhead = x - blockPosAhead.getX() < touchingThreshold;
                break;
            case EAST:
                isTouchingBlockAhead = entityBox.maxX >= blockPosAhead.getX() && entityBox.maxX < blockPosAhead.getX() + 1;
                break;
        }
        LOGGER.debug("Is touching block ahead: {}", isTouchingBlockAhead);

        // Calculate the BlockPos above the block directly ahead
        BlockPos blockPosAbove = blockPosAhead.above();
        LOGGER.debug("Block position above the one directly ahead: {}", blockPosAbove);

        boolean isBlockAheadCollidable = this.level().getBlockState(blockPosAhead).canOcclude();
        boolean isSpaceAboveFree = !this.level().getBlockState(blockPosAbove).canOcclude();
        LOGGER.debug("Is block ahead collidable: {}, is space above free: {}", isBlockAheadCollidable, isSpaceAboveFree);

        boolean canClimb = isTouchingBlockAhead && isBlockAheadCollidable && isSpaceAboveFree;
        LOGGER.debug("Can climb: {}", canClimb);
        return canClimb;
    }





    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        LivingEntity livingentity1;
        if (entity instanceof LivingEntity livingentity) {
            livingentity1 = livingentity;
        } else {
            livingentity1 = null;
        }

        return livingentity1;
    }


    private Vec3 getControlVector() {
        LOGGER.info("Attempting to get control vector.");
        if (this.getControllingPassenger() == null) {
            LOGGER.info("No controlling passenger.");
            return Vec3.ZERO;
        }
        if (!(this.getControllingPassenger() instanceof Player)) {
            LOGGER.info("Controlling passenger is not a player.");
            return Vec3.ZERO;
        }
        Player player = (Player) this.getControllingPassenger();
        float forward = player.zza; // Positive when moving forward
        LOGGER.info("Direct Player Input - Forward: {}", forward);
        return new Vec3(0.0, 0.0, forward).normalize(); // Use dynamic forward movement based on input
    }



    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide && !this.isRemoved()) {
                this.health = (int) (this.health - amount);
                if (this.health <= 0) {
                    this.remove(RemovalReason.KILLED);
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }
}