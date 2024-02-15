package mc.duzo.persona.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.data.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.awt.*;

public class SPHudOverlay implements HudRenderCallback {

    public static final Identifier SP_HUD = new Identifier(PersonaMod.MOD_ID, "textures/gui/sp_hud.png");
    public static final Identifier CHARACTER_ELEMENT = new Identifier(PersonaMod.MOD_ID, "textures/gui/character_element.png");

    public static final Identifier TARGET = new Identifier(PersonaMod.MOD_ID, "textures/gui/target_acquired.png");
    @Override
    public void onHudRender(DrawContext draw, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc == null) return;

        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        x = width / 2;
        y = height;

        PlayerData data = ClientData.getPlayerState(mc.player);

        if (!data.isPersonaRevealed()) return;
        if (data.findPersona().isEmpty()) return;

        String spText =  data.spiritPoints() < 100 ? data.spiritPoints() < 10 ? "00" + data.spiritPoints() : "0" + data.spiritPoints() : "" + data.spiritPoints();

        draw.drawTexture(SP_HUD, width - 362, 0,0,0,(int) (227 / 1.25f),(int) (101 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
        draw.drawTexture(SP_HUD, width - 181, 19,0,130,(int) (227 / 1.25f),(int) (101 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
        if(data.spiritPoints() > 100) {
            // @TODO this is temporary and it sucks so
            draw.drawTexture(SP_HUD, (width - 337) + 22, (54) - 18, 25, 95, (int) (195 / 1.25f), (int) (43 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
            draw.drawTexture(SP_HUD, (width - 181) + 22, (72) - 18, 0, 224, (int) (195 / 1.25f), (int) (43 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
        }

        MatrixStack stack = draw.getMatrices();

        if(data.hasTarget()) {
            if (mc.world.getEntityById(data.getTargetId()) != null) {
                String name = mc.world.getEntityById(data.getTargetId()).getDisplayName().getString().substring(1);
                String randomcapital = mc.world.getEntityById(data.getTargetId()).getDisplayName().getString().substring(0,1).toUpperCase();
                Text level = Text.literal(String.valueOf(mc.world.getEntityById(data.getTargetId()) instanceof PlayerEntity ? ((PlayerEntity) mc.world.getEntityById(data.getTargetId())).getNextLevelExperience() : 2)).formatted(Formatting.ITALIC);
                stack.push();
                int smt = mc.textRenderer.getWidth(name) / 2;
                int sizeofnumber = mc.textRenderer.getWidth(level) /2;
                int smt2 = mc.textRenderer.getWidth(randomcapital) / 2;
                stack.translate(240, 47, 0);
                stack.scale(2.7f, 2.7f, 1);
                stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(4.5f));
                draw.drawText(mc.textRenderer, name, -smt, 0, 0xFFFFFF, true);
                stack.push();
                stack.translate(-68, 1, 0);
                draw.drawText(mc.textRenderer, level, -sizeofnumber, 0, 0xFFFFFF, true);
                stack.pop();
                stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(3f));
                mc.textRenderer.draw(randomcapital, -smt -smt2-5, -1, 0, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, Colors.WHITE, 0xF000F0, false);
                stack.translate(0, 0, 20);
                mc.textRenderer.draw(randomcapital, -smt -smt2-4, -1, Colors.BLACK, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, false);
                stack.pop();
            }
            draw.drawTexture(TARGET, 0, 0, 0, 0, 436, 99, 436, 99);
        }


        stack.push();
        stack.translate(0, 0, -300);
        draw.drawTexture(CHARACTER_ELEMENT, width - 250, height -180,0,0,(int) (130 * 1.5),(int) (92 * 1.5), (int) (130 * 1.5),(int) (148 * 1.5));
        stack.pop();

        this.drawEntity(draw, width - 200, height + 70, 110, 20, 0f, mc.player, tickDelta);

        draw.drawTexture(CHARACTER_ELEMENT, width - 202, height - 89,0,(int) (92 * 1.5),(int) (130 * 1.5),(int) (56 * 1.5), (int) (130 * 1.5),(int) (148 * 1.5));

        stack.push();
        int scaledTextWidth = mc.textRenderer.getWidth(spText) / 2;
        stack.translate(width - 270, 15, 0);
        stack.scale(3.5f, 3.5f, 1);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(6.5f));
        draw.drawText(mc.textRenderer, spText, -scaledTextWidth, 0, 0xFFFFFF, false);
        stack.pop();

        String skillText = data.findPersona().get().skills().selected().name().getString().substring(1);
        String skillHighlight = data.findPersona().get().skills().selected().name().getString().substring(0, 1).toUpperCase();
        stack.push();
        int stw = (mc.textRenderer.getWidth(skillText) / 2);
        int stw2 = (mc.textRenderer.getWidth(skillHighlight) / 2);
        stack.translate(width - 115, height - 36, 0);
        stack.scale(3.5f, 3.5f, 1);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(12.5f));
        draw.drawText(mc.textRenderer, skillText, -stw, -8, 0xFFFFFF, false);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(5f));
        mc.textRenderer.draw(skillHighlight, -stw -stw2-5, -9, 0, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.POLYGON_OFFSET, Colors.WHITE, 0xF000F0, false);
        mc.textRenderer.draw(skillHighlight, -stw -stw2-4, -9, Colors.BLACK, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, false);
        stack.pop();

        //draw.drawText(mc.textRenderer, skillText , (x - (skillWidth)) - 94, y - 20, 0xFFFFFF, true);
    }

    public void drawEntity(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, float delta) {
        float f = (float) Math.atan(mouseX / 40.0f);
        float g = (float) Math.atan(mouseY / 40.0f);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0f * ((float) Math.PI / 180));
        quaternionf.mul(quaternionf2);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        this.drawEntity(context, x, y, size, quaternionf, quaternionf2, entity, delta);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    public void drawEntity(DrawContext context, int x, int y, int size, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, -100);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, -size));
        context.getMatrices().multiply(quaternionf);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.setRotation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() ->
                entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, context.getMatrices(), context.getVertexConsumers(), 0xF000F0));
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        //this.buffer.beginWrite(false);
        //this.buffer.draw(this.buffer.textureWidth, this.buffer.textureHeight, true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
