package mc.duzo.persona.mixin.server;

import mc.duzo.persona.util.VelvetUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(at = @At("HEAD"), method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    public void persona$placeBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (VelvetUtil.isVelvetRoom(context.getWorld()) && !context.getPlayer().isCreative()) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
