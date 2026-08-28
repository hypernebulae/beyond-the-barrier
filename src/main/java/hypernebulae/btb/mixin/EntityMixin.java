package hypernebulae.btb.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(
            method = "absSnapTo(DDD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(DDD)D"
            )
    )
    private double example$modifySnapLimit(
            double value,
            double min,
            double max
    ) {
        return Math.max(-Double.MAX_VALUE, Math.min(Double.MAX_VALUE, value));
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = 3.0000512E7D)
    )
    private double example$expandHorizontalPositiveLimit(double original) {
        return Double.MAX_VALUE;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = -3.0000512E7D)
    )
    private double example$expandHorizontalNegativeLimit(double original) {
        return -Double.MAX_VALUE;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = 2.0E7D)
    )
    private double example$expandVerticalPositiveLimit(double original) {
        return Double.MAX_VALUE;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = -2.0E7D)
    )
    private double example$expandVerticalNegativeLimit(double original) {
        return -Double.MAX_VALUE;
    }
}