package mc.duzo.persona.util;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;

import java.util.Optional;

public class PersonaUtil {
    public static void useSkill(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.findPersona().isEmpty()) return;

        TargetingUtil.verifyTarget(player);

        Persona persona = data.findPersona().get();
        Optional<LivingEntity> foundTarget = data.findTarget(player.getServerWorld());

        if (foundTarget.isEmpty()) return;

        persona.skills().selected().run(persona, foundTarget.get());
        createSkillParticles(foundTarget.get());
    }

    /**
     * Creates a spiral of particles around a target
     */
    public static void createSkillParticles(LivingEntity target) {
        double b = Math.PI / 8;

        if (target.getWorld().isClient()) return;

        ServerWorld world = (ServerWorld) target.getWorld();
        Vec3d source = target.getPos();

        Vec3d pos;
        double x;
        double y;
        double z;

        for(double t = 0.0D; t <= Math.PI * 2; t += Math.PI / 16) {
            for (int i = 0; i <= 1; i++) {
                x = 0.4D * (Math.PI * 2 - t) * 0.5D * Math.cos(t + b + i * Math.PI);
                y = 0.5D * t;
                z = 0.4D * (Math.PI * 2 - t) * 0.5D * Math.sin(t + b + i * Math.PI);
                pos = source.add(x, y, z);

                world.spawnParticles(ParticleTypes.ENCHANTED_HIT, pos.getX(), pos.getY(), pos.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
