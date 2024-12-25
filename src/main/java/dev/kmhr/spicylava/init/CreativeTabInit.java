package dev.kmhr.spicylava.init;

import dev.kmhr.spicylava.SpicyLava;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpicyLava.MODID);

    public static final List<Supplier<? extends ItemLike>> SPICYLAVA_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> SPICYLAVA_TAB = TABS.register("spicylava_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.spicylava_tab"))
                    .icon(ItemInit.LAVA_SPICY_BUCKET.get()::getDefaultInstance) // uses "method reference", like a supplier? can also pass instead () -> new ItemStack(ItemInit.STICK_TOASTING.get())
                    .displayItems((displayParameters, output) ->
                        SPICYLAVA_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .build()
    );

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        SPICYLAVA_TAB_ITEMS.add(itemLike);
        return itemLike;
    }
}
