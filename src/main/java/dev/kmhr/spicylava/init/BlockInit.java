package dev.kmhr.spicylava.init;

import dev.kmhr.spicylava.SpicyLava;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    // DeferredRegister for registering blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpicyLava.MODID);

    // Registering the custom Lava Spicy Block
    public static final RegistryObject<LiquidBlock> LAVA_SPICY_BLOCK = BLOCKS.register(
            "lava_spicy_block",
            () -> new LiquidBlock(
                    // Ensuring proper reference to FluidInit's LAVA_SPICY_SOURCE
                    () -> FluidInit.LAVA_SPICY_SOURCE.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE) // Fire-like color for the block
                            .replaceable() // Makes the block replaceable
                            .liquid() // Specifies it as a liquid block
                            .lightLevel(state -> 15) // Full light level
                            .pushReaction(PushReaction.DESTROY) // Specifies reaction to being pushed
            ));
}
