package net.yeoxuhang.example.improved_end_terrain;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class EndTerrain implements ModInitializer {

    /**
     @Note: The datapack might be broken in future version
     **/
    public static final String MOD_ID = "improved_end_terrain";
    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(MOD_ID, "improved_end_terrain"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });
        CustomDensityType.init();
    }
}
