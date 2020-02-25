package application;
import application.AccessToken;

public class Account {

    private AccessToken token;
    public String username;
    public String verysecretpassword;


    public Account(String name, String password, AccessToken accesstoken ) {

        username = name;
        verysecretpassword = password;
        token = accesstoken;

    }
}
