package org.example.envirobaby.app;

import org.example.envirobaby.*;

public class UserExchanger {
    private static UserExchanger instance;
    public User instanceUser; //use singleton design pattern to send user object across classes.

    private UserExchanger(){
    }

    public static UserExchanger getInstance(){
        if (instance==null) {
            instance = new UserExchanger(); // force only one instance of class to exist at all times
        }
        return instance; //get the same instance every time
    }

    public void setInstanceUser(User instanceUser) {
        this.instanceUser = instanceUser;
    } // initialise the current user

    public User getInstanceUser() {
        return instanceUser;
    } // return the current user. always the same post initialisation
}
