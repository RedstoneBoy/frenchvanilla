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
import net.bios.frenchvanilla.key_binds.PlayerBindDataComponent;
import net.bios.frenchvanilla.player_config.PlayerConfigComponent;
import net.bios.frenchvanilla.player_config.TeleportComponent;
import net.bios.frenchvanilla.sanity.PlayerSanityComponent;
import net.bios.frenchvanilla.tasks.FrenchTaskManagerComponent;
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

    public static final ComponentKey<PlayerBindDataComponent> PLAYER_BIND_DATA =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("player_bind_data"), PlayerBindDataComponent.class);

    public static final ComponentKey<PlayerConfigComponent> PLAYER_CONFIG =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("player_config"), PlayerConfigComponent.class);

    public static final ComponentKey<PlayerSanityComponent> PLAYER_SANITY =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("player_sanity"), PlayerSanityComponent.class);

    public static final ComponentKey<FrenchTaskManagerComponent> TASKS =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("tasks"), FrenchTaskManagerComponent.class);

    public static final ComponentKey<TeleportComponent> TELEPORT =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("teleport"), TeleportComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DEATH_LOCKS, (p) -> new DeathLocksComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(HOME, (p) -> new HomeComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PLAYER_BIND_DATA, (p) -> new PlayerBindDataComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(PLAYER_CONFIG, (p) -> new PlayerConfigComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PLAYER_SANITY, (p) -> new PlayerSanityComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(TELEPORT, (p) -> new TeleportComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(Items.BUCKET, CARRYING_BUCKET, CarryingBucketItemComponent::new);
        registry.register(Items.ARROW, DEATH_KEY, DeathKeyItemComponent::new);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(TASKS, (w) -> new FrenchTaskManagerComponent());
    }
}
