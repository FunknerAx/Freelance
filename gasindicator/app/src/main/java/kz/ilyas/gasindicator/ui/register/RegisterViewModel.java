package kz.ilyas.gasindicator.ui.register;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import kz.ilyas.gasindicator.data.DataBaseSource;
import kz.ilyas.gasindicator.data.model.Client;

public class RegisterViewModel extends ViewModel {

    private DataBaseSource db = new DataBaseSource(this);

    MutableLiveData<FirebaseAuth> firebaseAuthMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Exception> exceptionMutableLiveData = new MutableLiveData<>();

    LiveData<FirebaseAuth> getFirebaseAuthLiveData() {
        return firebaseAuthMutableLiveData;
    }

    LiveData<Exception> getExceptionMutableLiveData() {
        return exceptionMutableLiveData;
    }

    public String checkInputData(String email, String password) {
        StringBuilder result = new StringBuilder();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result.append(0);
        } else {
            result.append(1);
        }

        if (password.length() > 6) {
            result.append(0);
        } else {
            result.append(1);
        }

        return result.toString();
    }

    public void registerNewClient(Client client) {
        db.addNewUser(client);
    }

    public void sendResult(FirebaseAuth mAuth, Exception exception) {
        if (exception != null) {
            exceptionMutableLiveData.setValue(exception);
        } else {
            firebaseAuthMutableLiveData.setValue(mAuth);
        }
    }
}
