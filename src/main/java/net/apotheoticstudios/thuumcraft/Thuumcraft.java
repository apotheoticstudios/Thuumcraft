package net.apotheoticstudios.thuumcraft;

import com.mojang.logging.LogUtils;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.entity.ModEntities;
import net.apotheoticstudios.thuumcraft.entity.client.DraugrRenderer;
import net.apotheoticstudios.thuumcraft.entity.client.GiantRenderer;
import net.apotheoticstudios.thuumcraft.entity.client.SkeeverRenderer;
import net.apotheoticstudios.thuumcraft.item.ModCreativeModeTabs;
import net.apotheoticstudios.thuumcraft.item.ModFoods;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.apotheoticstudios.thuumcraft.loot.ModLootModifiers;
import net.apotheoticstudios.thuumcraft.magic.IronSpellbooksItemDisabler;
import net.apotheoticstudios.thuumcraft.magic.ModSpellSchools;
import net.apotheoticstudios.thuumcraft.magic.spell.ModSpells;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.apotheoticstudios.thuumcraft.sound.ModSounds;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.apotheoticstudios.thuumcraft.worldgen.structure.ModStructureTypes;
import net.apotheoticstudios.thuumcraft.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Thuumcraft.MOD_ID)
public class Thuumcraft {
    public static final String MOD_ID = "thuumcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Thuumcraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        ModCreativeModeTabs.register(modEventBus);

        ModAttributes.register(modEventBus);

        ModEffects.register(modEventBus);

        ModSpellSchools.register(modEventBus);
        ModSpells.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);

        ModStructureTypes.register(modEventBus);

        ModTrunkPlacerTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)  {
        ModMessages.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        IronSpellbooksItemDisabler.removeFromCreativeTab(event);
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.DRAUGR.get(), DraugrRenderer::new);
            EntityRenderers.register(ModEntities.GIANT.get(), GiantRenderer::new);
            EntityRenderers.register(ModEntities.SKEEVER.get(), SkeeverRenderer::new);
        }
    }
}
