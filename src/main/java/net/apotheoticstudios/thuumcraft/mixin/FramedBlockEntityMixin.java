package net.apotheoticstudios.thuumcraft.mixin;

import net.minecraft.core.Direction;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.camo.CamoContainer;

@Mixin(value = FramedBlockEntity.class, remap = false)
public abstract class FramedBlockEntityMixin {
    @Inject(method = "canSustainPlant", at = @At("HEAD"), cancellable = true, remap = false)
    private static void thuumcraft$failClosedWhenLevelMissing(FramedBlockEntity blockEntity, CamoContainer camo,
                                                              Direction side, IPlantable plantable,
                                                              CallbackInfoReturnable<Boolean> callback) {
        if (blockEntity.getLevel() == null) {
            callback.setReturnValue(false);
        }
    }
}
