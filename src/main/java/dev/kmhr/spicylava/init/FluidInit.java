package dev.kmhr.spicylava.init;

import dev.kmhr.spicylava.SpicyLava;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class FluidInit {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, SpicyLava.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SpicyLava.MODID);

    public static ForgeFlowingFluid.Properties fluidProperties() {
        return new ForgeFlowingFluid.Properties(
                LAVA_SPICY_TYPE,
                LAVA_SPICY_SOURCE,
                LAVA_SPICY_FLOWING)
                .block(BlockInit.LAVA_SPICY_BLOCK)
                .bucket(ItemInit.LAVA_SPICY_BUCKET)
                .explosionResistance(1000F)
                .tickRate(10);
    }

    public static final RegistryObject<FlowingFluid> LAVA_SPICY_SOURCE = FLUIDS.register(
            "lava_spicy",
            () -> new ForgeFlowingFluid.Source(fluidProperties()));
    public static final RegistryObject<Fluid> LAVA_SPICY_FLOWING = FLUIDS.register(
            "lava_spicy_flowing",
            () -> new ForgeFlowingFluid.Flowing(fluidProperties()));
    public static final RegistryObject<FluidType> LAVA_SPICY_TYPE = FLUID_TYPES.register(
            "lava_spicy",
            () -> new FluidType(FluidType.Properties.create()
                    .canSwim(false)
                    .canDrown(false)
                    .canExtinguish(false)
                    .canHydrate(false)
                    .canPushEntity(true)
                    .lightLevel(15)
                    .density(2000)
                    .viscosity(1200)
                    .temperature(4000)
                    .rarity(Rarity.RARE)
                    .pathType(BlockPathTypes.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
            ) {

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {

                        private static final String ID = "lava_spicy";
                        private static final ResourceLocation FLOW = new ResourceLocation(SpicyLava.MODID + ":block/fluid/" + ID + "_flow");
                        private static final ResourceLocation STILL = new ResourceLocation(SpicyLava.MODID + ":block/fluid/" + ID + "_still");

                        @Override
                        public ResourceLocation getStillTexture() {
                            return STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return FLOW;
                        }

                        @Override
                        public ResourceLocation getOverlayTexture() {
                            return null;
                        }
                    });
                }

                @Override
                public double motionScale(Entity entity) {
                    return entity.level().dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
                }

                @Override
                public void setItemMovement(ItemEntity entity) {
                    Vec3 vec3 = entity.getDeltaMovement();
                    entity.setDeltaMovement(vec3.x * 0.95F, vec3.y + (vec3.y < 0.06F ? 5.0E-4F : 0.0F), vec3.z * 0.95F);

                    // Ignite non-fire-resistant items
                    if (!entity.getItem().getItem().isFireResistant()) {
                        entity.setSecondsOnFire(5);
                    }
                }

                // Custom fluid interaction with entities
                public void onEntityInside(Entity entity) {
                    // Burn entities that are not fire-immune
                    if (!entity.fireImmune()) {
                        entity.setSecondsOnFire(5); // Set the entity on fire for 5 seconds
                    }
                }
            }
    );
}
