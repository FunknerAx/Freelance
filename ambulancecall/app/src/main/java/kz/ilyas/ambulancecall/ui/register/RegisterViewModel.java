package kz.ilyas.ambulancecall.ui.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kz.ilyas.ambulancecall.data.ProfileDataSource;
import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.ClientProfile;


public class RegisterViewModel extends ViewModel {
    private ProfileDataSource profileDataSource = new ProfileDataSource();
    private MutableLiveData<ClientProfile> clientProfileLiveData = new MutableLiveData<>();
    private MutableLiveData<AmbulanceProfile> ambulanceProfileMutableLiveData = new MutableLiveData<>();

    LiveData<ClientProfile> getClientProfile() {
        return clientProfileLiveData;
    }

    LiveData<AmbulanceProfile> getAmbulanceProfile() {
        return ambulanceProfileMutableLiveData;
    }

    public void sendClientProfile(String surname, String name, String patronymic, String sex, String birthDate, String weight, String email, String password, String invalid) {

        profileDataSource.registerProfile(this, surname, name, patronymic, sex, birthDate, weight, email, password, invalid);
        Log.v("PROFILE_CREATION", "SEND DATA TO DB");
    }

    public void registerComplete(ClientProfile clientProfile) {
        clientProfileLiveData.setValue(clientProfile);
    }

    public void registerAmbulance(String isFree, String name, String address, double xCoordinate, double yCoordinate, String email, String password) {
        profileDataSource.registerAmbulance(this, isFree, name, address, xCoordinate, yCoordinate, email, password);
    }

    public void ambulanceRegisterComplete(AmbulanceProfile ambulanceProfile) {
        ambulanceProfileMutableLiveData.setValue(ambulanceProfile);
    }
}
