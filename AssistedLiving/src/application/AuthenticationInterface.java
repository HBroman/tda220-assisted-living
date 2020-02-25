package application;

public interface AuthenticationInterface {

    AccessToken authenticate (String name, String password);
}
