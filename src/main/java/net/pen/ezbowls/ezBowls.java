package net.pen.ezbowls;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ezBowls implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ezbowls");

	@Override
	public void onInitialize() {
		LOGGER.info("Your soup bowls stack on their own!");

	}
}
