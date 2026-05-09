package net.apotheoticstudios.thuumcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apotheoticstudios.thuumcraft.entity.animations.ModAnimationDefinitions;
import net.apotheoticstudios.thuumcraft.entity.custom.SkeeverEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// Model and Texture credit to: Drecxi

public class SkeeverModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart Skeever;
    private final ModelPart Body;
    private final ModelPart Neck;
    private final ModelPart Head;
    private final ModelPart Mouth;
    private final ModelPart Ears;
    private final ModelPart Ear_Left;
    private final ModelPart Ear_Right;
    private final ModelPart Body_Back;
    private final ModelPart Tail;
    private final ModelPart Tail_Mid;
    private final ModelPart Tail_Back;
    private final ModelPart Left_arm;
    private final ModelPart Foream_Left;
    private final ModelPart Foot_Left;
    private final ModelPart Right_arm;
    private final ModelPart Foream_Right;
    private final ModelPart Foot_Right;
    private final ModelPart Left_Leg;
    private final ModelPart Knee_Left;
    private final ModelPart Foot_Back_Left;
    private final ModelPart Right_Leg;
    private final ModelPart Knee_Right;
    private final ModelPart Foot_Back_Right;

    public SkeeverModel(ModelPart root) {
        this.Skeever = root.getChild("Skeever");
        this.Body = this.Skeever.getChild("Body");
        this.Neck = this.Body.getChild("Neck");
        this.Head = this.Neck.getChild("Head");
        this.Mouth = this.Head.getChild("Mouth");
        this.Ears = this.Head.getChild("Ears");
        this.Ear_Left = this.Ears.getChild("Ear_Left");
        this.Ear_Right = this.Ears.getChild("Ear_Right");
        this.Body_Back = this.Body.getChild("Body_Back");
        this.Tail = this.Body_Back.getChild("Tail");
        this.Tail_Mid = this.Tail.getChild("Tail_Mid");
        this.Tail_Back = this.Tail_Mid.getChild("Tail_Back");
        this.Left_arm = this.Skeever.getChild("Left_arm");
        this.Foream_Left = this.Left_arm.getChild("Foream_Left");
        this.Foot_Left = this.Foream_Left.getChild("Foot_Left");
        this.Right_arm = this.Skeever.getChild("Right_arm");
        this.Foream_Right = this.Right_arm.getChild("Foream_Right");
        this.Foot_Right = this.Foream_Right.getChild("Foot_Right");
        this.Left_Leg = this.Skeever.getChild("Left_Leg");
        this.Knee_Left = this.Left_Leg.getChild("Knee_Left");
        this.Foot_Back_Left = this.Knee_Left.getChild("Foot_Back_Left");
        this.Right_Leg = this.Skeever.getChild("Right_Leg");
        this.Knee_Right = this.Right_Leg.getChild("Knee_Right");
        this.Foot_Back_Right = this.Knee_Right.getChild("Foot_Back_Right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Skeever = partdefinition.addOrReplaceChild("Skeever", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -0.1406F));

        PartDefinition Body = Skeever.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(28, 15).addBox(-3.5F, -4.0F, -10.0F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.75F));

        PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-3.0F, -2.7972F, -7.1851F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition Neck = Body.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(28, 28).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -11.0F));

        PartDefinition Head = Neck.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 29).addBox(-2.5F, -3.5F, -3.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 8).addBox(-1.5F, -1.5F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 13).addBox(-0.5F, -1.5F, -7.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition cube_r2 = Head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 14).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.25F, 0.5F, -6.875F, 0.0F, 0.0F, 1.0036F));

        PartDefinition cube_r3 = Head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(30, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(0.25F, 0.5F, -6.875F, 0.0F, 0.0F, -1.0036F));

        PartDefinition cube_r4 = Head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 47).mirror().addBox(-2.0F, -2.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.125F, -1.5F, 0.875F, 0.3124F, -0.2079F, -0.0666F));

        PartDefinition cube_r5 = Head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(44, 47).addBox(1.0F, -2.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, -1.5F, 0.875F, 0.3124F, 0.2079F, 0.0666F));

        PartDefinition Mouth = Head.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(44, 8).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(46, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.002F)), PartPose.offset(0.0F, 1.0F, -3.5F));

        PartDefinition Ears = Head.addOrReplaceChild("Ears", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition Ear_Left = Ears.addOrReplaceChild("Ear_Left", CubeListBuilder.create(), PartPose.offset(1.5F, 0.25F, -0.375F));

        PartDefinition cube_r6 = Ear_Left.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 39).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -0.125F, 0.375F, 0.0F, 0.0F, 0.9163F));

        PartDefinition Ear_Right = Ears.addOrReplaceChild("Ear_Right", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -0.375F));

        PartDefinition cube_r7 = Ear_Right.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(14, 39).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 0.125F, 0.375F, 0.0F, 0.0F, -0.9163F));

        PartDefinition Body_Back = Body.addOrReplaceChild("Body_Back", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.9537F, -0.3007F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition Tail = Body_Back.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, -2.4537F, 6.6993F));

        PartDefinition cube_r8 = Tail.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.5F, 0.375F, -0.2182F, 0.0F, 0.0F));

        PartDefinition Tail_Mid = Tail.addOrReplaceChild("Tail_Mid", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.75F));

        PartDefinition cube_r9 = Tail_Mid.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 46).addBox(0.0F, -1.1105F, -0.1216F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.375F, -0.0436F, 0.0F, 0.0F));

        PartDefinition Tail_Back = Tail_Mid.addOrReplaceChild("Tail_Back", CubeListBuilder.create(), PartPose.offset(0.0F, 0.125F, 3.75F));

        PartDefinition cube_r10 = Tail_Back.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(20, 38).addBox(0.5F, -0.7988F, -0.2424F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.25F, 0.375F, 0.1745F, 0.0F, 0.0F));

        PartDefinition Left_arm = Skeever.addOrReplaceChild("Left_arm", CubeListBuilder.create(), PartPose.offset(3.0F, -8.0F, -6.0F));

        PartDefinition cube_r11 = Left_arm.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(36, 38).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition Foream_Left = Left_arm.addOrReplaceChild("Foream_Left", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition cube_r12 = Foream_Left.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(20, 29).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition Foot_Left = Foream_Left.addOrReplaceChild("Foot_Left", CubeListBuilder.create().texOffs(26, 46).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(36, 47).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r13 = Foot_Left.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 47).mirror().addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 1.0F, -0.375F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r14 = Foot_Left.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(36, 47).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.375F, 0.0F, -0.4363F, 0.0F));

        PartDefinition Right_arm = Skeever.addOrReplaceChild("Right_arm", CubeListBuilder.create(), PartPose.offset(-3.0F, -8.0F, -6.0F));

        PartDefinition cube_r15 = Right_arm.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(36, 38).mirror().addBox(-2.0F, -2.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition Foream_Right = Right_arm.addOrReplaceChild("Foream_Right", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition cube_r16 = Foream_Right.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(20, 29).mirror().addBox(-1.0F, -7.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition Foot_Right = Foream_Right.addOrReplaceChild("Foot_Right", CubeListBuilder.create().texOffs(26, 46).mirror().addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(36, 47).mirror().addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r17 = Foot_Right.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(36, 47).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.375F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r18 = Foot_Right.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(36, 47).mirror().addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 1.0F, -0.375F, 0.0F, 0.4363F, 0.0F));

        PartDefinition Left_Leg = Skeever.addOrReplaceChild("Left_Leg", CubeListBuilder.create(), PartPose.offset(4.0F, -8.0F, 7.0F));

        PartDefinition cube_r19 = Left_Leg.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition Knee_Left = Left_Leg.addOrReplaceChild("Knee_Left", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition cube_r20 = Knee_Left.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(20, 29).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Foot_Back_Left = Knee_Left.addOrReplaceChild("Foot_Back_Left", CubeListBuilder.create().texOffs(26, 46).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(36, 47).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r21 = Foot_Back_Left.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(36, 47).mirror().addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 1.0F, -0.375F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r22 = Foot_Back_Left.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(36, 47).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.375F, 0.0F, -0.4363F, 0.0F));

        PartDefinition Right_Leg = Skeever.addOrReplaceChild("Right_Leg", CubeListBuilder.create(), PartPose.offset(-4.0F, -8.0F, 7.0F));

        PartDefinition cube_r23 = Right_Leg.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 39).mirror().addBox(-2.0F, -2.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition Knee_Right = Right_Leg.addOrReplaceChild("Knee_Right", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition cube_r24 = Knee_Right.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(20, 29).mirror().addBox(-1.0F, -7.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Foot_Back_Right = Knee_Right.addOrReplaceChild("Foot_Back_Right", CubeListBuilder.create().texOffs(26, 46).mirror().addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(36, 47).mirror().addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r25 = Foot_Back_Right.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(36, 47).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.375F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r26 = Foot_Back_Right.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(36, 47).mirror().addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 1.0F, -0.375F, 0.0F, 0.4363F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        SkeeverEntity skeever = (SkeeverEntity) entity;

        if (skeever.isChasingTarget()) {
            this.animateWalk(ModAnimationDefinitions.skeeverAnimation.SKEEVER_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        } else {
            this.animateWalk(ModAnimationDefinitions.skeeverAnimation.SKEEVER_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        }

        this.animate(skeever.idleAnimationState, ModAnimationDefinitions.skeeverAnimation.SKEEVER_IDLE, ageInTicks, 1f);
        this.animate(skeever.attackAnimationState, ModAnimationDefinitions.skeeverAnimation.SKEEVER_ATTACK, ageInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Skeever.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Skeever;
    }
}
