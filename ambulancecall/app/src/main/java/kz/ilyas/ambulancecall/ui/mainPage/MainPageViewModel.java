package kz.ilyas.ambulancecall.ui.mainPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import kz.ilyas.ambulancecall.data.DataBaseSource;
import kz.ilyas.ambulancecall.data.ProfileDataSource;
import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.CallStructure;
import kz.ilyas.ambulancecall.data.model.ClientProfile;

public class MainPageViewModel extends ViewModel {

    private DataBaseSource db = new DataBaseSource();

    private ProfileDataSource pds = new ProfileDataSource();

    private MutableLiveData<ClientProfile> clientProfileLiveData = new MutableLiveData<>();
    private MutableLiveData<AmbulanceProfile> ambulanceProfileLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<CallStructure>> callsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> updatedStatusLiveData = new MutableLiveData<>();

    LiveData<ClientProfile> getClientProfile() {
        return clientProfileLiveData;
    }

    LiveData<AmbulanceProfile> getAmbulanceProfile() {
        return ambulanceProfileLiveData;
    }

    LiveData<ArrayList<CallStructure>> getCallStructure() {
        return callsLiveData;
    }

    LiveData<String> getUpdatedStatusLiveData() {
        return updatedStatusLiveData;
    }

    public void getData(String uid) {
        pds.getProfile(this, uid);
        pds.getAmbulanceProfile(this, uid);
    }

    public void updateClient(ClientProfile value) {
        clientProfileLiveData.setValue(value);
    }

    public void updateAmbulanceProfile(AmbulanceProfile ambulanceProfile) {
        ambulanceProfileLiveData.setValue(ambulanceProfile);
    }

    public void subscribeOnCalls(ClientProfile clientProfile, AmbulanceProfile ambulanceProfile, Boolean isFirst) {
        db.getCallKeys(this, clientProfile, ambulanceProfile, isFirst);
    }

    public void updateCalls(ArrayList<CallStructure> newData) {
        if (newData != null) {
            callsLiveData.setValue(newData);
        }
    }

    public void updateState(String uid, String status) {
        db.updateCallStatus(this, uid, status);
    }

    public void updatedCallStatus(String status) {
        updatedStatusLiveData.setValue(status);
    }
}
