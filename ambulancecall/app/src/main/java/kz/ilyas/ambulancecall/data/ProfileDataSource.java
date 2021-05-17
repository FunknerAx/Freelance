package kz.ilyas.ambulancecall.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.ui.mainPage.MainPageViewModel;
import kz.ilyas.ambulancecall.ui.register.RegisterViewModel;

import static android.content.ContentValues.TAG;

public class ProfileDataSource {

    protected FirebaseAuth mAuth;
    private RegisterViewModel rvm;
    private DatabaseReference db = new DataBaseSource().getDb();


    public void registerProfile(RegisterViewModel rvm, final String surname, final String name, final String patronymic, final String sex, final String birthDate, final String weight, final String email, String password, final String invalid) {
        mAuth = FirebaseAuth.getInstance();
        setRvm(rvm);
//        int numCores = Runtime.getRuntime().availableProcessors();
////        ThreadPoolExecutor executor = new ThreadPoolExecutor(numCores * 2, numCores *2,
////                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            ClientProfile clientProfile = new ClientProfile(mAuth.getCurrentUser().getUid(),
                                    surname, name,
                                    patronymic, sex,
                                    birthDate, email, invalid, weight);

                            addProfile(clientProfile);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public void addProfile(ClientProfile clientProfile) {

        getDb().child("users").child(clientProfile.getUid()).child("profile").setValue(clientProfile);
        getRvm().registerComplete(clientProfile);

    }

    public void getProfile(MainPageViewModel mpvm, String uid) {
        getDb().child("users").child(uid).child("profile").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            mpvm.updateClient(task.getResult().getValue(ClientProfile.class));
                        } else {
                            Log.v("PROFILE_NOT_FOUND", "Профиль не найден");
                        }
                    }
                });
    }

    public void getAmbulanceProfile(MainPageViewModel mpvm, String uid) {
        getDb().child("ambulances").child(uid).child("profile").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            mpvm.updateAmbulanceProfile(task.getResult().getValue(AmbulanceProfile.class));
                        } else {
                            Log.v("PROFILE_NOT_FOUND", "Профиль не найден");
                        }
                    }
                });
    }

    public RegisterViewModel getRvm() {
        return rvm;
    }

    public void setRvm(RegisterViewModel rvm) {
        this.rvm = rvm;
    }

    public DatabaseReference getDb() {
        return db;
    }

    public void registerAmbulance(RegisterViewModel registerViewModel, String isFree, String name, String address, double xCoordinate, double yCoordinate, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        setRvm(registerViewModel);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AmbulanceProfile ambulanceProfile = new AmbulanceProfile(mAuth.getCurrentUser().getUid(), name, address, isFree, String.valueOf(xCoordinate), String.valueOf(yCoordinate));
                            addAmbulance(ambulanceProfile);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private void addAmbulance(AmbulanceProfile ambulanceProfile) {
        getDb().child("ambulances").child(ambulanceProfile.getUid()).child("profile").setValue(ambulanceProfile);
        getRvm().ambulanceRegisterComplete(ambulanceProfile);
    }
}


