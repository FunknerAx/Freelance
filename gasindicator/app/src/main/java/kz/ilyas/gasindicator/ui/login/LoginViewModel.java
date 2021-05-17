package kz.ilyas.gasindicator.ui.login;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public String checkLoginData(String email, String password) {
        StringBuilder result = new StringBuilder();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result.append(0);
        } else {
            result.append(1);
        }

        if (!password.equals("")) {
            result.append(0);
        } else {
            result.append(1);
        }

        return result.toString();
    }

    public void authenticate(String email, String password) {
    }
}
