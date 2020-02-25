package application;

public class AccessToken {
    boolean homesafety;
    boolean homesecurity;
    boolean photosystem;
    boolean healthinfo;
    boolean compositelogic;
    boolean alarmcom;

    public AccessToken(boolean safety, boolean security, boolean photo, boolean health, boolean logic, boolean alarm){
        homesafety = safety;
        homesecurity = security;
        photosystem = photo;
        healthinfo = health;
        compositelogic = logic;
        alarmcom = alarm;

    }
}
