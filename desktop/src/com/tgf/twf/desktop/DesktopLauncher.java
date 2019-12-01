package com.tgf.twf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.tgf.twf.TheWorldFactoryGame;
import com.tgf.twf.config.WorldModule;
import com.tgf.twf.core.world.World;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.forceExit = false;

        final World world = Guice.createInjector(new WorldModule()).getInstance(World.class);
        new LwjglApplication(new TheWorldFactoryGame(world), config);
    }
}
