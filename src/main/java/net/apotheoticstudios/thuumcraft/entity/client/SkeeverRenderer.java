package net.apotheoticstudios.thuumcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.entity.custom.SkeeverEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkeeverRenderer extends MobRenderer<SkeeverEntity, SkeeverModel<SkeeverEntity>> {

    public SkeeverRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SkeeverModel<>(pContext.bakeLayer(ModModelLayers.SKEEVER_LAYER)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(SkeeverEntity pEntity) {
        return new ResourceLocation(Thuumcraft.MOD_ID, "textures/entity/skeever.png");
    }


    @Override
    public void render(SkeeverEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
