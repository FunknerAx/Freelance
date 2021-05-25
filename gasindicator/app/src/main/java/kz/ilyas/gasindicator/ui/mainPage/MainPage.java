package kz.ilyas.gasindicator.ui.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.ui.mainPage.indicator.RegisterIndicator;
import kz.ilyas.gasindicator.ui.mainPage.indicator.StaticIndicator;

public class MainPage extends AppCompatActivity {

    private Client client;
    private MainPageViewModel mainPageViewModel;
    private ArrayList<Indicator> indicators = new ArrayList<>();
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Bundle arguments = getIntent().getExtras();

        if (arguments.get("client") != null) {
            client = (Client) arguments.get("client");
            //Toast.makeText(this, client.getUID() + " " + client.getName(), Toast.LENGTH_SHORT).show();
        }

        add = (Button) findViewById(R.id.mainPage_fab);
        RecyclerView recyclerView = findViewById(R.id.mainPage_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainPage.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, indicators);
        recyclerView.setAdapter(indicatorAdapter);

        mainPageViewModel = ViewModelProviders.of(this).get(MainPageViewModel.class);

        mainPageViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<Indicator>>() {
            @Override
            public void onChanged(ArrayList<Indicator> indicatorsNew) {
                if (indicatorsNew != null && indicatorsNew.size() > 0) {
                    int index = -1;
                    for (int i = 0; i < indicators.size(); i++) {
                        if (indicators.get(i).getId().equals(indicatorsNew.get(0).getId()))
                            index = i;
                    }
                    if (index != -1) {
                        indicators.add(index, indicatorsNew.get(0));
                    } else {
                        indicators.add(new Indicator(indicatorsNew.get(0).getId(), indicatorsNew.get(0).getAddress(), indicatorsNew.get(0).getPosition(), indicatorsNew.get(0).getCurrentValue(), indicatorsNew.get(0).getCriticalValue(), null));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, RegisterIndicator.class).putExtra("client", client));
                finish();
            }
        });

        mainPageViewModel.subscribeOnIndicators(client);
    }

    public void getIndicatorStatic(View view) {
        ConstraintLayout parent = (ConstraintLayout) view.getParent();
        TextView id = parent.findViewById(R.id.gasItem_id_value);
        Toast.makeText(MainPage.this, id.getText().toString(), Toast.LENGTH_SHORT).show();
        Indicator indicator = null;
        for (Indicator cur : indicators) {
            if(cur.getId().equals(id.getText().toString()))
                indicator = cur;
        }
        startActivity(new Intent(MainPage.this, StaticIndicator.class).putExtra("indicator", indicator));
    }

  /*  private void setInitialData(){

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
    }*/
}