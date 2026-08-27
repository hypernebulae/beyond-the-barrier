package hypernebulae.btb.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Overwrite
    public int getAbsoluteMaxWorldSize() {
        return 2147483647;
    }
}
