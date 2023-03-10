package me.quesia.peepopractice.core.category.properties;

import me.quesia.peepopractice.core.exception.NotInitializedException;
import me.quesia.peepopractice.core.category.PracticeCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

import java.util.Random;

@SuppressWarnings("UnusedDeclaration")
public class PlayerProperties extends BaseProperties {
    private BlockPos spawnPos;
    private PracticeCategory.ExecuteReturnTask<BlockPos> spawnPosTask;
    private Vec2f spawnAngle;
    private PracticeCategory.ExecuteReturnTask<Vec2f> spawnAngleTask;
    private EntityType<? extends Entity> vehicle;

    public BlockPos getSpawnPos() {
        return this.spawnPos != null ? this.spawnPos : new BlockPos(0, 62, 0);
    }

    public boolean hasSpawnPos() {
        return this.spawnPos != null;
    }

    public PlayerProperties setSpawnPos(BlockPos spawnPos) {
        this.spawnPos = spawnPos;
        return this;
    }

    public PlayerProperties setSpawnPos(PracticeCategory.ExecuteReturnTask<BlockPos> spawnPosTask) {
        this.spawnPosTask = spawnPosTask;
        return this;
    }

    public Vec2f getSpawnAngle() {
        return this.spawnAngle != null ? this.spawnAngle : new Vec2f(0.0F, 0.0F);
    }

    public boolean hasSpawnAngle() {
        return this.spawnAngle != null;
    }

    public PlayerProperties setSpawnAngle(float yaw, float pitch) {
        this.spawnAngle = new Vec2f(yaw, pitch);
        return this;
    }

    public PlayerProperties setSpawnAngle(PracticeCategory.ExecuteReturnTask<Vec2f> spawnAngleTask) {
        this.spawnAngleTask = spawnAngleTask;
        return this;
    }

    public EntityType<? extends Entity> getVehicle() {
        return this.vehicle;
    }

    public boolean hasVehicle() {
        return this.vehicle != null;
    }

    public PlayerProperties setVehicle(EntityType<? extends Entity> vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public void reset(Random random, ServerWorld world) throws NotInitializedException {
        if (this.spawnPosTask != null) {
            this.setSpawnPos(this.spawnPosTask.execute(this.getCategory(), random, world));
        }
        if (this.spawnAngleTask != null) {
            Vec2f vec = this.spawnAngleTask.execute(this.getCategory(), random, world);
            if (vec != null) {
                this.setSpawnAngle(vec.x, vec.y);
            }
        }
    }
}
