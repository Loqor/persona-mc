package mc.duzo.persona.util;

import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class TargetingUtil {
    private static final double MAX_DISTANCE_FROM_TARGET = 8;

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

    public static double distanceFromTarget(LivingEntity source, LivingEntity target) {
        return source.getPos().distanceTo(target.getPos());
    }

    /**
     * Verifies whether the player has a valid target
     * If not, sets the target to null
     * @return true if the player has a valid target
     */
    public static boolean verifyTarget(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.findTarget(player.getServerWorld()).isEmpty()) return true;

        LivingEntity target = data.findTarget(player.getServerWorld()).get();

        boolean invalid = distanceFromTarget(player, target) > MAX_DISTANCE_FROM_TARGET;

        if (!invalid) return true;

        data.setTarget(null);
        return false;
    }
}
