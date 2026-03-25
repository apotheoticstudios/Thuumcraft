package net.apotheoticstudios.thuumcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.entity.custom.DraugrEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3dStack;

public class DraugrRenderer extends MobRenderer<DraugrEntity, DraugrModel<DraugrEntity>> {

    public DraugrRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DraugrModel<>(pContext.bakeLayer(ModModelLayers.DRAUGR_LAYER)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(DraugrEntity pEntity) {
        return new ResourceLocation(Thuumcraft.MOD_ID, "textures/entity/draugr.png");
    }


    @Override
    public void render(DraugrEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
