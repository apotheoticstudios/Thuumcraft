package net.apotheoticstudios.thuumcraft.entity.client;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class DraugrModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart hair;
	private final ModelPart beard;
	private final ModelPart body;
	private final ModelPart cloth;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public DraugrModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.head = this.main.getChild("head");
		this.hair = this.head.getChild("hair");
		this.beard = this.head.getChild("beard");
		this.body = this.main.getChild("body");
		this.cloth = this.body.getChild("cloth");
		this.left_arm = this.main.getChild("left_arm");
		this.right_arm = this.main.getChild("right_arm");
		this.left_leg = this.main.getChild("left_leg");
		this.right_leg = this.main.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(32, 52).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(48, 0).addBox(-4.0F, -3.0F, -4.025F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 16).addBox(-4.0F, -10.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(1, 17).addBox(-4.0F, -10.75F, -2.5F, 8.0F, 11.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition cloth = body.addOrReplaceChild("cloth", CubeListBuilder.create().texOffs(0, 55).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.25F, -0.5F));

		PartDefinition left_arm = main.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 32).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_arm = main.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return truemain;
	}
}