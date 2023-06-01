package net.pen.ezbowls;

import net.fabricmc.api.ModInitializer;
import net.pen.ezbowls.item.CustomItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ezBowls implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("EZBOWLS");

	public static final CustomItem CUSTOM_ITEM =
			Registry.register(Registry.ITEM, new Identifier("ezbowls", "custom_item"),
			new CustomItem(new FabricItemSettings().group(ItemGroup.MISC)));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Your soup bowls stack on their own!");
		assert CUSTOM_ITEM != null;
		// IDEA was really upset that i didn't use this
		// so i "used" it

	}
}
