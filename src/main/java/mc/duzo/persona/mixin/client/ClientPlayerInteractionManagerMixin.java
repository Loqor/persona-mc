package mc.duzo.persona.mixin.client;

import mc.duzo.persona.util.VelvetUtil;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "HEAD"), cancellable = true)
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ClientWorld world = this.client.world;
        ClientPlayerEntity player = this.client.player;
        if (world == null || player == null) return;


        if (!player.isCreative() && VelvetUtil.isVelvetRoom(world)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
