package mc.duzo.persona.mixin.server;

import mc.duzo.persona.util.VelvetUtil;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    protected ServerWorld world;

    @Shadow public abstract boolean isCreative();

    @Inject(method = "tryBreakBlock", at = @At(value = "HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!isCreative() && VelvetUtil.isVelvetRoom(this.world)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
