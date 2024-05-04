package pyzpre.createbicyclesbitterballen.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import pyzpre.createbicyclesbitterballen.index.CreateBicBitModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WrappedKetchup extends Item {
    public WrappedKetchup(Properties p_41383_) {
        super(p_41383_);
    }
    @Override
    public void appendHoverText(ItemStack itemstack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, tooltip, flag);
        tooltip.add(Component.literal("§9Fire Resistance (0:10)"));
    }
    @Override
    public int getUseDuration(@Nonnull ItemStack itemstack) {
        return 25;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        ItemStack retval = new ItemStack(CreateBicBitModItems.DIRTY_PAPER);
        super.finishUsingItem(itemstack, world, entity);
        if (itemstack.isEmpty()) {
            return retval;
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(retval))
                    player.drop(retval, false);
            }
            return itemstack;
        }
    }
}
