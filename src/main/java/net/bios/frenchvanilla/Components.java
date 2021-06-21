package net.bios.frenchvanilla;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.bios.frenchvanilla.deathlock.DeathKeyItemComponent;
import net.bios.frenchvanilla.deathlock.DeathLocksComponent;
import net.bios.frenchvanilla.home.HomeComponent;
import net.minecraft.item.Items;

public class Components implements EntityComponentInitializer, ItemComponentInitializer {
    public static final ComponentKey<DeathLocksComponent> DEATH_LOCKS =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("deathlocks"), DeathLocksComponent.class);

    public static final ComponentKey<DeathKeyItemComponent> DEATH_KEY =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("deathkey"), DeathKeyItemComponent.class);

    public static final ComponentKey<HomeComponent> HOME =
            ComponentRegistry.getOrCreate(FrenchVanilla.identifier("home"), HomeComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DEATH_LOCKS, (p) -> new DeathLocksComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(HOME, (p) -> new HomeComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(Items.ARROW, DEATH_KEY, DeathKeyItemComponent::new);
    }
}
