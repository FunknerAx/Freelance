package kz.ilyas.gasindicator.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import kz.ilyas.gasindicator.data.DataBaseSource;

public class LoginViewModel extends ViewModel {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private DataBaseSource db = new DataBaseSource(this);

    MutableLiveData<FirebaseAuth> firebaseAuthMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Exception> exceptionMutableLiveData = new MutableLiveData<>();

    LiveData<FirebaseAuth> getFirebaseAuthLiveData() {
        return firebaseAuthMutableLiveData;
    }

    LiveData<Exception> getExceptionMutableLiveData() {
        return exceptionMutableLiveData;
    }

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
        db.signIn(email, password);

    }

    public void sendResult(FirebaseAuth mAuth, Exception exception) {
        if(exception!= null){
            exceptionMutableLiveData.setValue(exception);
        } else {
            firebaseAuthMutableLiveData.setValue(mAuth);
        }
    }
}
