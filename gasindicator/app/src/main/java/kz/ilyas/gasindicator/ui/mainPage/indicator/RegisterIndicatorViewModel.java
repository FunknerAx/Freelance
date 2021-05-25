package kz.ilyas.gasindicator.ui.mainPage.indicator;

import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kz.ilyas.gasindicator.data.DataBaseSource;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Result;

public class RegisterIndicatorViewModel extends ViewModel {

    MutableLiveData<Result> resultMutableLiveData = new MutableLiveData<>();

    LiveData<Result> getResultLiveData() {
        return resultMutableLiveData;
    }

    private DataBaseSource db = new DataBaseSource(this);

    public boolean checkRegisterData(EditText id, EditText address, EditText position, EditText criticalValue, EditText password) {
        return id.getText().toString().isEmpty() && address.getText().toString().isEmpty() && position.getText().toString().isEmpty() && criticalValue.getText().toString().isEmpty() && password.getText().toString().isEmpty();
    }

    public boolean checkRegisterData(EditText idExists, EditText passwordExists) {
        return idExists.getText().toString().isEmpty() && passwordExists.getText().toString().isEmpty();

    }

    public void registerNewIndicator(String UID, Indicator indicator) {
        db.registerIndicator(UID, indicator);
    }

    public void sendResult(String status, Exception exception) {
        resultMutableLiveData.setValue(new Result(status, exception));
    }

    public void addExists(String uid, String id, String password) {
        db.addExists(uid, id, password);
    }
}
