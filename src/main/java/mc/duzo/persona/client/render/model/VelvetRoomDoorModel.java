package mc.duzo.persona.client.render.model;

import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import mc.duzo.persona.common.entity.door.VelvetDoorVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class VelvetRoomDoorModel extends SinglePartEntityModel<VelvetDoorEntity> {
    public abstract VelvetDoorVariant getVariant(); // todo make a registry on client for enum -> model

    public abstract void render(VelvetDoorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    public Identifier getTexture() {
        return this.getVariant().texture();
    }
}
