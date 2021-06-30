package nl.maikyperlee.advancedauth;

import nl.maikyperlee.advancedauth.modules.discord.DiscordAuthLoader;
import nl.maikyperlee.advancedauth.modules.google.GoogleAuthLoader;
import nl.maikyperlee.advancedauth.modules.registerlogin.RegisterLoginAuthLoader;
import nl.maikyperlee.advancedauth.objects.enums.AuthenticationType;
import nl.maikyperlee.advancedauth.objects.interfaces.Authenticator;

public class AuthenticationHandler extends Authenticator {

    public AuthenticationHandler(AuthenticationType authenticationType) {
        super(authenticationType);

        if (authenticationType == AuthenticationType.GOOGLE)
            google();
        /**
         *         if (authenticationType == AuthenticationType.DISCORD)
         *             discord();
         *         if (authenticationType == AuthenticationType.REGISTERLOGIN)
         *             registerLogin();
         */

        AdvancedAuth.getPlugin().getLogger().info("[Authentication Handler] Initialized succesfully took " +
                ((System.currentTimeMillis() - AdvancedAuth.getPluginStartTime()) + "ms"));
        return;
    }

    @Override
    public void google() {
        GoogleAuthLoader.load(null);
    }

    @Override
    public void discord() {
        DiscordAuthLoader.load(null);
    }

    @Override
    public void registerLogin() {
        RegisterLoginAuthLoader.load(null);
    }

}
