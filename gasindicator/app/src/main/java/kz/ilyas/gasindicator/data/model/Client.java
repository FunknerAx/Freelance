package kz.ilyas.gasindicator.data.model;

import java.io.Serializable;

public class Client implements Serializable {

    private String email;
    private String password;
    private String name;
    private String UID;

    public Client() {
    }

    public Client(String email, String password, String name, String UID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
