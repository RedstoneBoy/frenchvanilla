package net.bios.frenchvanilla.sanity;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static net.bios.frenchvanilla.Components.PLAYER_SANITY;

public class OnKill implements ServerEntityCombatEvents.AfterKilledOtherEntity {
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new OnKill());
    }

    private static final double INNOCENT_PENALTY = 0.005;
    private static final double MONSTER_BONUS = 0.005;
    private static final double DRAGON_BONUS = 1.0;
    private static final double WITHER_BONUS = 0.2;

    private static final Class<?>[] INNOCENT_ENTITIES = new Class<?>[]{
            AxolotlEntity.class,
            BatEntity.class,
            CatEntity.class,
            ChickenEntity.class,
            CowEntity.class,
            FishEntity.class,
            FoxEntity.class,
            AbstractHorseEntity.class,
            IronGolemEntity.class,
            OcelotEntity.class,
            ParrotEntity.class,
            PigEntity.class,
            RabbitEntity.class,
            SheepEntity.class,
            SnowGolemEntity.class,
            SquidEntity.class,
            StriderEntity.class,
            TurtleEntity.class,
            VillagerEntity.class,
            WolfEntity.class
    };

    private static final Class<?>[] MONSTER_ENTITIES = new Class<?>[]{
            HostileEntity.class
    };

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (!FrenchVanilla.config.sanity.value) return;

        if (entity instanceof ServerPlayerEntity p) {
            PlayerSanityComponent sanity = PLAYER_SANITY.get(p);

            for (Class<?> innocent : INNOCENT_ENTITIES) {
                if (innocent.isAssignableFrom(killedEntity.getClass())) {
                    sanity.removeSanity(INNOCENT_PENALTY);
                    return;
                }
            }

            for (Class<?> monster : MONSTER_ENTITIES) {
                if (monster.isAssignableFrom(killedEntity.getClass())) {
                    sanity.addSanity(MONSTER_BONUS);
                    return;
                }
            }

            if (killedEntity instanceof EnderDragonEntity) {
                sanity.addSanity(DRAGON_BONUS);
                return;
            }

            if (killedEntity instanceof WitherEntity) {
                sanity.addSanity(WITHER_BONUS);
            }
        }
    }
}
