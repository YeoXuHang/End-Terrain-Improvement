package net.yeoxuhang.example.improved_end_terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public class CustomDensityType {

    public record MainEndIslandDistanceGradient(int minDistance, int maxDistance, double fromValue, double toValue) implements DensityFunction.SimpleFunction {
        private static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0, 1000000.0);
        private static final MapCodec<MainEndIslandDistanceGradient> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.INT.fieldOf("min_distance").forGetter(MainEndIslandDistanceGradient::minDistance), Codec.INT.fieldOf("max_distance").forGetter(MainEndIslandDistanceGradient::maxDistance), NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(MainEndIslandDistanceGradient::fromValue), NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(MainEndIslandDistanceGradient::toValue)).apply(instance, MainEndIslandDistanceGradient::new));
        public static final KeyDispatchDataCodec<MainEndIslandDistanceGradient> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

        @Override
        public double minValue() {
            return Math.min(fromValue, toValue);
        }

        @Override
        public double maxValue() {
            return Math.max(fromValue, toValue);
        }

        @Override
        public double compute(FunctionContext context) {
            float f = context.blockX();
            float h = context.blockZ();
            float distance = Mth.sqrt(f * f + h * h);
            return Mth.clampedMap(distance, minDistance, maxDistance, fromValue, toValue);
        }

        @Override
        public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return MainEndIslandDistanceGradient.CODEC;
        }
    }

    private static void register(String string, KeyDispatchDataCodec<? extends DensityFunction> keyDispatchDataCodec) {
        Registry.register(Registry.DENSITY_FUNCTION_TYPES, new ResourceLocation(EndTerrain.MOD_ID, string), keyDispatchDataCodec.codec());
    }

    public static void init(){
        register("main_end_island_distance_gradient", MainEndIslandDistanceGradient.CODEC);
    }
}