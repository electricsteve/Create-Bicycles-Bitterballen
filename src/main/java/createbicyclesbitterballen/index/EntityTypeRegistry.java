package createbicyclesbitterballen.index;

import com.tterrag.registrate.util.entry.EntityEntry;
import createbicyclesbitterballen.CreateBicBitMod;
import createbicyclesbitterballen.entity.BicycleEntity;
import createbicyclesbitterballen.entity.render.BicycleEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class EntityTypeRegistry {
    public static void register() {
        EntityEntry<BicycleEntity> bicycle = CreateBicBitMod.REGISTRATE.entity("bicycle", BicycleEntity::new, MobCategory.MISC)
                .properties(o -> o.setTrackingRange(80).sized(1f,1f).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true))
                .register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> {
            event.enqueueWork(() -> {

                EntityRenderers.register(bicycle.get(), BicycleEntityRenderer::new);
            });
        });
    }
}
