package kz.ilyas.ambulancecall.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.ilyas.ambulancecall.data.model.LoggedInUser;
import kz.ilyas.ambulancecall.ui.login.LoginActivity;
import kz.ilyas.ambulancecall.ui.login.LoginViewModel;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;

    public void login(LoginRepository loginRepository, LoginViewModel loginViewModel, LoginActivity loginActivity, String username, String password) {
        try {
            // TODO: handle loggedInUser authentication

            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "sign in:success");
                                loginViewModel.sendLoginResult(new Result.Success<>(new LoggedInUser(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid())),"OK");
                                loginRepository.setLoggedInUser(new LoggedInUser(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid()));

                            } else {
                                // If sign in fails, display a message to the user.
                                loginViewModel.sendLoginResult(new Result.Error(new Exception("Ошибка авторизации")),"ERROR");
                                Log.w(TAG, "sign in:failure", task.getException());
                            }
                        }
                    });

        } catch (Exception e) {
            loginViewModel.sendLoginResult(new Result.Error(new Exception("Ошибка авторизации")),"ERROR");
            Log.w(TAG, "sign in:failure", e.getCause());
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}