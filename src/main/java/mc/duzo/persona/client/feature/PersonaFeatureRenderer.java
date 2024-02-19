package mc.duzo.persona.client.feature;

import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.data.PlayerData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(value= EnvType.CLIENT)
public class PersonaFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {
    private final PlayerEntityModel<T> model;

    public PersonaFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.model = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        PlayerData data = ClientData.getPlayerState(livingEntity);

        if (!data.isPersonaRevealed() || data.findPersona().isEmpty()) return;

        if (!(livingEntity instanceof AbstractClientPlayerEntity player)) return;

        Identifier identifier = data.findPersona().get().texture();
        matrixStack.push();
        matrixStack.translate(0.25f, -0.5f, 0.5f);

        matrixStack.translate(0f, livingEntity.getWorld().random.nextFloat() * 0.02, 0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().getTickDelta() % 180));

        this.getContextModel().copyStateTo(this.model);

        this.model.setAngles(livingEntity, f, g, j, k, l);

        this.model.leftLeg.pitch = 0f;
        this.model.rightLeg.pitch = 0f;

        this.model.leftArm.pitch = 0f;
        this.model.rightArm.pitch = 0f;

        this.model.leftPants.pitch = 0f;
        this.model.rightPants.pitch = 0f;

        this.model.leftSleeve.pitch = 0f;
        this.model.rightSleeve.pitch = 0f;


        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(identifier, true));
        this.model.render(matrixStack, vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV, 1, 1, 1, livingEntity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);
        matrixStack.pop();
    }
}

