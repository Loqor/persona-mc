package mc.duzo.persona.common.entity.door;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.Register;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import mc.duzo.persona.util.AbsoluteBlockPos;
import mc.duzo.persona.util.VelvetUtil;
import mc.duzo.persona.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelvetDoorEntity extends Entity implements VariantHolder<VelvetDoorVariant> {
    private static final double REQUIRED_DISTANCE = 0.25f;

    protected static final TrackedData<Integer> DATA_VARIANT_ID = DataTracker.registerData(VelvetDoorEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public VelvetDoorEntity(EntityType<?> type, World world) {
        super(type, world);
    }
    protected VelvetDoorEntity(World world) {
        this(Register.VELVET_DOOR_ENTITY, world);
    }
    public VelvetDoorEntity(World world, BlockPos pos) {
        this(world);

        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);

        if (player instanceof ServerPlayerEntity sPlayer) {
            if (distanceFromDoor(player) > REQUIRED_DISTANCE) return;

            performTeleport(sPlayer);
        }
    }


    private static void performTeleport(ServerPlayerEntity player) {
        if (!VelvetUtil.isInVelvetRoom(player)) {
            sendToRoom(player);
            return;
        }

        returnFromRoom(player);
    }

    private static void returnFromRoom(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.getVelvetDoorPos() == null) {
            PersonaMod.LOGGER.error("Velvet door position not set for " + player.getName());
            return;
        }

        AbsoluteBlockPos.Directed pos = data.getVelvetDoorPos();
        Direction direction = pos.getDirection();

        WorldUtil.teleport(
                player,
                (ServerWorld) pos.getWorld(),
                offsetPosition(pos).toCenterPos(),
                direction.getHorizontal(),
                player.getPitch()
        );
    }

    private static BlockPos offsetPosition(AbsoluteBlockPos.Directed pos) {
        return switch (pos.getDirection()) {
            case DOWN, UP ->
                    throw new IllegalArgumentException("Cannot adjust position with direction: " + pos.getDirection());
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 1);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 1);
            case EAST -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 0.5);
        };
    }

    private static void sendToRoom(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        AbsoluteBlockPos.Directed pos = fromEntity(player);
        data.setVelvetDoorPos(new AbsoluteBlockPos.Directed(pos, pos.getDirection().getOpposite()));

        ServerData.getServerState(player.getServer()).markDirty();
        PersonaMessages.syncData(player, player);

        VelvetUtil.sendToRoom(player);
    }

    private double distanceFromDoor(LivingEntity entity) {
        BlockPos them = entity.getBlockPos();
        BlockPos me = this.getBlockPos();

        return Math.sqrt(me.getSquaredDistance(them));
    }

    public static AbsoluteBlockPos.Directed fromEntity(LivingEntity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();
        Direction dir = entity.getHorizontalFacing();

        return new AbsoluteBlockPos.Directed(new AbsoluteBlockPos(pos, world), dir);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(DATA_VARIANT_ID, VelvetDoorVariant.values()[0].ordinal());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Variant")) {
            this.setVariant(VelvetDoorVariant.values()[nbt.getInt("Variant")]);
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Variant", this.getVariant().ordinal());
    }

    @Override
    public void setVariant(VelvetDoorVariant variant) {
        this.dataTracker.set(DATA_VARIANT_ID, variant.ordinal());
    }

    @Override
    public @NotNull VelvetDoorVariant getVariant() {
        return VelvetDoorVariant.values()[this.dataTracker.get(DATA_VARIANT_ID)];
    }

    @Override
    public void tick() {
        super.tick();

        if (random.nextInt(128) == 0) {
            BlockPos pos = this.getBlockPos();

            this.getWorld().playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5f, random.nextFloat() * 0.4f + 0.8f, false);
        }
    }
}
