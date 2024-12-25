package dev.kmhr.spicylava.init;

import dev.kmhr.spicylava.SpicyLava;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    // DeferredRegister for registering items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpicyLava.MODID);

    // Registering the custom Lava Spicy Bucket
    public static final RegistryObject<Item> LAVA_SPICY_BUCKET = ITEMS.register(
            "lava_spicy_bucket",
            () -> new BucketItem(
                    // Ensure proper supplier reference to LAVA_SPICY_SOURCE
                    () -> FluidInit.LAVA_SPICY_SOURCE.get(),
                    new Item.Properties()
                            .craftRemainder(Items.BUCKET) // Returns an empty bucket after use
                            .stacksTo(1) // Maximum stack size of 1
            )
    );
}
