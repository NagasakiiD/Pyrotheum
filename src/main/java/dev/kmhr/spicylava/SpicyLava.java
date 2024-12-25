package dev.kmhr.spicylava;

import dev.kmhr.spicylava.init.BlockInit;
import dev.kmhr.spicylava.init.CreativeTabInit;
import dev.kmhr.spicylava.init.FluidInit;
import dev.kmhr.spicylava.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;

@Mod(SpicyLava.MODID)
public class SpicyLava {
    public static final String MODID = "spicylava";

    public SpicyLava() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register mod components
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        FluidInit.FLUID_TYPES.register(modEventBus);
        FluidInit.FLUIDS.register(modEventBus);
        CreativeTabInit.TABS.register(modEventBus);

        // Register event listeners
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Add fluid interactions
        FluidInteractionRegistry.addInteraction(FluidInit.LAVA_SPICY_TYPE.get(),
                new FluidInteractionRegistry.InteractionInformation(
                        ForgeMod.LAVA_TYPE.get(),
                        Blocks.STONE.defaultBlockState()
                )
        );

        FluidInteractionRegistry.addInteraction(FluidInit.LAVA_SPICY_TYPE.get(),
                new FluidInteractionRegistry.InteractionInformation(
                        (level, currentPos, relativePos, currentState) -> level.getBlockState(relativePos).is(Blocks.SAND),
                        Blocks.GLASS.defaultBlockState()
                )
        );
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide && event.level instanceof ServerLevel level) {
            spreadFireAroundSpicyLava(level);
        }
    }

    private void spreadFireAroundSpicyLava(ServerLevel level) {
        Random random = new Random();

        // Iterate through all positions within a range of spicy lava
        for (BlockPos pos : BlockPos.betweenClosed(
                level.getMinBuildHeight(), level.getMinBuildHeight(), level.getMinBuildHeight(),
                level.getMaxBuildHeight(), level.getMaxBuildHeight(), level.getMaxBuildHeight())) {

            BlockState blockState = level.getBlockState(pos);
            if (blockState.getBlock() == Blocks.LAVA) { // Replace with your spicy lava block
                for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                    BlockState nearbyBlockState = level.getBlockState(nearbyPos);

                    // Check if the block is flammable or air
                    if (nearbyBlockState.isAir() || nearbyBlockState.getBlock() == Blocks.FIRE) {
                        if (random.nextFloat() < 0.3f) { // Adjust probability
                            level.setBlock(nearbyPos, Blocks.FIRE.defaultBlockState(), 11);
                        }
                    }
                }
            }
        }
    }
}
