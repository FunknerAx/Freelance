package kz.ilyas.gasindicator.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.ui.login.LoginViewModel;
import kz.ilyas.gasindicator.ui.register.RegisterViewModel;

public class DataBaseSource {
    private RegisterViewModel registerViewModel;
    private LoginViewModel loginViewModel;
    protected DatabaseReference db;
    protected FirebaseAuth mAuth;

    public DataBaseSource(RegisterViewModel registerViewModel) {
        this();
        this.registerViewModel = registerViewModel;

    }

    public DataBaseSource() {
        this.db = FirebaseDatabase.getInstance("https://ambulancecall-18638-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

    public DataBaseSource(LoginViewModel loginViewModel) {
        this();
        this.loginViewModel = loginViewModel;
    }


    public void addNewUser(Client client) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(client.getName())
                                    .build();

                            mAuth.getCurrentUser().updateProfile(update)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            registerViewModel.sendResult(mAuth, null);
                                        }
                                    });
                        } else {
                            registerViewModel.sendResult(mAuth, task.getException());
                        }
                    }
                });

    }

    public void signIn(String email, String password) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginViewModel.sendResult(mAuth, null);
                        } else {
                            loginViewModel.sendResult(mAuth, task.getException());
                        }
                    }
                });
    }
}
