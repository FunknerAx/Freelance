package kz.ilyas.gasindicator.ui.register;

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

public class Register extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText name;
    private Button registerButton;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        email = findViewById(R.id.register_email_editText);
        password = findViewById(R.id.register_password_editText);
        registerButton = findViewById(R.id.register_register_button);
        name = findViewById(R.id.register_clientName_editText);

        registerViewModel.getFirebaseAuthLiveData().observe(this, new Observer<FirebaseAuth>() {
            @Override
            public void onChanged(FirebaseAuth mAuthIn) {
                if (mAuthIn != null) {
                    Toast.makeText(Register.this, mAuthIn.getUid(), Toast.LENGTH_SHORT).show();
                    Intent mainPage = new Intent(Register.this, MainPage.class);
                    mainPage.putExtra("client", new Client(mAuthIn.getCurrentUser().getEmail(),null,mAuthIn.getCurrentUser().getDisplayName(),mAuthIn.getCurrentUser().getUid()));
                    startActivity(mainPage);
                }
            }
        });

        registerViewModel.getExceptionMutableLiveData().observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                if (e != null) {
                    Toast.makeText(Register.this, "Ошибка\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(v -> {

            switch (registerViewModel.checkInputData(email.getText().toString(), password.getText().toString())) {
                case "00":
                    if (name.getText().toString().equals("")) {
                        Toast.makeText(Register.this, "Заполните имя пользователя", Toast.LENGTH_SHORT).show();
                    } else {
                        register(email.getText().toString(), password.getText().toString(), name.getText().toString());
                    }
                    break;
                case "10":
                    Toast.makeText(Register.this, "Проверьте правильность ввода email", Toast.LENGTH_SHORT).show();
                    break;
                case "01":
                    Toast.makeText(Register.this, "Проверьте правильность ввода пароля", Toast.LENGTH_SHORT).show();
                    break;
                case "11":
                    Toast.makeText(Register.this, "Проверьте правильность ввода email и пароля", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void register(String email, String password, String name) {
        registerViewModel.registerNewClient(new Client(email, password, name, null));
    }
}