package kz.ilyas.ambulancecall.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.Serializable;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.ui.mainPage.MainPage;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        final Button buttonReg = findViewById(R.id.register_button_RA);
        final TextView surname = findViewById(R.id.surname_editText);
        final TextView name = findViewById(R.id.name_editText);
        final TextView patronymic = findViewById(R.id.patronymic_editText);
        final RadioButton sexFemale = findViewById(R.id.sexFemale_button);
        final RadioButton sexMale = findViewById(R.id.sexMale_button);
        final TextView birthDate = findViewById(R.id.birthDate_editText);
        final TextView weight = findViewById(R.id.weight_editText);
        final TextView invalid = findViewById(R.id.invalid_editText);
        final TextView email = findViewById(R.id.email_editText);
        final TextView password = findViewById(R.id.password_editText);
        final ProgressBar progressBar = findViewById(R.id.progressBar_RA);
        final Button ambulanceRegisterButton = findViewById(R.id.ambulance_register_button_RA);

        registerViewModel.getClientProfile().observe(this, new Observer<ClientProfile>() {
            @Override
            public void onChanged(ClientProfile clientProfile) {
                if(clientProfile != null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this ,"Добро пожаловать!\n" + clientProfile.getDisplayName(), Toast.LENGTH_SHORT).show();

                    Intent mainPage = new Intent(RegisterActivity.this, MainPage.class);
                    mainPage.putExtra("clientProfile", clientProfile);
                    startActivity(mainPage);
                   // RegisterActivity.this.onDestroy();
                }
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (    surname.getText().toString() != "" &&
                        name.getText().toString() != "" &&
                        patronymic.getText().toString() != "" &&
                        (sexFemale.isChecked() || sexMale.isChecked()) &&
                        birthDate.getText().toString() != "" &&
                        weight.getText().toString() != "" &&
                        email.getText().toString() != "" &&
                        password.getText().toString() != ""
                ) {
                    progressBar.setVisibility(View.VISIBLE);
                    registerViewModel.sendClientProfile(surname.getText().toString(),
                                                        name.getText().toString(),
                                                        patronymic.getText().toString(),
                                                        sexFemale.isChecked()? "женский": "мужской",
                                                        birthDate.getText().toString(),
                                                        weight.getText().toString(),
                                                        email.getText().toString(),
                                                        password.getText().toString(),
                                                        invalid.getText().toString());


                } else {
                    Toast.makeText(RegisterActivity.this,"Проверьте заполненность полей",Toast.LENGTH_SHORT).show();
                }


            }
        });

        ambulanceRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ambulanceRegisterIntent = new Intent(RegisterActivity.this, RegisterAmbulanceActivity.class);
                startActivity(ambulanceRegisterIntent);
            }
        });
    }
}