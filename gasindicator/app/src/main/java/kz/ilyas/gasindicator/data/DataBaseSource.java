package kz.ilyas.gasindicator.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import kz.ilyas.gasindicator.data.model.Client;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Statistic;
import kz.ilyas.gasindicator.data.model.Statistics;
import kz.ilyas.gasindicator.ui.login.LoginViewModel;
import kz.ilyas.gasindicator.ui.mainPage.MainPageViewModel;
import kz.ilyas.gasindicator.ui.mainPage.indicator.RegisterIndicatorViewModel;
import kz.ilyas.gasindicator.ui.mainPage.indicator.StaticIndicatorViewModel;
import kz.ilyas.gasindicator.ui.register.RegisterViewModel;

public class DataBaseSource {
    private RegisterViewModel registerViewModel;
    private LoginViewModel loginViewModel;
    private RegisterIndicatorViewModel registerIndicatorViewModel;
    private MainPageViewModel mainPageViewModel;
    private StaticIndicatorViewModel staticIndicatorViewModel;
    protected DatabaseReference db;
    protected FirebaseAuth mAuth;

    public DataBaseSource(RegisterIndicatorViewModel registerIndicatorViewModel) {
        this();
        this.registerIndicatorViewModel = registerIndicatorViewModel;
    }

    public DataBaseSource(RegisterViewModel registerViewModel) {
        this();
        this.registerViewModel = registerViewModel;

    }

    public DataBaseSource(LoginViewModel loginViewModel) {
        this();
        this.loginViewModel = loginViewModel;
    }

    public DataBaseSource(MainPageViewModel mainPageViewModel) {
        this();
        this.mainPageViewModel = mainPageViewModel;

    }

    public DataBaseSource(StaticIndicatorViewModel staticIndicatorViewModel) {
        this();
        this.staticIndicatorViewModel = staticIndicatorViewModel;
    }

    public DataBaseSource() {
        this.db = FirebaseDatabase.getInstance("https://gasindicator-d6ac9-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }


    public void addNewUser(Client client) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(client.getName())
                                    .build();

                            mAuth.getCurrentUser().updateProfile(update)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            registerViewModel.sendResult(mAuth, null);
                                        }
                                    });
                        } else {
                            registerViewModel.sendResult(mAuth, task.getException());
                        }
                    }
                });

    }

    public void signIn(String email, String password) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginViewModel.sendResult(mAuth, null);
                        } else {
                            loginViewModel.sendResult(mAuth, task.getException());
                        }
                    }
                });
    }

    public void registerIndicator(String uid, Indicator indicator) {
        db.child("indicators").child(indicator.getId()).child("info").setValue(indicator)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            connectToClient(uid, indicator);
                        } else {
                            registerIndicatorViewModel.sendResult("ERROR|registerIndicator", task.getException());
                        }
                    }
                });
    }

    private void connectToClient(String uid, Indicator indicator) {
        db.child("clientIndicators").child(uid).child(indicator.getId()).setValue(indicator.getId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createFirstStatics(indicator);
                        } else {
                            registerIndicatorViewModel.sendResult("ERROR|connectToClient", task.getException());
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFirstStatics(Indicator indicator) {
//        Log.v("STRINGASD",String.format("%.3f", Math.random() * Double.valueOf(indicator.getCriticalValue())));
        ArrayList<LocalDateTime> a = new ArrayList<>();
        LocalDateTime cur = LocalDateTime.now();
        a.add(cur);
        for (int i = 0; i < 5040; i++) {
            a.add(a.get(a.size() - 1).plusMinutes(2));
        }
        for (LocalDateTime current : a) {
            db.child("indicators").child(indicator.getId()).child("statics")
                    .child(current.toString().substring(0, 10)).child(current.toString().substring(11, 19))
                    .setValue(String.format("%.1f", Math.random() * Double.valueOf(indicator.getCriticalValue())));
        }
        registerIndicatorViewModel.sendResult("OK", null);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateStaticForMonth() {

    }

    public void subscribe(Client client, ArrayList<String> actualKeys) {
        db.child("indicators").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    HashMap<String, Object> updated = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<Indicator> newData = new ArrayList<>();
                    ArrayList<String> key = new ArrayList<>(updated.keySet());
                    if (key.size() > 1) {
                        HashMap<String, String> current = (HashMap<String, String>) updated.get(key.get(1));
                        Indicator indicator = new Indicator(current);

                        for (String cur : actualKeys) {
                            if (indicator.getId().equals(cur))
                                newData.add(indicator);
                        }

                        mainPageViewModel.updateCalls(newData);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    HashMap<String, Object> updated = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<Indicator> newData = new ArrayList<>();
                    ArrayList<String> key = new ArrayList<>(updated.keySet());
                    if (key.size() > 1) {
                        HashMap<String, String> current = (HashMap<String, String>) updated.get(key.get(1));
                        Indicator indicator = new Indicator(current);

                        if (actualKeys.size() > 0)
                            for (String cur : actualKeys) {
                                if (indicator.getId().equals(cur))
                                    newData.add(indicator);
                            }

                        mainPageViewModel.updateCalls(newData);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> updated = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<Indicator> newData = new ArrayList<>();
                    ArrayList<String> key = new ArrayList<>(updated.keySet());
                    if (key.size() > 1) {
                        HashMap<String, String> current = (HashMap<String, String>) updated.get(key.get(1));
                        Indicator indicator = new Indicator(current);

                        if (actualKeys.size() > 0)
                            for (String cur : actualKeys) {
                                if (indicator.getId().equals(cur))
                                    newData.add(indicator);
                            }

                        mainPageViewModel.updateCalls(newData);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getKeys(Client client) {
        db.child("clientIndicators").child(client.getUID()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, String> keys = (HashMap<String, String>) task.getResult().getValue();
                            ArrayList<String> actualKeys;
                            if (keys != null && keys.keySet() != null && keys.keySet().size() > 0) {
                                actualKeys = new ArrayList<>(keys.keySet());
                                subscribe(client, actualKeys);
                            }

                        } else {

                        }
                    }
                });
    }

    public void addExists(String uid, String id, String password) {
        db.child("indicators").child(id).child("info").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            Indicator indicator = task.getResult().getValue(Indicator.class);
                            if (indicator != null) {
                                if (indicator.getPassword().equals(password)) {
                                    connectToClient(uid, indicator);
                                } else {
                                    registerIndicatorViewModel.sendResult("FAILED\nWrong password", null);
                                }
                            } else {
                                registerIndicatorViewModel.sendResult("FAILED\nДанного устройства не существует", null);

                            }
                        } else {
                            registerIndicatorViewModel.sendResult("ERROR", task.getException());
                        }
                    }
                });
    }

    public void getStatistics(String id) {
        ArrayList<Statistics> statics = new ArrayList<>();
        db.child("indicators").child(id).child("statics").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, HashMap<String, String>> result = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
                            ArrayList<String> dateKeys = new ArrayList<>(result.keySet());
                            for (int i = 0; i < dateKeys.size(); i++) {
                                ArrayList<String> timeKeys = new ArrayList<>(result.get(dateKeys.get(i)).keySet());
                                ArrayList<Statistic> values = new ArrayList<>();
                                for (int j = 0; j < timeKeys.size(); j++){
                                    values.add(new Statistic(timeKeys.get(j),result.get(dateKeys.get(i)).get(timeKeys.get(j))));
                                }
                                statics.add(new Statistics(dateKeys.get(i),values));
                            }

                            staticIndicatorViewModel.sendResult(statics, null);

                        } else {
                            staticIndicatorViewModel.sendResult(statics, task.getException());
                        }
                    }
                });
    }
}
