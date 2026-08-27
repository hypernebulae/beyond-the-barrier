package hypernebulae.btb.mixin;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Mixin(DebugEntryPosition.class)
public abstract class DebugEntryPositionMixin {
    private char getColorPrecision(double precision) {
        if (precision <= 0.03125) {
            return 'a';
        }
        return precision > 0.25 ? 'c' : 'e';
    }

    private char getXZColor(double coord) {
        double absolute = Math.abs(coord);
        if (absolute < 1E6) {
            return 'b';
        } else if (absolute < 3E7) {
            return 'a';
        } else if (absolute < 3.2E7) {
            return 'e';
        } else if (absolute < 33554432) {
            return '6';
        } else {
            return 'c';
        }
    }

    private char getYColor(double coord) {
        if (coord > -64 && coord < 320) {
            return 'b';
        } else {
            double absolute = Math.abs(coord);
            if (absolute < 2E7) {
                return 'a';
            } else if (absolute < 3.2E7) {
                return 'e';
            } else if (absolute < 2147483647) {
                return '6';
            } else {
                return 'c';
            }
        }
    }


    private String format(double value, String format) {
        if (Math.abs(value) >= 1E10 || Double.isNaN(value)) {
            return String.valueOf(value);
        }
        return new DecimalFormat(format).format(value);
    }

    @Overwrite
    public void display(
            DebugScreenDisplayer displayer,
            @Nullable Level level,
            @Nullable LevelChunk chunk1,
            @Nullable LevelChunk chunk2
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.getCameraEntity();

        if (entity == null) {
            return;
        }

        BlockPos blockPos = entity.blockPosition();
        ChunkPos chunkPos = new ChunkPos(
                SectionPos.blockToSectionCoord(blockPos.getX()),
                SectionPos.blockToSectionCoord(blockPos.getZ())
        );
        Direction direction = entity.getDirection();


        String directionText = switch (direction) {
            case NORTH -> "Towards negative Z";
            case SOUTH -> "Towards positive Z";
            case WEST -> "Towards negative X";
            case EAST -> "Towards positive X";
            default -> "Invalid";
        };

        long maxPosition = (long) Math.max(Math.abs(entity.getX()), Math.max(Math.abs(entity.getY()), Math.abs(entity.getZ())));
        int maxBit = 64 - Long.numberOfLeadingZeros(maxPosition);
        double doublePrecision = Math.pow(2.0D, maxBit - 53);
        double floatPrecision = Math.pow(2.0D, maxBit - 24);

        LongSet forcedChunks = level instanceof ServerLevel serverLevel
                ? serverLevel.getForceLoadedChunks()
                : LongSets.EMPTY_SET;

        displayer.addToGroup(
                DebugEntryPosition.GROUP,
                List.of(
                        "X: §" + getXZColor(entity.getX()) + format(entity.getX(), "0.################"),
                        "Y: §" + getYColor(entity.getY()) + format(entity.getY(), "0.################"),
                        "Z: §" + getXZColor(entity.getZ()) + format(entity.getZ(), "0.################"),

                        "Current precision: §"
                                + getColorPrecision(doublePrecision)
                                + doublePrecision
                                + "§r (float: §"
                                + getColorPrecision(floatPrecision)
                                + floatPrecision
                                + "§r)",
                        "",

                        String.format(
                                Locale.ROOT,
                                "Block: %d %d %d",
                                blockPos.getX(),
                                blockPos.getY(),
                                blockPos.getZ()
                        ),

                        String.format(
                                Locale.ROOT,
                                "Chunk: %d %d %d [%d %d in r.%d.%d.mca]",
                                chunkPos.x(),
                                SectionPos.blockToSectionCoord(blockPos.getY()),
                                chunkPos.z(),
                                chunkPos.getRegionLocalX(),
                                chunkPos.getRegionLocalZ(),
                                chunkPos.getRegionX(),
                                chunkPos.getRegionZ()
                        ),

                        String.format(
                                Locale.ROOT,
                                "Facing: %s (%s) (%.1f / %.1f)",
                                direction,
                                directionText,
                                Mth.wrapDegrees(entity.getYRot()),
                                Mth.wrapDegrees(entity.getXRot())
                        ),

                        minecraft.level.dimension().identifier() + " FC: " + forcedChunks.size()
                )
        );
    }
}