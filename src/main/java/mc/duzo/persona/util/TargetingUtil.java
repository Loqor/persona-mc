package mc.duzo.persona.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class TargetingUtil {

    /**
     * Finds the entity that another entity is looking at
     * @param player the source
     * @return an empty optional if there is no entity or the entity that is being looked at
     */
    public static Optional<LivingEntity> findEntityBeingLookedAt(LivingEntity player) {
        double d = 10;
        double e = d * d;
        Vec3d vec3d = player.getCameraPosVec(1);
        Vec3d vec3d2 = player.getRotationVec(1.0f);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        float f = 1.0f;
        Box box = player.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(player, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);

        if (entityHitResult == null || entityHitResult.getType() != HitResult.Type.ENTITY) return Optional.empty();

        return Optional.ofNullable((LivingEntity) entityHitResult.getEntity());
    }
}
