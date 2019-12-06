package com.tgf.twf.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.google.inject.Guice;
import com.tgf.twf.TheWorldFactoryGame;
import com.tgf.twf.config.WorldModule;
import com.tgf.twf.core.world.World;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final World world = Guice.createInjector(new WorldModule()).getInstance(World.class);

        final Lwjgl3ApplicationConfiguration lwjgl3ApplicationConfiguration = new Lwjgl3ApplicationConfiguration();
        lwjgl3ApplicationConfiguration.setMaximized(true);

        new Lwjgl3Application(new TheWorldFactoryGame(world), lwjgl3ApplicationConfiguration);
    }
}
