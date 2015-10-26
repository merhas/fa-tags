package fr.xorus.software.fa;

/**
 * Created by Xorus on 26/10/2015.
 */
public class LoginError extends Exception {
    public LoginError(String error) {
        super(error);
    }
}
