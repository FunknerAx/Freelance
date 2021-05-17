package kz.ilyas.ambulancecall.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.ui.mainPage.MainPage;

public class RegisterAmbulanceActivity extends AppCompatActivity {
    private RegisterViewModel rvm;
//    private TextView address;
    private EditText address;
    private double xCoordinate, yCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_register);

        rvm = ViewModelProviders.of(this).get(RegisterViewModel.class);

        ProgressBar progressBar = findViewById(R.id.ambulance_progressBar_RA);

        rvm.getAmbulanceProfile().observe(this, new Observer<AmbulanceProfile>() {
            @Override
            public void onChanged(AmbulanceProfile ambulanceProfile) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(RegisterAmbulanceActivity.this, "Добро пожаловать!\n" + ambulanceProfile.getName(), Toast.LENGTH_SHORT).show();

                Intent mainPage = new Intent(RegisterAmbulanceActivity.this, MainPage.class);
                mainPage.putExtra("ambulanceProfile", ambulanceProfile);
                startActivity(mainPage);
                finish();
            }
        });

        TextView name = findViewById(R.id.ambulance_name_editText_RA);
        address = findViewById(R.id.ambulance_address_editText_RA);
        TextView email = findViewById(R.id.ambulance_email_editText);
        TextView password = findViewById(R.id.ambulance_password_editText);
        Button register = findViewById(R.id.ambulance_register_button_RA);
        Button changeOnClientButton = findViewById(R.id.client_registerChange_button_RA);
        Button openMapButton = findViewById(R.id.add_address_register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString() != "" &&
                        address.getText().toString() != "" &&
                        email.getText().toString() != "" &&
                        password.getText().toString() != "") {
                    progressBar.setVisibility(View.VISIBLE);
                    rvm.registerAmbulance("true" ,name.getText().toString(), address.getText().toString(), xCoordinate, yCoordinate, email.getText().toString(), password.getText().toString());

                } else {
                    Toast.makeText(RegisterAmbulanceActivity.this, "Проверьте заполненность полей", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeOnClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeClientRegisterIntent = new Intent(RegisterAmbulanceActivity.this, RegisterActivity.class);
                startActivity(changeClientRegisterIntent);
                finish();
            }
        });

        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addAddress = new Intent(RegisterAmbulanceActivity.this, MapsActivity.class);
                startActivityForResult(addAddress, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        address.setText(data.getStringExtra("location"));
        xCoordinate = data.getDoubleExtra("xCoordinate", 0);
        yCoordinate = data.getDoubleExtra("yCoordinate", 0);
    }
}