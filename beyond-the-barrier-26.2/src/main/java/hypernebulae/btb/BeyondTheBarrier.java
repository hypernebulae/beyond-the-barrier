package hypernebulae.btb;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeyondTheBarrier implements ModInitializer {
	public static final String MOD_ID = "beyond-the-barrier";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean value = true;

	@Override
	public void onInitialize() {
		LOGGER.info("initializing beyond the barrier - get ready to break the limits!");
		FarlandsCommand.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
