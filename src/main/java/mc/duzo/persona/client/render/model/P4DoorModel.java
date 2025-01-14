package mc.duzo.persona.client.render.model;

import mc.duzo.persona.common.entity.door.VelvetDoorEntity;
import mc.duzo.persona.common.entity.door.VelvetDoorVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class P4DoorModel extends VelvetDoorModel {
	private final ModelPart door;
	public P4DoorModel(ModelPart root) {
		this.door = root.getChild("door");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData door = modelPartData.addChild("door", ModelPartBuilder.create().uv(41, 7).cuboid(-17.0F, -56.0F, -1.0F, 34.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(46, 55).cuboid(-15.0F, -52.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(18, 52).cuboid(11.0F, -52.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(5, 65).cuboid(11.0F, -48.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
				.uv(49, 62).cuboid(-13.0F, -48.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
				.uv(46, 26).cuboid(-8.0F, -59.0F, -1.0F, 16.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(61, 32).cuboid(-13.0F, -16.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
				.uv(16, 59).cuboid(11.0F, -16.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
				.uv(5, 52).cuboid(11.0F, -11.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(46, 48).cuboid(-15.0F, -11.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(41, 0).cuboid(-17.0F, -7.0F, -1.0F, 34.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(46, 20).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(46, 17).cuboid(-11.0F, -52.0F, -0.5F, 22.0F, 1.0F, 1.0F, new Dilation(0.001F))
				.uv(46, 14).cuboid(-11.0F, -8.0F, -0.5F, 22.0F, 1.0F, 1.0F, new Dilation(0.001F))
				.uv(0, 44).cuboid(-11.0F, -51.0F, -0.5F, 1.0F, 43.0F, 1.0F, new Dilation(0.001F))
				.uv(41, 14).cuboid(10.0F, -51.0F, -0.5F, 1.0F, 43.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = door.addChild("cube_r1", ModelPartBuilder.create().uv(5, 44).cuboid(14.25F, -11.5F, -1.05F, 5.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(4.3895F, 11.6244F, 0.05F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r2 = door.addChild("cube_r2", ModelPartBuilder.create().uv(46, 32).cuboid(-19.25F, -11.5F, -1.05F, 5.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(-4.3895F, 11.6244F, 0.05F, 0.0F, 0.0F, 0.4363F));

		ModelPartData cube_r3 = door.addChild("cube_r3", ModelPartBuilder.create().uv(29, 57).cuboid(-7.75F, -20.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(6.3637F, 11.6834F, 0.0F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r4 = door.addChild("cube_r4", ModelPartBuilder.create().uv(5, 59).cuboid(4.75F, -20.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(-6.3637F, 11.6834F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData cube_r5 = door.addChild("cube_r5", ModelPartBuilder.create().uv(59, 46).cuboid(13.0F, -12.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(4.3637F, 1.6834F, 0.0F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r6 = door.addChild("cube_r6", ModelPartBuilder.create().uv(59, 54).cuboid(-15.0F, -12.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.005F)), ModelTransform.of(-4.3637F, 1.6834F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData cube_r7 = door.addChild("cube_r7", ModelPartBuilder.create().uv(20, 44).cuboid(-19.25F, 6.5F, -1.0F, 5.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(-4.4137F, -70.5579F, 0.0F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r8 = door.addChild("cube_r8", ModelPartBuilder.create().uv(61, 40).cuboid(4.75F, 17.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(-6.3637F, -70.6834F, 0.0F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r9 = door.addChild("cube_r9", ModelPartBuilder.create().uv(38, 62).cuboid(-7.75F, 17.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(6.3637F, -70.6834F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData cube_r10 = door.addChild("cube_r10", ModelPartBuilder.create().uv(46, 40).cuboid(14.25F, 6.5F, -1.0F, 5.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(4.4137F, -70.5579F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData cube_r11 = door.addChild("cube_r11", ModelPartBuilder.create().uv(58, 62).cuboid(-15.0F, 7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.005F)), ModelTransform.of(-4.3637F, -60.6834F, 0.0F, 0.0F, 0.0F, -0.4363F));

		ModelPartData cube_r12 = door.addChild("cube_r12", ModelPartBuilder.create().uv(25, 63).cuboid(13.0F, 7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.05F)), ModelTransform.of(4.3637F, -60.6834F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData end_portal = door.addChild("end_portal", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, -47.0F, 0.0F, 20.0F, 43.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

		ModelPartData bone2 = door.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(5.3895F, 16.6244F, 0.05F));

		ModelPartData bone5 = bone2.addChild("bone5", ModelPartBuilder.create(), ModelTransform.pivot(-9.779F, 0.0F, 0.0F));

		ModelPartData bone6 = bone2.addChild("bone6", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 0.0F, 0.0F));

		ModelPartData bone4 = door.addChild("bone4", ModelPartBuilder.create(), ModelTransform.pivot(-5.4137F, -65.5579F, 0.0F));

		ModelPartData bone = bone4.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(9.8274F, 0.0F, 0.0F));

		ModelPartData bone3 = bone4.addChild("bone3", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		door.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void render(VelvetDoorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180f));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getBodyYaw()));

		this.getChild("end_portal").ifPresent(cube -> {
			if(MinecraftClient.getInstance().player.distanceTo(entity) < 2f) {
				matrices.push();
				matrices.scale(1f, 1f, 2f);
				cube.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEndGateway()), 0xFF00F0, OverlayTexture.DEFAULT_UV, 0f, 0.5f, 1f, 1f);
				matrices.pop();
			}
		});

		matrices.translate(0f, -1.5f, 0f);
		this.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySmoothCutout(this.getTexture())), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return door;
	}

	@Override
	public void setAngles(VelvetDoorEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public VelvetDoorVariant getVariant() {
		return VelvetDoorVariant.FOUR;
	}
}