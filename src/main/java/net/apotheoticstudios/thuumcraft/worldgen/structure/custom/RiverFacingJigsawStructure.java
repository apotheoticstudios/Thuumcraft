package net.apotheoticstudios.thuumcraft.worldgen.structure.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.apotheoticstudios.thuumcraft.worldgen.structure.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class RiverFacingJigsawStructure extends Structure {
    private static final Direction[] HORIZONTAL_DIRECTIONS = {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };

    public static final Codec<RiverFacingJigsawStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 7).fieldOf("size").forGetter(structure -> structure.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter(structure -> structure.useExpansionHack),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
                    horizontalDirectionCodec().optionalFieldOf("front_direction", Direction.SOUTH).forGetter(structure -> structure.frontDirection),
                    Codec.intRange(4, 64).optionalFieldOf("river_search_radius", 32).forGetter(structure -> structure.riverSearchRadius)
            ).apply(instance, RiverFacingJigsawStructure::new)
    ), RiverFacingJigsawStructure::verifyRange).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Direction frontDirection;
    private final int riverSearchRadius;

    public RiverFacingJigsawStructure(StructureSettings settings,
                                      Holder<StructureTemplatePool> startPool,
                                      Optional<ResourceLocation> startJigsawName,
                                      int maxDepth,
                                      HeightProvider startHeight,
                                      boolean useExpansionHack,
                                      Optional<Heightmap.Types> projectStartToHeightmap,
                                      int maxDistanceFromCenter,
                                      Direction frontDirection,
                                      int riverSearchRadius) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.frontDirection = frontDirection;
        this.riverSearchRadius = riverSearchRadius;
    }

    private static Codec<Direction> horizontalDirectionCodec() {
        return ExtraCodecs.validate(Direction.CODEC, direction -> direction.getAxis().isHorizontal()
                ? com.mojang.serialization.DataResult.success(direction)
                : com.mojang.serialization.DataResult.error(() -> "Direction must be horizontal"));
    }

    private static com.mojang.serialization.DataResult<RiverFacingJigsawStructure> verifyRange(RiverFacingJigsawStructure structure) {
        return structure.maxDistanceFromCenter > 128
                ? com.mojang.serialization.DataResult.error(() -> "Structure size must not exceed 128")
                : com.mojang.serialization.DataResult.success(structure);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int y = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());
        BlockPos searchCenter = new BlockPos(chunkPos.getMiddleBlockX(), y, chunkPos.getMiddleBlockZ());
        Optional<Direction> riverDirection = this.findRiverDirection(context, searchCenter);

        if (riverDirection.isEmpty()) {
            return JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, startPos,
                    this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
        }

        Rotation rotation = this.rotationFor(riverDirection.get());
        GenerationContext rotatedContext = new GenerationContext(context.registryAccess(), context.chunkGenerator(),
                context.biomeSource(), context.randomState(), context.structureTemplateManager(),
                new ForcedFirstRotationRandom(context.random(), rotation), context.seed(), context.chunkPos(),
                context.heightAccessor(), context.validBiome());

        return JigsawPlacement.addPieces(rotatedContext, this.startPool, this.startJigsawName, this.maxDepth, startPos,
                this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    private Optional<Direction> findRiverDirection(GenerationContext context, BlockPos center) {
        Direction bestDirection = null;
        int bestScore = 0;

        for (Direction direction : HORIZONTAL_DIRECTIONS) {
            int score = this.scoreRiverBiomes(context, center, direction);
            if (score > bestScore) {
                bestScore = score;
                bestDirection = direction;
            }
        }

        return bestDirection == null ? Optional.empty() : Optional.of(bestDirection);
    }

    private int scoreRiverBiomes(GenerationContext context, BlockPos center, Direction direction) {
        int score = 0;
        Direction side = direction.getClockWise();

        for (int distance = 8; distance <= this.riverSearchRadius; distance += 8) {
            for (int sideOffset = -8; sideOffset <= 8; sideOffset += 8) {
                BlockPos samplePos = center.relative(direction, distance).relative(side, sideOffset);
                Holder<Biome> biome = context.biomeSource().getNoiseBiome(
                        QuartPos.fromBlock(samplePos.getX()),
                        QuartPos.fromBlock(samplePos.getY()),
                        QuartPos.fromBlock(samplePos.getZ()),
                        context.randomState().sampler()
                );

                if (biome.is(BiomeTags.IS_RIVER)) {
                    score += this.riverSearchRadius - distance + 8;
                }
            }
        }

        return score;
    }

    private Rotation rotationFor(Direction targetDirection) {
        for (Rotation rotation : Rotation.values()) {
            if (rotation.rotate(this.frontDirection) == targetDirection) {
                return rotation;
            }
        }

        return Rotation.NONE;
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.RIVER_FACING_JIGSAW.get();
    }

    private static class ForcedFirstRotationRandom extends WorldgenRandom {
        private final WorldgenRandom delegate;
        private final int rotationIndex;
        private boolean forceNextRotation = true;

        private ForcedFirstRotationRandom(WorldgenRandom delegate, Rotation rotation) {
            super(RandomSource.create(0L));
            this.delegate = delegate;
            this.rotationIndex = rotation.ordinal();
        }

        @Override
        public int nextInt(int bound) {
            if (this.forceNextRotation && bound == Rotation.values().length) {
                this.forceNextRotation = false;
                this.delegate.nextInt(bound);
                return this.rotationIndex;
            }

            return this.delegate.nextInt(bound);
        }

        @Override
        public int next(int bits) {
            return this.delegate.next(bits);
        }

        @Override
        public RandomSource fork() {
            return this.delegate.fork();
        }

        @Override
        public int getCount() {
            return this.delegate.getCount();
        }
    }
}
