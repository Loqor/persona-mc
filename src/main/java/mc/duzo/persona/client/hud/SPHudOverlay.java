package mc.duzo.persona.client.hud;

import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.data.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class SPHudOverlay implements HudRenderCallback {
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
        if (data.findPersona().isEmpty()) return;;

        // String spText = "SP: " + Math.round((((double) data.spiritPoints() / PlayerData.MAX_SP) * 100d)) + "%";

        String spText = "SP: " + data.spiritPoints();
        int spWidth = mc.textRenderer.getWidth(spText);

        draw.drawText(mc.textRenderer, spText, (x - (spWidth)) - 94, y - 10, 0xFFFFFF, true);

        String skillText = data.findPersona().get().skills().selected().name().getString();
        int skillWidth = mc.textRenderer.getWidth(skillText);

        draw.drawText(mc.textRenderer, skillText , (x - (skillWidth)) - 94, y - 20, 0xFFFFFF, true);
    }
}
