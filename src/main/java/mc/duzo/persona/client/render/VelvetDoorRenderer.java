package mc.duzo.persona.client.render;

import mc.duzo.persona.client.render.model.P4DoorModel;
import mc.duzo.persona.client.render.model.VelvetRoomDoorModel;
import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
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
        this.model = new P4DoorModel(P4DoorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(VelvetDoorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.model.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(VelvetDoorEntity entity) {
        return entity.getVariant().texture();
    }
}
