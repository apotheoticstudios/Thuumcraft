package net.apotheoticstudios.thuumcraft.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ModAnimationDefinitions {

    public class draugrAnimation {
        public static final AnimationDefinition DRAUGR_IDLE = AnimationDefinition.Builder.withLength(2.0F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.4976F, 0.109F, -2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.4953F, -0.2172F, -0.019F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(2.4976F, 0.109F, -2.4976F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 5.0F, -6.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 6.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("cloth", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .build();

        public static final AnimationDefinition DRAUGR_MOVE = AnimationDefinition.Builder.withLength(1.25F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(7.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.degreeVec(4.974F, -0.5434F, 2.4693F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(5.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5833F, KeyframeAnimations.posVec(0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.375F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.15F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-1.5155F, -0.351F, -14.0233F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-12.3846F, 1.5043F, -7.7062F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-1.5155F, -0.351F, -14.0233F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.75F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -0.25F, -1.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.4583F, KeyframeAnimations.posVec(0.14F, -0.25F, -0.69F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5417F, KeyframeAnimations.posVec(0.25F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, -0.25F, -1.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.75F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(4.8606F, -2.491F, 7.1442F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.7062F, 5.1908F, 4.0134F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.6667F, KeyframeAnimations.posVec(0.25F, 0.25F, -0.75F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.posVec(0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("cloth", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-8.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-8.33F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .build();

        public static final AnimationDefinition DRAUGR_ATTACK = AnimationDefinition.Builder.withLength(0.5F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.4976F, 0.109F, -2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.125F, KeyframeAnimations.degreeVec(-0.0107F, -2.145F, 1.802F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.degreeVec(34.9756F, 0.9636F, -4.8471F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(24.9929F, -0.0924F, -2.581F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -0.22F, 0.13F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.75F, -2.25F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2083F, KeyframeAnimations.degreeVec(15.3472F, -12.0675F, -8.284F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -0.25F, 0.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -0.25F, -1.25F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -0.25F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-139.1066F, 20.7048F, -17.2077F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-146.3043F, 10.8199F, -25.1509F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.degreeVec(6.2779F, -17.6989F, -1.911F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-13.7221F, -17.6989F, -1.911F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.75F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.posVec(0.75F, -1.5F, -2.75F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("cloth", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.1667F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .build();
    }
}
