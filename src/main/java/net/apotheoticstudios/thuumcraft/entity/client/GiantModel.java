package net.apotheoticstudios.thuumcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apotheoticstudios.thuumcraft.entity.animations.ModAnimationDefinitions;
import net.apotheoticstudios.thuumcraft.entity.custom.GiantEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// Model and Texture credit to: Drecxi

public class GiantModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart Giant;
    private final ModelPart Body;
    private final ModelPart Chest;
    private final ModelPart Head;
    private final ModelPart Barba;
    private final ModelPart Ears;
    private final ModelPart Ear2;
    private final ModelPart Ear3;
    private final ModelPart Cejas;
    private final ModelPart Ceja1;
    private final ModelPart Ceja2;
    private final ModelPart Left_arm;
    private final ModelPart Foream;
    private final ModelPart Hand;
    private final ModelPart Right_arm;
    private final ModelPart Foream2;
    private final ModelPart Hand2;
    private final ModelPart Left_leg;
    private final ModelPart Knee;
    private final ModelPart Foot;
    private final ModelPart Right_leg;
    private final ModelPart Knee2;
    private final ModelPart Foot2;

    public GiantModel(ModelPart root) {
        this.Giant = root.getChild("Giant");
        this.Body = this.Giant.getChild("Body");
        this.Chest = this.Body.getChild("Chest");
        this.Head = this.Chest.getChild("Head");
        this.Barba = this.Head.getChild("Barba");
        this.Ears = this.Head.getChild("Ears");
        this.Ear2 = this.Ears.getChild("Ear2");
        this.Ear3 = this.Ears.getChild("Ear3");
        this.Cejas = this.Head.getChild("Cejas");
        this.Ceja1 = this.Cejas.getChild("Ceja1");
        this.Ceja2 = this.Cejas.getChild("Ceja2");
        this.Left_arm = this.Chest.getChild("Left_arm");
        this.Foream = this.Left_arm.getChild("Foream");
        this.Hand = this.Foream.getChild("Hand");
        this.Right_arm = this.Chest.getChild("Right_arm");
        this.Foream2 = this.Right_arm.getChild("Foream2");
        this.Hand2 = this.Foream2.getChild("Hand2");
        this.Left_leg = this.Giant.getChild("Left_leg");
        this.Knee = this.Left_leg.getChild("Knee");
        this.Foot = this.Knee.getChild("Foot");
        this.Right_leg = this.Giant.getChild("Right_leg");
        this.Knee2 = this.Right_leg.getChild("Knee2");
        this.Foot2 = this.Knee2.getChild("Foot2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Giant = partdefinition.addOrReplaceChild("Giant", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Body = Giant.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(119, 231).addBox(-7.9745F, -9.5F, 9.7883F, 14.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(2, 2).addBox(-13.9745F, -1.0F, -12.8117F, 26.0F, 33.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -58.0F, 3.0F));

        PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(58, 164).addBox(-3.0F, 5.0F, -5.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(39, 224).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.59F, 12.0F, -13.8117F, 0.6074F, 0.0546F, -0.0078F));

        PartDefinition Chest = Body.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(0, 121).addBox(-10.59F, -13.0F, -8.8117F, 22.0F, 20.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, 0.0F));

        PartDefinition cube_r2 = Chest.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(6, 65).addBox(-17.0F, -47.0F, -17.0F, 24.0F, 33.0F, 23.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(5.4255F, 16.5F, 8.7883F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r3 = Chest.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(108, 0).addBox(-12.0F, -23.0F, -10.0F, 24.0F, 23.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, -10.0F, -0.8117F, 0.1745F, 0.0F, 0.0F));

        PartDefinition Head = Chest.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -32.0F, -3.0F));

        PartDefinition cube_r4 = Head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(184, 108).addBox(-7.0F, -31.0F, -6.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, 22.0F, 3.7883F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r5 = Head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(184, 132).addBox(-6.0F, -28.0F, -10.0F, 12.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(58, 169).addBox(-2.0F, -35.0F, -11.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(80, 143).addBox(-2.0F, -32.0F, -12.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(154, 154).addBox(-9.0F, -41.0F, -8.0F, 18.0F, 20.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, 16.0F, -2.2117F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r6 = Head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(107, 109).addBox(-10.0F, -0.1484F, -11.3991F, 20.0F, 27.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(107, 109).addBox(-10.0F, -0.1484F, -11.3991F, 20.0F, 27.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, -25.0F, -2.2117F, 0.3054F, 0.0F, 0.0F));

        PartDefinition Barba = Head.addOrReplaceChild("Barba", CubeListBuilder.create(), PartPose.offset(2.0F, -5.0F, -4.0F));

        PartDefinition Ears = Head.addOrReplaceChild("Ears", CubeListBuilder.create(), PartPose.offset(1.0F, -12.0F, 0.0F));

        PartDefinition Ear2 = Ears.addOrReplaceChild("Ear2", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.6545F));

        PartDefinition cube_r7 = Ear2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(58, 175).mirror().addBox(-17.6963F, -1.3971F, -5.7284F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 130).mirror().addBox(-11.6963F, -1.4407F, -7.2265F, 10.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.7411F, -3.0F, -5.2117F, 0.4391F, 0.3307F, -0.0752F));

        PartDefinition Ear3 = Ears.addOrReplaceChild("Ear3", CubeListBuilder.create(), PartPose.offsetAndRotation(20.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.6545F));

        PartDefinition cube_r8 = Ear3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(58, 175).addBox(2.0404F, -6.0335F, 0.1118F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(80, 130).addBox(-7.9596F, -6.0771F, -1.3863F, 10.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.7411F, -3.0F, -5.2117F, 0.4391F, -0.3307F, 0.0752F));

        PartDefinition Cejas = Head.addOrReplaceChild("Cejas", CubeListBuilder.create(), PartPose.offset(1.0F, -17.0F, 0.0F));

        PartDefinition Ceja1 = Cejas.addOrReplaceChild("Ceja1", CubeListBuilder.create(), PartPose.offset(7.0F, -2.0F, -8.0F));

        PartDefinition cube_r9 = Ceja1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(80, 139).addBox(-4.4573F, -1.0629F, -0.4485F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.59F, 1.0F, -6.2117F, 0.0F, 0.0F, 0.0436F));

        PartDefinition Ceja2 = Cejas.addOrReplaceChild("Ceja2", CubeListBuilder.create(), PartPose.offset(-8.0F, -2.0F, -8.0F));

        PartDefinition cube_r10 = Ceja2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(80, 139).mirror().addBox(-3.5427F, -1.0629F, -0.4485F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.6411F, 1.0F, -6.2117F, 0.0F, 0.0F, -0.0436F));

        PartDefinition Left_arm = Chest.addOrReplaceChild("Left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-14.0F, -30.0F, -2.0F, 0.1597F, 0.0059F, -0.3887F));

        PartDefinition cube_r11 = Left_arm.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 164).addBox(-15.3033F, 10.1257F, -4.8884F, 15.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(130, 192).mirror().addBox(-14.3033F, -1.8743F, -3.8884F, 13.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0457F, 0.7677F, -3.3564F, -0.228F, 0.1298F, 0.5087F));

        PartDefinition Foream = Left_arm.addOrReplaceChild("Foream", CubeListBuilder.create(), PartPose.offsetAndRotation(-14.0798F, 14.7677F, -4.5447F, 2.0338F, 0.4969F, -0.2225F));

        PartDefinition cube_r12 = Foream.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(180, 192).addBox(-16.7798F, -3.6868F, 14.5546F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.7115F, -18.6741F, -1.1029F, -2.3881F, 0.7093F, 0.1579F));

        PartDefinition cube_r13 = Foream.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(200, 0).addBox(-14.7798F, -9.6868F, 16.5546F, 9.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.6115F, -19.4741F, -0.2029F, -2.3881F, 0.7093F, 0.1579F));

        PartDefinition Hand = Foream.addOrReplaceChild("Hand", CubeListBuilder.create(), PartPose.offset(-6.514F, -15.4741F, -10.3912F));

        PartDefinition cube_r14 = Hand.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(106, 61).addBox(-16.7798F, 8.8132F, -30.4454F, 12.0F, 12.0F, 35.0F, new CubeDeformation(0.0F))
                .texOffs(80, 154).addBox(-14.7798F, 10.8132F, 2.5546F, 8.0F, 8.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(24.1255F, -4.0F, 10.1883F, -2.3881F, 0.7093F, 0.1579F));

        PartDefinition cube_r15 = Hand.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(180, 215).mirror().addBox(-14.6538F, 11.099F, 20.0352F, 10.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(26.1255F, -5.0F, 13.1883F, -2.3881F, 0.7093F, 0.1579F));

        PartDefinition Right_arm = Chest.addOrReplaceChild("Right_arm", CubeListBuilder.create(), PartPose.offset(12.0F, -26.0F, -1.0F));

        PartDefinition cube_r16 = Right_arm.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 190).addBox(-1.0F, 5.0F, -5.0F, 9.0F, 15.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(80, 191).addBox(-3.0F, -7.0F, -6.0F, 13.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0255F, 1.0F, -0.8117F, 0.0F, 0.0F, -0.1309F));

        PartDefinition cube_r17 = Right_arm.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(198, 57).addBox(-7.0F, -9.0F, -6.0F, 11.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3255F, 17.6F, -0.8117F, 0.0F, 0.0F, -0.0873F));

        PartDefinition Foream2 = Right_arm.addOrReplaceChild("Foream2", CubeListBuilder.create(), PartPose.offset(8.0F, 21.0F, 0.0F));

        PartDefinition cube_r18 = Foream2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(180, 192).addBox(-5.5464F, -1.6912F, -6.0F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0255F, 6.0F, -0.8117F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r19 = Foream2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 205).addBox(-1.0F, 20.0F, -5.0F, 9.0F, 15.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9745F, -20.0F, -0.8117F, 0.0F, 0.0F, -0.1309F));

        PartDefinition Hand2 = Foream2.addOrReplaceChild("Hand2", CubeListBuilder.create(), PartPose.offset(1.0F, 16.0F, 0.0F));

        PartDefinition cube_r20 = Hand2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(80, 216).addBox(-4.5464F, 9.3088F, -5.0F, 10.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0255F, -10.0F, -0.8117F, 0.0F, 0.0F, 0.0436F));

        PartDefinition Left_leg = Giant.addOrReplaceChild("Left_leg", CubeListBuilder.create(), PartPose.offset(6.0F, -50.0F, 2.0F));

        PartDefinition cube_r21 = Left_leg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(37, 189).addBox(-5.0F, -21.0F, -7.0F, 10.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, 18.0F, 0.1883F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Knee = Left_leg.addOrReplaceChild("Knee", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -1.0F));

        PartDefinition cube_r22 = Knee.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(222, 215).addBox(-4.0F, -17.0F, -5.0F, 8.0F, 18.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.41F, 14.0F, 2.1883F, 0.0436F, 0.0F, 0.0F));

        PartDefinition Foot = Knee.addOrReplaceChild("Foot", CubeListBuilder.create().texOffs(200, 80).addBox(-4.59F, 3.0F, -4.8117F, 10.0F, 17.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(158, 45).addBox(-3.59F, 14.0F, -9.8117F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(80, 121).addBox(-3.59F, 16.0F, -14.8117F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(122, 217).addBox(-5.59F, 0.0F, -5.8117F, 12.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 1.0F));

        PartDefinition Right_leg = Giant.addOrReplaceChild("Right_leg", CubeListBuilder.create(), PartPose.offset(-8.0F, -50.0F, 2.0F));

        PartDefinition cube_r23 = Right_leg.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(37, 189).mirror().addBox(-5.0F, -21.0F, -7.0F, 10.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.59F, 18.0F, 0.1883F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Knee2 = Right_leg.addOrReplaceChild("Knee2", CubeListBuilder.create(), PartPose.offset(2.0F, 18.0F, -1.0F));

        PartDefinition cube_r24 = Knee2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(222, 215).mirror().addBox(-4.0F, -17.0F, -5.0F, 8.0F, 18.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.41F, 14.0F, 2.1883F, 0.0436F, 0.0F, 0.0F));

        PartDefinition Foot2 = Knee2.addOrReplaceChild("Foot2", CubeListBuilder.create().texOffs(200, 80).mirror().addBox(-5.41F, 3.0F, -4.8117F, 10.0F, 17.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(158, 45).mirror().addBox(-4.41F, 14.0F, -9.8117F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 121).mirror().addBox(-4.41F, 16.0F, -14.8117F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(122, 217).mirror().addBox(-6.41F, 0.0F, -5.8117F, 12.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationDefinitions.giantAnimation.GIANT_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((GiantEntity) entity).idleAnimationState, ModAnimationDefinitions.giantAnimation.GIANT_IDLE, ageInTicks, 1f);
        this.animate(((GiantEntity) entity).attackAnimationState, ModAnimationDefinitions.giantAnimation.GIANT_ATTACK, ageInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Giant.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Giant;
    }
}
