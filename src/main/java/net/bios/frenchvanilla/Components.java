package net.bios.frenchvanilla;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.bios.frenchvanilla.carrying_bucket.CarryingBucketItemComponent;
import net.bios.frenchvanilla.deathlock.DeathKeyItemComponent;
import net.bios.frenchvanilla.deathlock.DeathLocksComponent;
import net.bios.frenchvanilla.home.HomeComponent;
import net.bios.frenchvanilla.player_settings.PlayerSettingsComponent;
import net.bios.frenchvanilla.timber.TimberTaskManagerComponent;
import net.minecraft.item.Items;

public class Components implements EntityComponentInitializer, ItemComponentInitializer, WorldComponentInitializer {
    public static final ComponentKey<CarryingBucketItemComponent> CARRYING_BUCKET =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("carrying_bucket"), CarryingBucketItemComponent.class);

    public static final ComponentKey<DeathLocksComponent> DEATH_LOCKS =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("deathlocks"), DeathLocksComponent.class);

    public static final ComponentKey<DeathKeyItemComponent> DEATH_KEY =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("deathkey"), DeathKeyItemComponent.class);

    public static final ComponentKey<HomeComponent> HOME =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("home"), HomeComponent.class);

    public static final ComponentKey<PlayerSettingsComponent> PLAYER_SETTINGS =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("player_settings"), PlayerSettingsComponent.class);

    public static final ComponentKey<TimberTaskManagerComponent> TIMBER_TASKS =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("timber_tasks"), TimberTaskManagerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DEATH_LOCKS, (p) -> new DeathLocksComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(HOME, (p) -> new HomeComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PLAYER_SETTINGS, (p) -> new PlayerSettingsComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(Items.BUCKET, CARRYING_BUCKET, CarryingBucketItemComponent::new);
        registry.register(Items.ARROW, DEATH_KEY, DeathKeyItemComponent::new);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(TIMBER_TASKS, (w) -> new TimberTaskManagerComponent());
    }
}
