package com.tgf.twf.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tgf.twf.TheWorldFactoryGame;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final Lwjgl3ApplicationConfiguration lwjgl3ApplicationConfiguration = new Lwjgl3ApplicationConfiguration();
        lwjgl3ApplicationConfiguration.setMaximized(true);

        new Lwjgl3Application(new TheWorldFactoryGame(), lwjgl3ApplicationConfiguration);
    }
}
