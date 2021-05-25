package kz.ilyas.gasindicator.ui.mainPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import kz.ilyas.gasindicator.data.DataBaseSource;
import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.data.model.Indicator;

public class MainPageViewModel extends ViewModel {
    private DataBaseSource db = new DataBaseSource(this);

    MutableLiveData<ArrayList<Indicator>> arrayListMutableLiveData = new MutableLiveData<>();

    LiveData<ArrayList<Indicator>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public void subscribeOnIndicators(Client client) {
        db.getKeys(client);
    }

    public void updateCalls(ArrayList<Indicator> newData) {
        if(newData != null)
            arrayListMutableLiveData.setValue(newData);
    }
}
