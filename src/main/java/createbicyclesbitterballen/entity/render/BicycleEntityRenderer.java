package createbicyclesbitterballen.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import createbicyclesbitterballen.entity.BicycleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class BicycleEntityRenderer extends EntityRenderer<BicycleEntity> {
    public BicycleEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(BicycleEntity entity) {
        return null;
    }

    @Override
    public void render(BicycleEntity entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.popPose();
    }
}
