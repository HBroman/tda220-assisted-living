package application;
import application.AccessToken;

public class Account {

    private AccessToken token;
    public String username;
    public String verySecretpassword;


    public Account(String name, String password, AccessToken accesstoken ) {

        username = name;
        verySecretpassword = password;
        token = accesstoken;

    }
}
