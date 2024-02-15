package mc.duzo.persona.common.skill;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.util.VelvetUtil;
import mc.duzo.persona.util.WorldUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class SkillRegistry {
    public static final SimpleRegistry<Skill> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Skill>ofRegistry(new Identifier(PersonaMod.MOD_ID, "skill"))).buildAndRegister();

    public static Skill register(Skill skill) {
        return Registry.register(REGISTRY, skill.id(), skill);
    }

    public static Skill get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static Skill DIA = register(Skill.create(
            new Identifier(PersonaMod.MOD_ID, "dia"),
            (source, persona, target) -> target.setHealth(target.getHealth() + 4),
            false,
            3,
            1,
            PersonaSounds.DIA
    ));
    public static Skill CLEAVE = register(Skill.create(
            new Identifier(PersonaMod.MOD_ID, "cleave"),
            (source, persona, target) -> target.damage(target.getDamageSources().generic(), 4),
            true,
            5,
            1
    ));
    public static Skill ZIO = register(Skill.create(
            new Identifier(PersonaMod.MOD_ID, "zio"),
            (source, persona, target) -> {
                EntityType.LIGHTNING_BOLT.spawn((ServerWorld) target.getWorld(), target.getBlockPos(), SpawnReason.TRIGGERED);
            },
            false,
            4,
            2
    ));

    public static Skill TRAFURI = register(Skill.create(
            new Identifier(PersonaMod.MOD_ID, "trafuri"),
            (source, persona, target) -> {
                if (!PersonaMod.hasServer()) return;

                ServerWorld world = WorldUtil.findWorld(source.getSpawnPointDimension());
                BlockPos pos = source.getSpawnPointPosition();

                if (world == null || pos == null) return;

                Optional<Vec3d> respawnPos = PlayerEntity.findRespawnPosition(world, pos, source.getSpawnAngle(), true, true);

                if (respawnPos.isEmpty()) return;

                WorldUtil.teleport(source, world, respawnPos.get(), source.getSpawnAngle(), 0);
            },
            false,
            25,
            4
    ));

    public static Skill VELVET = register(Skill.create( // Temporary skill
            new Identifier(PersonaMod.MOD_ID, "velvet"),
            (source, persona, target) -> VelvetUtil.sendToRoom(source),
            false,
            0,
            1
    ));

    public static void init() {

    }
}
