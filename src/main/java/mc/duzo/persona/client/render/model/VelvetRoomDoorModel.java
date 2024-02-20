package mc.duzo.persona.client.render.model;

import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import mc.duzo.persona.common.entity.door.VelvetDoorVariant;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public abstract class VelvetRoomDoorModel extends SinglePartEntityModel<VelvetDoorEntity> {
    public abstract VelvetDoorVariant getVariant(); // todo make a registry on client for enum -> model
}
