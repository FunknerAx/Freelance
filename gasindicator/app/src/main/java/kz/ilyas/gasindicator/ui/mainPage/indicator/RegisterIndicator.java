package kz.ilyas.gasindicator.ui.mainPage.indicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Result;
import kz.ilyas.gasindicator.ui.mainPage.MainPage;

public class RegisterIndicator extends AppCompatActivity {

    private EditText id, address, position, criticalValue, password, idExists, passwordExists;
    private Button register, add;
    private RegisterIndicatorViewModel registerIndicatorViewModel;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_indicator);

        Bundle arguments = getIntent().getExtras();

        if ((Client) arguments.get("client") != null)
            client = (Client) arguments.get("client");

        id = (EditText) findViewById(R.id.registerIndicator_idIndicator_editText);
        address = (EditText) findViewById(R.id.registerIndicator_addressIndicator_editText);
        position = (EditText) findViewById(R.id.registerIndicator_positionIndicator_editText);
        criticalValue = (EditText) findViewById(R.id.registerIndicator_criticalValueIndicator_editText);
        password = (EditText) findViewById(R.id.registerIndicator_passwordIndicator_editText);
        idExists = (EditText) findViewById(R.id.registerIndicator_idExistsIndicator_editText);
        passwordExists = (EditText) findViewById(R.id.registerIndicator_passwordExistsIndicator_editText);

        register = (Button) findViewById(R.id.registerIndicator_register_button);
        add = (Button) findViewById(R.id.registerIndicator_addExist_button);

        registerIndicatorViewModel = ViewModelProviders.of(this).get(RegisterIndicatorViewModel.class);

        registerIndicatorViewModel.getResultLiveData().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result != null && result.getStatus().equals("OK")) {
                    Toast.makeText(RegisterIndicator.this, "Устройство добавлено", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterIndicator.this, MainPage.class).putExtra("client", client));
                    finish();
                } else {
                    if (result.getException() != null){
                        Toast.makeText(RegisterIndicator.this, result.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterIndicator.this, result.getStatus(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!registerIndicatorViewModel.checkRegisterData(id, address, position, criticalValue, password)) {
                    registerIndicatorViewModel.registerNewIndicator(client.getUID(), new Indicator(id.getText().toString(),
                            address.getText().toString(),
                            position.getText().toString(),
                            "0",
                            criticalValue.getText().toString(),
                            password.getText().toString()));
                } else {
                    Toast.makeText(RegisterIndicator.this, "Проверьте заполненность данных для регистрации", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!registerIndicatorViewModel.checkRegisterData(idExists, passwordExists)) {
                    registerIndicatorViewModel.addExists(client.getUID(), idExists.getText().toString(), passwordExists.getText().toString());
                } else {
                    Toast.makeText(RegisterIndicator.this, "Проверьте заполненность данных для добавления", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}