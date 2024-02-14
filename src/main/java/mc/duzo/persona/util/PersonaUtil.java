package mc.duzo.persona.util;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.common.skill.Skill;
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

        if (!canUseSkill(player, persona.skills().selected())) return;

        LivingEntity target = foundTarget.get();
        Skill selected = persona.skills().selected();

        selected.run(player, persona, target);
        createSkillParticles(target);

        createCooldown(player, selected.cooldown());

        // If its dead now give them SP
        if (!target.isAlive()) {
            data.addSP((int) (target.getMaxHealth() * 0.1));
        }
    }

    public static void createCooldown(ServerPlayerEntity player, double seconds) {
        DeltaTimeManager.createDelay("skill-cooldown-" + player.getUuidAsString(), (long) (seconds * 1000L));
    }
    public static boolean onCooldown(ServerPlayerEntity player) {
        return DeltaTimeManager.isOnDelay("skill-cooldown-" + player.getUuidAsString());
    }

    public static boolean canUseSkill(ServerPlayerEntity player, Skill skill) {
        if (onCooldown(player)) return false;

        PlayerData data = ServerData.getPlayerState(player);

        if (skill.usesHealth()) return true;

        return data.hasEnoughSP(skill.cost());
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
