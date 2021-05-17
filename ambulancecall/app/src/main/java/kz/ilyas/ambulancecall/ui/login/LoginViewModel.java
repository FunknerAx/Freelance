package kz.ilyas.ambulancecall.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.LoginRepository;
import kz.ilyas.ambulancecall.data.Result;
import kz.ilyas.ambulancecall.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(LoginActivity loginActivity, String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(this, loginActivity, username, password);
    }

    public void sendLoginResult(Result<LoggedInUser> user, String code) {
        if (user instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) user).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }

    }

}