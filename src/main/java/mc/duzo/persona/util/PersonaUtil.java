package mc.duzo.persona.util;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.common.skill.Skill;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;

import java.util.Optional;

public class PersonaUtil {
    public static void useSkill(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.findPersona().isEmpty()) return;
        if (!data.isPersonaRevealed()) return;

        TargetingUtil.verifyTarget(player);

        Persona persona = data.findPersona().get();
        Optional<LivingEntity> foundTarget = data.findTarget(player.getServerWorld());

        if (foundTarget.isEmpty()) return;

        if (!canUseSkill(player, persona.getSkillSet().getSelected())) return;

        LivingEntity target = foundTarget.get();
        Skill selected = persona.getSkillSet().getSelected();

        selected.run(player, persona, target);

        createSkillParticles(target, ParticleTypes.ENCHANTED_HIT);
        createSkillParticles(player, ParticleTypes.FIREWORK);

        player.getServerWorld().playSound(null, player.getBlockPos(), selected.getUseSound(), SoundCategory.PLAYERS, 1.0f, 1.0f);

        createCooldown(player, selected.getCooldown());

        // If its dead now give them SP
        if (!target.isAlive()) {
            data.addSP((int) (target.getMaxHealth() * 0.1));

            ServerData.getServerState(player.getServer()).markDirty();
            PersonaMessages.syncData(player, player);
        }
    }

    public static void revealPersona(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        data.revealPersona();

        if (data.findPersona().isEmpty()) return;

        player.getServerWorld().playSound(null, player.getBlockPos(), data.findPersona().get().getSummonSound(), SoundCategory.PLAYERS, 1.0f, 1.0f);

        ServerData.getServerState(player.getServer()).markDirty();
        PersonaMessages.syncData(player, player);
    }
    public static void hidePersona(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        data.hidePersona();

        ServerData.getServerState(player.getServer()).markDirty();
        PersonaMessages.syncData(player, player);
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

        return data.hasEnoughSP(skill.getCost());
    }

    /**
     * Creates a spiral of particles around a target
     */
    public static <T extends ParticleEffect> void createSkillParticles(LivingEntity target, T particle) {
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

                world.spawnParticles(particle, pos.getX(), pos.getY(), pos.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
