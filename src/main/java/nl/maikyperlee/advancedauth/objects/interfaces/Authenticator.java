package nl.maikyperlee.advancedauth.objects.interfaces;

import nl.maikyperlee.advancedauth.objects.enums.AuthenticationType;

public abstract class Authenticator {

    private AuthenticationType authenticationType;

    public Authenticator(AuthenticationType authenticationType){
        this.authenticationType = authenticationType;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public abstract void google();
    public abstract void discord();
    public abstract void registerLogin();

}
