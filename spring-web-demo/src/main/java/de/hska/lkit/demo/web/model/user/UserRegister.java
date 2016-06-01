package de.hska.lkit.demo.web.model.user;

/**
 * Created by Tobias Kerst on 31.05.2016.
 */
public class UserRegister extends User {
    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    private String passwordRepeat;
}
