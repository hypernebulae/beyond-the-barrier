package hypernebulae.btb.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(doubleValue = 2.9999999E7D)
    )
    private double example$modifyPositiveWorldLimit(double original) {
        return Double.MAX_VALUE;
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(doubleValue = -2.9999999E7D)
    )
    private double example$modifyNegativeWorldLimit(double original) {
        return -Double.MAX_VALUE;
    }
}