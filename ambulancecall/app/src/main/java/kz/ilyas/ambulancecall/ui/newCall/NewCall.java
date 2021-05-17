package kz.ilyas.ambulancecall.ui.newCall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.ui.mainPage.MainPage;
import kz.ilyas.ambulancecall.ui.register.MapsActivity;

public class NewCall extends AppCompatActivity {


    private NewCallViewModel newCallViewModel;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterSymptoms;

    private ArrayList<String> listForSymptoms = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private ClientProfile clientProfile;
    private TextView address;
    private double xCoordinate;
    private double yCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_call);

        Bundle arguments = getIntent().getExtras();
        clientProfile = (ClientProfile) arguments.getSerializable("clientProfile");
        newCallViewModel = new NewCallViewModel();

        ListView listView = findViewById(R.id.symptoms_search_list_view);
        ListView listViewSymptoms = findViewById(R.id.symptoms_list_view);
        SearchView searchView = findViewById(R.id.search_symptom_NC);
        Button callButton = findViewById(R.id.init_call_button_NC);
        Button addAddress = findViewById(R.id.add_address_register_NC);
        address = findViewById(R.id.call_address_editText_NC);

        newCallViewModel = ViewModelProviders.of(this).get(NewCallViewModel.class);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        adapterSymptoms = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listForSymptoms);

        listView.setAdapter(adapter);
        listViewSymptoms.setAdapter(adapterSymptoms);

        newCallViewModel.getSymptomsLiveData().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> arrayList) {
                if (arrayList != null) {
                    list.clear();
                    list.addAll(arrayList);
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                }

            }
        });

        newCallViewModel.getMySymptomsLiveData().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                listForSymptoms.clear();
                listForSymptoms.addAll(strings);
                searchView.clearFocus();
                ((BaseAdapter) listViewSymptoms.getAdapter()).notifyDataSetChanged();
                listViewSymptoms.setVisibility(View.VISIBLE);
            }
        });

        newCallViewModel.getResultCallCreationLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("OK")) {
                    Toast.makeText(NewCall.this, "Вызов создан\nОжидайте", Toast.LENGTH_SHORT).show();
                    Intent intoMain = new Intent(NewCall.this, MainPage.class);
                    intoMain.putExtra("clientProfile",clientProfile);
                    startActivity(intoMain);
                    finish();
                } else {
                    Toast.makeText(NewCall.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    listViewSymptoms.setVisibility(View.INVISIBLE);
                    adapter.getFilter().filter(newText);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                    listViewSymptoms.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newCallViewModel.addSymptom((String) parent.getItemAtPosition(position));
                searchView.setQueryHint("Симптомы");
                searchView.clearFocus();
                listView.setVisibility(View.GONE);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCallViewModel.initCall(clientProfile, address.getText().toString(), xCoordinate, yCoordinate);
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getAddress = new Intent(NewCall.this, MapsActivity.class);
                startActivityForResult(getAddress, 2);
            }
        });
        newCallViewModel.getSymptomsFromDb();

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