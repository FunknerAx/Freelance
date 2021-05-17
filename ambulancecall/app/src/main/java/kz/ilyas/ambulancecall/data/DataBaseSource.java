package kz.ilyas.ambulancecall.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.CallStructure;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.ui.mainPage.MainPageViewModel;

public class DataBaseSource {

    private DatabaseReference db;


    public DataBaseSource() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ambulancecall-18638-default-rtdb.europe-west1.firebasedatabase.app/");
        this.db = database.getReference();
    }

    public DatabaseReference getDb() {
        return db;
    }

    public void subscribeOnNewCalls(MainPageViewModel mainPageViewModel, ArrayList<String> keys) {
        db.child("calls").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    HashMap<String, Object> updatedCalls = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<CallStructure> newData = new ArrayList<>();

                    for (String cur : keys) {
                        if (updatedCalls.get("uid").equals(cur))
                            newData.add(new CallStructure(updatedCalls));
                    }

                    mainPageViewModel.updateCalls(newData);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    HashMap<String, Object> updatedCalls = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<CallStructure> newData = new ArrayList<>();

                    for (String cur : keys) {
                        if (updatedCalls.get("uid").equals(cur))
                            newData.add(new CallStructure(updatedCalls));
                    }

                    mainPageViewModel.updateCalls(newData);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    HashMap<String, Object> updatedCalls = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<CallStructure> newData = new ArrayList<>();

                    for (String cur : keys) {
                        if (updatedCalls.get("uid").equals(cur))
                            newData.add(new CallStructure(updatedCalls));
                    }

                    mainPageViewModel.updateCalls(newData);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOnce(MainPageViewModel mainPageViewModel, ArrayList<String> keys) {
        db.child("calls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, HashMap<String, Object>> updatedCalls = (HashMap<String, HashMap<String, Object>>) snapshot.getValue();
                ArrayList<String> keysOfDb = new ArrayList<>(updatedCalls.keySet());
                ArrayList<CallStructure> newData = new ArrayList<>();

                for (String cur : keysOfDb) {
                    if (keys.contains(cur))
                        newData.add(new CallStructure(updatedCalls.get(cur)));
                }


                mainPageViewModel.updateCalls(newData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCallKeys(MainPageViewModel mainPageViewModel, ClientProfile clientProfile, AmbulanceProfile ambulanceProfile, Boolean isFirst) {
        if (clientProfile != null) {
            db.child("users").child(clientProfile.getUid()).child("calls").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getValue() != null) {
                                    HashMap<String, String> keys = (HashMap<String, String>) task.getResult().getValue();
                                    ArrayList<String> arrKey = new ArrayList<>(keys.keySet());

                                    if (isFirst) {
                                        getOnce(mainPageViewModel, arrKey);
                                    } else {
                                        subscribeOnNewCalls(mainPageViewModel, arrKey);
                                    }
                                } else {
                                    mainPageViewModel.updateCalls(new ArrayList<>());
                                }
                            }
                        }
                    });
        } else {
            db.child("ambulances").child(ambulanceProfile.getUid()).child("calls").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getValue() != null) {
                                    HashMap<String, String> keys = (HashMap<String, String>) task.getResult().getValue();
                                    ArrayList<String> arrKey = new ArrayList<>(keys.keySet());

                                    if (isFirst) {
                                        getOnce(mainPageViewModel, arrKey);
                                    } else {
                                        subscribeOnNewCalls(mainPageViewModel, arrKey);
                                    }
                                } else {
                                    mainPageViewModel.updateCalls(new ArrayList<>());
                                }
                            }
                        }
                    });
        }
    }

    public void updateCallStatus(MainPageViewModel mainPageViewModel, String uid, String status) {
        db.child("calls").child(uid).child("status").setValue(status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mainPageViewModel.updatedCallStatus("Статус успешно обновлен");
                        } else {
                            mainPageViewModel.updatedCallStatus("Произошла ошибка5\nпри обновлении статуса");
                        }
                    }
                });
    }
}
