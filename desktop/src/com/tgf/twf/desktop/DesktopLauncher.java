package com.tgf.twf.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.google.inject.Guice;
import com.tgf.twf.TheWorldFactoryGame;
import com.tgf.twf.config.WorldModule;
import com.tgf.twf.core.world.World;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // TODO: fullscreen from command line args
        // config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        final World world = Guice.createInjector(new WorldModule()).getInstance(World.class);
        new Lwjgl3Application(new TheWorldFactoryGame(world), config);
    }
}
