package nl.maikyperlee.advancedauth.objects;

public class AuthData {

    public long lastVerificationDate;

    public boolean verificated;

    public AuthData(long lastVerificationDate, boolean verificated){
        this.verificated = verificated;
        this.lastVerificationDate = lastVerificationDate;
    }
}
