package com.tgf.twf.core.world.daytimesystem;

import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.rules.Rules;

public class Daytime implements System {
    private int ticksToday;

    private Daytime() {

    }

    public static Daytime INSTANCE = new Daytime();

    @Override
    public void tick() {
        ticksToday++;
        ticksToday = ticksToday % (Rules.TICKS_PER_DAY + Rules.TICKS_PER_NIGHT);
    }

    public boolean isDay() {
        return ticksToday < Rules.TICKS_PER_DAY;
    }

    public boolean isNight() {
        return ticksToday >= Rules.TICKS_PER_DAY;
    }
}
