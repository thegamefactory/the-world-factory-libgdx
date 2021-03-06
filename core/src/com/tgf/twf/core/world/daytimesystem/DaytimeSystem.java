package com.tgf.twf.core.world.daytimesystem;

import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.rules.Rules;

public class DaytimeSystem implements System {
    private int ticksToday;

    @Override
    public void tick() {
        ticksToday++;
        if (ticksToday == Rules.TICKS_PER_DAY) {
            Entities.allComponents(Agent.class)
                    .forEach(agent -> agent.decreaseFood(Rules.AGENT_FOOD_EATEN_PER_DAY));
        }
        ticksToday = ticksToday % (Rules.TICKS_PER_DAY + Rules.TICKS_PER_NIGHT);
    }

    public boolean isDay() {
        return ticksToday < Rules.TICKS_PER_DAY;
    }

    public boolean isNight() {
        return ticksToday >= Rules.TICKS_PER_DAY;
    }
}
