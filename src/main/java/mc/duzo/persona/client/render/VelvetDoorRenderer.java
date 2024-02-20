package mc.duzo.persona.client.render;

import mc.duzo.persona.client.models.VelvetRoomDoorModel;
import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class VelvetDoorRenderer extends EntityRenderer<VelvetDoorEntity> {

    VelvetRoomDoorModel model;

    public VelvetDoorRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new VelvetRoomDoorModel(VelvetRoomDoorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(VelvetDoorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getBodyYaw()));
        this.model.getChild("end_portal").ifPresent(cube -> {
            if(MinecraftClient.getInstance().player.distanceTo(entity) < 5f) {
                matrices.push();
                matrices.scale(1f, 1f, 2f);
                cube.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEndGateway()), 0xFF00F0, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
                matrices.pop();
            }
        });
        matrices.translate(0f, -1.5f, 0f);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySmoothCutout(this.getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(VelvetDoorEntity entity) {
        return entity.getVariant().texture();
    }
}
