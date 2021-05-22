package kz.ilyas.gasindicator.ui.mainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.data.model.Indicator;

public class MainPage extends AppCompatActivity {

    private Client client;
    private ArrayList<Indicator> indicators = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Bundle arguments = getIntent().getExtras();

        if (arguments.get("client") != null){
            client = (Client) arguments.get("client");
            Toast.makeText(this,client.getUID()+" "+ client.getName(),Toast.LENGTH_SHORT).show();
        }

        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.mainPage_recyclerView);
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, indicators);
        recyclerView.setAdapter(indicatorAdapter);

    }

    private void setInitialData(){

        indicators.add(new Indicator ("1", "Байтурсынова 144", "Кухня","3.0", "5.5"));
        indicators.add(new Indicator ("2", "Ауэзова 14","Кухня","2.3", "5.5"));
        indicators.add(new Indicator ("3", "Курмангазы 56", "Спальня", "3.3", "5.5"));
        indicators.add(new Indicator ("1", "Байтурсынова 144", "Кухня","3.0", "5.5"));
        indicators.add(new Indicator ("2", "Ауэзова 14","Кухня","2.3", "5.5"));
        indicators.add(new Indicator ("3", "Курмангазы 56", "Спальня", "3.3", "5.5"));
        indicators.add(new Indicator ("1", "Байтурсынова 144", "Кухня","3.0", "5.5"));
        indicators.add(new Indicator ("2", "Ауэзова 14","Кухня","2.3", "5.5"));
        indicators.add(new Indicator ("3", "Курмангазы 56", "Спальня", "3.3", "5.5"));
        indicators.add(new Indicator ("1", "Байтурсынова 144", "Кухня","3.0", "5.5"));
        indicators.add(new Indicator ("2", "Ауэзова 14","Кухня","2.3", "5.5"));
        indicators.add(new Indicator ("3", "Курмангазы 56", "Спальня", "3.3", "5.5"));
    }
}