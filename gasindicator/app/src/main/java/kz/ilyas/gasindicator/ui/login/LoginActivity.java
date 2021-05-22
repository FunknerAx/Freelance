package kz.ilyas.gasindicator.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.ui.mainPage.MainPage;
import kz.ilyas.gasindicator.ui.register.Register;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signIn;
    private Button register;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        email = findViewById(R.id.login_email_editText);
        password = findViewById(R.id.login_password_editText);
        signIn = findViewById(R.id.login_signIn_button);
        register = findViewById(R.id.login_register_button);

        loginViewModel.getFirebaseAuthLiveData().observe(this, new Observer<FirebaseAuth>() {
            @Override
            public void onChanged(FirebaseAuth mAuthIn) {
                if (mAuthIn != null) {
                    Intent mainPage = new Intent(LoginActivity.this, MainPage.class);
                    mainPage.putExtra("client", new Client(mAuthIn.getCurrentUser().getEmail(),null,mAuthIn.getCurrentUser().getDisplayName(),mAuthIn.getCurrentUser().getUid()));
                    startActivity(mainPage);
                }
            }
        });

        loginViewModel.getExceptionMutableLiveData().observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                if (e != null)
                    Toast.makeText(LoginActivity.this, "Ошибка\n" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        signIn.setOnClickListener(v -> {

            switch (loginViewModel.checkLoginData(email.getText().toString(), password.getText().toString())) {
                case "00":
                    signIn(email.getText().toString(), password.getText().toString());
                    break;
                case "10":
                    Toast.makeText(LoginActivity.this, "Проверьте правильность ввода email", Toast.LENGTH_SHORT).show();
                    break;
                case "01":
                    Toast.makeText(LoginActivity.this, "Проверьте правильность ввода пароля", Toast.LENGTH_SHORT).show();
                    break;
                case "11":
                    Toast.makeText(LoginActivity.this, "Проверьте правильность ввода email и пароля", Toast.LENGTH_SHORT).show();
                    break;
            }

        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(this, Register.class));
        });
    }

    private void signIn(String email, String password) {
        loginViewModel.authenticate(email, password);
    }
}