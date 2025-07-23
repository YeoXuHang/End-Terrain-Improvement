package net.yeoxuhang.example.improved_end_terrain;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.RandomState;

import java.text.DecimalFormat;

public class DebugNoiseOverlay implements HudRenderCallback {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.000000");

    @Override
    public void onHudRender(PoseStack poseStack, float tickDelta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;

        if (player == null || level == null) return;
        ServerLevel serverLevel = mc.getSingleplayerServer().getLevel(ServerLevel.END);
        BlockPos pos = player.blockPosition();
        ServerChunkCache serverChunkCache = serverLevel.getChunkSource();
        RandomState randomState = serverChunkCache.randomState();
        NoiseRouter router = randomState.router();
        DensityFunction.SinglePointContext ctx = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());

        double ridges = router.ridges().compute(ctx);
        String debugLine = "Temperature: " + format(router.temperature().compute(ctx))
                + " Humidity: " + format(router.vegetation().compute(ctx))
                + " Continentalness: " + format(router.continents().compute(ctx));

        String debugLine2 = "Erosion: " + format(router.erosion().compute(ctx))
                + " Depth: " + format(router.depth().compute(ctx))
                + " Offset: " + format(ridges);

        String debugLine3 = "Biomes: " + printBiome(level.getBiome(pos));

        String coords = "X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ();


        Font font = mc.font;
        font.draw(poseStack, debugLine, 5, 60, 0xFFFFFF);
        font.draw(poseStack, debugLine2, 5, 80, 0xFFFFFF);
        font.draw(poseStack, debugLine3, 5, 100, 0xFFFFFF);
        font.draw(poseStack, coords, 5, 120, 0xFFFFFF);
    }

    private String format(double d) {
        return decimalFormat.format(d);
    }

    private static String printBiome(Holder<Biome> holder) {
        return holder.unwrap().map(resourceKey -> resourceKey.location().toString(), biome -> "[unregistered " + biome + "]");
    }
}
