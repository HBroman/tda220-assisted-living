package application;

import java.util.Arrays;
import java.util.List;
import application.Account;

public class Authentication implements AuthenticationInterface{

    AccessToken intruder = new AccessToken(false,false,false,false,false,false);
    AccessToken friend = new AccessToken(false,true,true,false,false,true);
    Account intruderAccount = new Account("Intruder","1234",intruder);
    Account friendAccount = new Account("Friend","5678",friend);

    List<Account> database = Arrays.asList(intruderAccount, friendAccount);

    boolean login = false;

    public AccessToken authenticate(String username, String password){
        for (Account element : database) {
            if(element.username.equals(username) && element.verysecretpassword.equals(password)){
                System.out.println("Login successful");
                login = true;
                return element.token;
            }
        }
        if(!login){
            System.out.println("Login failed");
        }
        return new AccessToken(false, false, false, false, false, false);
    }
}
