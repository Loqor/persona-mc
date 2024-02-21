package mc.duzo.persona.client.render.model;

import mc.duzo.persona.common.entity.door.VelvetDoorVariant;

import java.util.HashMap;

public class VelvetDoorRegistry {
    // static registry stuff
    private static final HashMap<VelvetDoorVariant, VelvetDoorModel> models = new HashMap<>();

    public static VelvetDoorModel get(VelvetDoorVariant variant) {
        return models.get(variant);
    }

    public static VelvetDoorModel register(VelvetDoorVariant variant, VelvetDoorModel model) {
        models.put(variant, model);
        return model;
    }


    // initialising and adding our default stuff

    public VelvetDoorModel four;

    public VelvetDoorRegistry() {
        this.init();
    }

    private void init() {
        four = register(VelvetDoorVariant.FOUR, new P4DoorModel(P4DoorModel.getTexturedModelData().createModel()));
    }

    // for obtaining our models statically
    // idk why i did it this way, i might change it later so this entire class is static.

    private static VelvetDoorRegistry INSTANCE;

    public static VelvetDoorRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new VelvetDoorRegistry();

        return INSTANCE;
    }
}
