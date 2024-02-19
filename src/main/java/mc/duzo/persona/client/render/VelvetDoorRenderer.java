package mc.duzo.persona.client.render;

import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class VelvetDoorRenderer extends EntityRenderer<VelvetDoorEntity> {
    public VelvetDoorRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(VelvetDoorEntity entity) {
        return entity.getVariant().texture();
    }
}
