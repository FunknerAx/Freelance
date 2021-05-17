package kz.ilyas.ambulancecall.ui.newCall;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Iterator;

import kz.ilyas.ambulancecall.data.NewCallDataSource;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.data.model.Symptoms;
import kz.ilyas.ambulancecall.data.model.Symtom;

public class NewCallViewModel extends ViewModel {

    MutableLiveData<ArrayList<String>> symptomLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> mySymptomsLiveData = new MutableLiveData<>();
    MutableLiveData<String> resultCallCreationLiveData = new MutableLiveData<>();

    NewCallDataSource newCallDataSource = new NewCallDataSource();

    private ArrayList<String> mySymptomsArr = new ArrayList<>();

    LiveData<ArrayList<String>> getSymptomsLiveData() {
        return symptomLiveData;
    }

    LiveData<String> getResultCallCreationLiveData() {
        return resultCallCreationLiveData;
    }

    //    public MutableLiveData<ArrayList<String>> getMySymptomsLiveData() {
//        return mySymptomsLiveData;
//    }
    LiveData<ArrayList<String>> getMySymptomsLiveData() {
        return mySymptomsLiveData;
    }

    public void getSymptomsFromDb() {
        newCallDataSource.getSymptoms(this);
    }

    public void updateSymptomsFromDb(Symptoms value) {

        ArrayList<String> symptomsArr = new ArrayList<>();
        Iterator<Symtom> it = value.getSymtoms().iterator();

        while (it.hasNext()) {
            symptomsArr.add(it.next().getName());
        }

        symptomLiveData.setValue(symptomsArr);
    }

    public void addSymptom(String symptom) {
        mySymptomsArr.add(symptom);
        mySymptomsLiveData.setValue(mySymptomsArr);
    }

    public void initCall(ClientProfile clientProfile, String address, double x, double y) {
        newCallDataSource.initCall(this, clientProfile, address, x, y, mySymptomsArr);
    }

    public void sendError(String s) {
        resultCallCreationLiveData.setValue(s);
    }

    public void callCreated() {
        resultCallCreationLiveData.setValue("OK");
    }
}
