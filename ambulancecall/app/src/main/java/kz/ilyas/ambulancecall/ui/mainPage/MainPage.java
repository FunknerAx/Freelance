package kz.ilyas.ambulancecall.ui.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.CallStructure;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.data.model.ItemOfList;
import kz.ilyas.ambulancecall.ui.newCall.NewCall;

public class MainPage extends AppCompatActivity {

    MainPageViewModel mainPageViewModel;
    ArrayList<ItemOfList> items = new ArrayList<>();
    ListView itemListView;
    ClientProfile profile = null;
    AmbulanceProfile ambulanceProfile = null;
    private boolean isFirst = false;
    private boolean isClient = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Bundle arguments = getIntent().getExtras();
        String uid = arguments.getString("uid");


        TextView displayName = findViewById(R.id.displayName_textView_MP);
        TextView sex = findViewById(R.id.sex_textView_MP);
        TextView weight = findViewById(R.id.weight_textView_MP);
        TextView invalid = findViewById(R.id.invalid_textView_MP);
        ProgressBar progressBar = findViewById(R.id.progressBar_MP);
        Button newCall = findViewById(R.id.new_call_button_MP);

        itemListView = findViewById(R.id.items_listView_MP);
        ItemListAdapter stateAdapter = new ItemListAdapter(this, R.layout.item_list_layout, items);
        itemListView.setAdapter(stateAdapter);

        mainPageViewModel = ViewModelProviders.of(this).get(MainPageViewModel.class);

        mainPageViewModel.getClientProfile().observe(this, new Observer<ClientProfile>() {
            @Override
            public void onChanged(ClientProfile clientProfile) {
                if (clientProfile != null) {
                    isClient = true;
                    profile = clientProfile;
                    displayName.setText("ФИО: " + clientProfile.getDisplayName());
                    sex.setText("Пол: " + clientProfile.getSex());
                    weight.setText("Вес: " + clientProfile.getWeight() + " кг");
                    invalid.setText("Инвалидность: " + clientProfile.getInvalid());

                    progressBar.setVisibility(View.GONE);
                    mainPageViewModel.subscribeOnCalls(profile, null, isFirst);
                }

            }
        });

        mainPageViewModel.getAmbulanceProfile().observe(this, new Observer<AmbulanceProfile>() {
            @Override
            public void onChanged(AmbulanceProfile ambulanceProfile) {
                if (ambulanceProfile != null) {
                    isClient = false;
                    newCall.setVisibility(View.GONE);
                    displayName.setText("Наименование органзиации: " + ambulanceProfile.getName());
                    sex.setText("Адресс: " + ambulanceProfile.getAddress());

                    progressBar.setVisibility(View.GONE);
                    mainPageViewModel.subscribeOnCalls(null, ambulanceProfile, isFirst);
                }

            }
        });

        mainPageViewModel.getCallStructure().observe(this, new Observer<ArrayList<CallStructure>>() {
            @Override
            public void onChanged(ArrayList<CallStructure> callStructures) {
                if (callStructures.size() > 0) {
                    if (callStructures.get(0).getStatus().equals("Выполнено") || callStructures.get(0).getStatus().equals("Отмененно") || callStructures.get(0).getStatus().equals("Завершенно")) {
                        if(isClient)
                            newCall.setVisibility(View.VISIBLE);
                    } else {
                        newCall.setVisibility(View.GONE);
                    }
                    int index = -1;
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getId().equals(callStructures.get(0).getUid()))
                            index = i;
                    }
                    if (index != -1) {
                        items.get(index).setState(callStructures.get(0).getStatus());
                    } else {
                        items.add(new ItemOfList(callStructures.get(0).getUid(), callStructures.get(0).getClientProfile().getDisplayName(), callStructures.get(0).getAddress(), String.valueOf(callStructures.get(0).getCategory()),callStructures.get(0).getStatus(), isClient, callStructures.get(0).getClientProfile(),callStructures.get(0).getSymptoms()));
                    }

                    ((BaseAdapter) itemListView.getAdapter()).notifyDataSetChanged();

                }
            }
        });

        mainPageViewModel.getUpdatedStatusLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(MainPage.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (uid != null) {
            progressBar.setVisibility(View.VISIBLE);
            mainPageViewModel.getData(arguments.getString("uid"));

        }
        if (arguments.getSerializable("clientProfile") != null) {
            isClient = true;
            profile = (ClientProfile) arguments.getSerializable("clientProfile");

            displayName.setText(profile.getDisplayName());
            sex.setText("Пол: " + profile.getSex());
            weight.setText("Вес: " + profile.getWeight() + " кг");
            invalid.setText("Инвалидность: " + profile.getInvalid());
            mainPageViewModel.subscribeOnCalls(profile, null, isFirst);
        }
        if (arguments.getSerializable("ambulanceProfile") != null) {
            isClient = false;
            newCall.setVisibility(View.GONE);
            ambulanceProfile = (AmbulanceProfile) arguments.getSerializable("ambulanceProfile");
            displayName.setText(ambulanceProfile.getName());
            sex.setText(ambulanceProfile.getAddress());
            mainPageViewModel.subscribeOnCalls(null, ambulanceProfile, isFirst);
        }


        newCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callPage = new Intent(MainPage.this, NewCall.class);
                callPage.putExtra("clientProfile", profile);
                startActivity(callPage);
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemOfList cur = (ItemOfList) parent.getItemAtPosition(position);


                findViewById(R.id.button_complete_ILL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainPage.this, "Click", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void updateCallStatus(View view) {
        ConstraintLayout parent = (ConstraintLayout) view.getParent();
        TextView uid = parent.findViewById(R.id.item_list_id);
        TextView button = parent.findViewById(R.id.button_complete_ILL);
        if (isClient) {
            mainPageViewModel.updateState(uid.getText().toString(), "Отмененно");
        } else {
            if (button.getText().toString().equals("принять")) {
                mainPageViewModel.updateState(uid.getText().toString(), "В работе");
            } else {
                mainPageViewModel.updateState(uid.getText().toString(), "Завершенно");

            }
        }
    }
}
