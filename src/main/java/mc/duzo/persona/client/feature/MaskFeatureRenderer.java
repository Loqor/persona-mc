package mc.duzo.persona.client.feature;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.Register;
import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.data.PlayerData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

/**
 * A slightly transparent mask on the players face which shows if they have a persona and its hidden
 * Might remove - not too sure if I like it.
 *
 * @author duzo
 */
@Environment(value= EnvType.CLIENT)
public class MaskFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier MASK_TEXTURE = new Identifier(PersonaMod.MOD_ID, "textures/skins/mask.png"); // https://www.planetminecraft.com/skin/joker-persona-5-5675860/
    private final PlayerEntityModel<T> model;

    public MaskFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.model = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() != Register.JOKER_MASK) return;

        PlayerData data = ClientData.getPlayerState(livingEntity);

        if (data.isPersonaRevealed() || data.findPersona().isEmpty()) return;

        if (!(livingEntity instanceof AbstractClientPlayerEntity player)) return;

        matrixStack.push();

        this.getContextModel().copyStateTo(this.model);
        this.model.setAngles(livingEntity, f, g, j, k, l);
        this.model.sneaking = player.isSneaking();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(MASK_TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1f);

        matrixStack.pop();
    }
}

