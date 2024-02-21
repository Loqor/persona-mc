package mc.duzo.persona.client.render;

import mc.duzo.persona.client.render.model.VelvetDoorModel;
import mc.duzo.persona.client.render.model.VelvetDoorRegistry;
import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import mc.duzo.persona.common.entity.door.VelvetDoorVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class VelvetDoorRenderer extends EntityRenderer<VelvetDoorEntity> {

    VelvetDoorModel model;

    public VelvetDoorRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(VelvetDoorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (this.model == null) {
            Optional<VelvetDoorModel> found = findModel(entity);

            if (found.isEmpty()) return; // Add a warning message?

            this.model = found.get();
        }

        this.model.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(VelvetDoorEntity entity) {
        return entity.getVariant().texture();
    }

    private static Optional<VelvetDoorModel> findModel(VelvetDoorEntity entity) {
        return findModel(entity.getVariant());
    }

    private static Optional<VelvetDoorModel> findModel(VelvetDoorVariant variant) {
        return Optional.ofNullable(VelvetDoorRegistry.getInstance().get(variant));
    }
}
