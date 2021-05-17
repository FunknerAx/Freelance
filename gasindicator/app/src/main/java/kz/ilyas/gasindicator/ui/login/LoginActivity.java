package kz.ilyas.gasindicator.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.ilyas.gasindicator.R;

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

        signIn.setOnClickListener(v -> {

            switch (loginViewModel.checkLoginData(email.getText().toString(), password.getText().toString())) {
                case "00": signIn(email.getText().toString(),password.getText().toString());
                    break;
                case "10":
                    Toast.makeText(LoginActivity.this,"Проверьте правильность ввода email",Toast.LENGTH_SHORT).show();
                    break;
                case "01":
                    Toast.makeText(LoginActivity.this,"Проверьте правильность ввода пароля",Toast.LENGTH_SHORT).show();
                    break;
                case "11":
                    Toast.makeText(LoginActivity.this,"Проверьте правильность ввода email и пароля",Toast.LENGTH_SHORT).show();
                    break;
            }

        });
    }

    private void signIn(String email, String password) {
        loginViewModel.authenticate(email,password);
    }
}