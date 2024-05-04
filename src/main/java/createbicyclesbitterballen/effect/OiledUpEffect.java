package createbicyclesbitterballen.effect;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;


public class OiledUpEffect extends MobEffect {
    private static final Random random = new Random();
    private static final double DROP_ITEM_CHANCE = 0.01;
    private static final double FALL_OFF_LADDER_CHANCE = 0.02;

    public OiledUpEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Player player = (Player) entity;
        Level world = player.getCommandSenderWorld();
        if (entity instanceof Player) {
            maybeDropHeldItem(player, amplifier);
            maybeFallOffLadder(player);
            maybeFloatInWater(player);
            if (world.isRaining()) {
                applyLevitationEffect(player);
            }
        }
    }

    private void maybeDropHeldItem(Player player, int amplifier) {
        double baseDivisor = 2.0;
        double adjustedDropChance = DROP_ITEM_CHANCE / (baseDivisor + Math.sqrt(amplifier + 1));
        if (!player.getLevel().isClientSide) {
        if (random.nextDouble() < adjustedDropChance) {
            ItemStack itemStack = player.getMainHandItem();
            if (!itemStack.isEmpty()) {
                player.drop(itemStack, false, false);  // Drop the item immediately
                player.setItemInHand(player.getUsedItemHand(), ItemStack.EMPTY);  // Empty the hand
            }
        }
    }
    }
    private void maybeFloatInWater(Player player) {
        if (player.isInWater() && !player.getLevel().getBlockState(player.blockPosition().above()).isAir()) {
            // Apply an upward force if the player is in water and there's no air block directly above
            double upwardForce = 0.1;  // Adjust this value to control the strength of the float effect
            player.setDeltaMovement(player.getDeltaMovement().add(0, upwardForce, 0));
        }
    }
    private void applyLevitationEffect(Player player) {
        MobEffectInstance levitation = new MobEffectInstance(MobEffects.LEVITATION, 20, 0);
        player.addEffect(levitation);
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            grantAdvancementCriterion(serverPlayer, "create_bic_bit:step_3", "got_levitation");
        }
    }
    private void grantAdvancementCriterion(ServerPlayer player, String advancementID, String criterionKey) {
        PlayerAdvancements playerAdvancements = player.getAdvancements();
        Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation(advancementID));

        if (advancement != null && advancement.getCriteria().containsKey(criterionKey)) {
            AdvancementProgress advancementProgress = playerAdvancements.getOrStartProgress(advancement);

            if (!advancementProgress.isDone()) {
                playerAdvancements.award(advancement, criterionKey);
            }
        }
    }
    private void maybeFallOffLadder(Player player) {
        // Check if the block at player's feet or the block they are moving into is a ladder
        boolean isOnLadder = player.getLevel().getBlockState(player.blockPosition()).is(BlockTags.CLIMBABLE) ||
                player.getLevel().getBlockState(player.blockPosition().above()).is(BlockTags.CLIMBABLE);

        if (isOnLadder && random.nextDouble() < FALL_OFF_LADDER_CHANCE) {
            // Get the player's facing direction
            Vec3 lookVector = player.getLookAngle();
            // Apply a force opposite to the look direction (negating the vector), but only affecting the X and Z axes (horizontal push)
            Vec3 pushVector = new Vec3(-lookVector.x, 0, -lookVector.z).normalize().scale(0.15); // Scale determines the strength of the push
            player.setDeltaMovement(player.getDeltaMovement().add(pushVector));
        }
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
