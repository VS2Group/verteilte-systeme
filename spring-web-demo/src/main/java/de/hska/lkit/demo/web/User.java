package de.hska.lkit.demo.web;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
public class User {

    public User (long id, String username, String password) {

        this.id=id;
        this.username=username;
        this.password = password;

    }

    public User () {}




    private long id;
    private String username;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
