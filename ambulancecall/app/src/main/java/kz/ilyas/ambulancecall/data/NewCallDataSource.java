package kz.ilyas.ambulancecall.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kz.ilyas.ambulancecall.data.model.AmbulanceProfile;
import kz.ilyas.ambulancecall.data.model.CallStructure;
import kz.ilyas.ambulancecall.data.model.ClientProfile;
import kz.ilyas.ambulancecall.data.model.Symptoms;
import kz.ilyas.ambulancecall.data.model.Symtom;
import kz.ilyas.ambulancecall.ui.newCall.NewCallViewModel;

public class NewCallDataSource {

    static DatabaseReference db = new DataBaseSource().getDb();
    private Symptoms mySymptoms = new Symptoms();

//    public void update() {
//        Symptoms s = new Symptoms();
//        ArrayList<Symtom> ar = new ArrayList<>();
//        ar.add(new Symtom("повышенная температура",3));
//        ar.add(new Symtom("остановка сердца",1));
//        ar.add(new Symtom("открытый перелом",2));
//        ar.add(new Symtom("закрытый перелом",4));
//        s.setSymtoms(ar);
//        db.child("symptoms").setValue(s);
//    }

    public void getSymptoms(NewCallViewModel newCallViewModel) {
        db.child("symptoms").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    mySymptoms = task.getResult().getValue(Symptoms.class);
                    newCallViewModel.updateSymptomsFromDb(task.getResult().getValue(Symptoms.class));
                }
            }
        });
    }

    public void initCall(NewCallViewModel newCallViewModel, ClientProfile clientProfile, String callAddress, double callXCoordinate, double callYCoordinate, ArrayList<String> mySymptomsArr) {

        ArrayList<Symtom> callSymptoms = new ArrayList<>();
        int category = 0;

        ArrayList<Symtom> symptoms = mySymptoms.getSymtoms();
        Iterator<Symtom> it = symptoms.iterator();
        Iterator<String> itInArr = mySymptomsArr.iterator();

        while (it.hasNext()) {
            Symtom cur = it.next();

            while (itInArr.hasNext()) {
                String name = itInArr.next();

                if (cur.getName().equals(name)) {
                    callSymptoms.add(cur);
                    if (cur.getStatus() > category)
                        category = cur.getStatus();

                }
            }

            itInArr = mySymptomsArr.iterator();
        }

        getFreeAmbulance(newCallViewModel, clientProfile, callAddress, callXCoordinate, callYCoordinate, category, callSymptoms);

    }

    private void getFreeAmbulance(NewCallViewModel newCallViewModel, ClientProfile clientProfile, String callAddress, double callXCoordinate, double callYCoordinate, int category, ArrayList<Symtom> callSymptoms) {
        db.child("ambulances").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap<String, HashMap<String, HashMap<String, String>>> arrayMap = (HashMap<String, HashMap<String, HashMap<String, String>>>) task.getResult().getValue();

                    ArrayList<String> hashMapKeys = new ArrayList<>(arrayMap.keySet());
                    ArrayList<AmbulanceProfile> ambulanceProfiles = new ArrayList<>();

                    for (String key : hashMapKeys) {
                        HashMap<String, HashMap<String, String>> cur = arrayMap.get(key);
                        if(cur != null){
                            ambulanceProfiles.add(new AmbulanceProfile(cur.get("profile")));
                        }
                    }

                    chooseAmbulance(newCallViewModel, clientProfile, callAddress, callXCoordinate, callYCoordinate, category, callSymptoms, ambulanceProfiles);
                } else {
                    newCallViewModel.sendError("Ошибка при получении скорых\nПовторите позже");
                }
            }
        });
    }

    private void chooseAmbulance(NewCallViewModel newCallViewModel, ClientProfile clientProfile, String callAddress, double callXCoordinate, double callYCoordinate, int category, ArrayList<Symtom> callSymptoms, ArrayList<AmbulanceProfile> ambulanceProfiles) {
        AmbulanceProfile freeAmbulance = null;

        for (AmbulanceProfile cur : ambulanceProfiles) {
            if (Boolean.valueOf(cur.isFree()) && checkDistance(callXCoordinate, callYCoordinate, Double.valueOf(cur.getxCoordinate()), Double.valueOf(cur.getyCoordinate()))) {
                freeAmbulance = cur;
                break;
            }
        }

        if (freeAmbulance == null) {
            newCallViewModel.sendError("В радиусе 1км\nнет скорых");
        } else {
            createCallInDb(newCallViewModel, freeAmbulance, callAddress, category, callSymptoms, clientProfile);
        }
    }

    private boolean checkDistance(double callXCoordinate, double callYCoordinate, double ambulanceXCoordinate, double ambulanceYCoordinate) {

        if (CalculationByDistance(callXCoordinate, callYCoordinate, ambulanceXCoordinate, ambulanceYCoordinate) < 1) {
            return true;
        } else {
            return false;
        }

    }

    public double CalculationByDistance(double callXCoordinate, double callYCoordinate, double ambulanceXCoordinate, double ambulanceYCoordinate) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = callXCoordinate;
        double lat2 = ambulanceXCoordinate;
        double lon1 = callYCoordinate;
        double lon2 = ambulanceYCoordinate;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void createCallInDb(NewCallViewModel newCallViewModel, AmbulanceProfile freeAmbulance, String callAddress, int category, ArrayList<Symtom> callSymptoms, ClientProfile clientProfile) {
        String key = db.child("calls").push().getKey();

        db.child("calls").child(key).setValue(new CallStructure(key, callAddress, category, freeAmbulance.getName(),"Ожидает" ,clientProfile, callSymptoms))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addCallToUesr(newCallViewModel, clientProfile, key, freeAmbulance);
                        } else {
                            newCallViewModel.sendError("Ошибка при добавлении нового вызова\nПопробуйте позже");
                        }
                    }
                });

    }

    private void addCallToUesr(NewCallViewModel newCallViewModel, ClientProfile clientProfile, String key, AmbulanceProfile freeAmbulance) {
        db.child("users").child(clientProfile.getUid()).child("calls").child(key).setValue(key)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addCallToAmbulance(newCallViewModel, key, freeAmbulance.getUid());
                        } else {
                            newCallViewModel.sendError("Ошибка при добавлении нового вызова пользователю\nПопробуйте позже");
                        }
                    }
                });
    }

    private void addCallToAmbulance(NewCallViewModel newCallViewModel, String key, String uid) {
        db.child("ambulances").child(uid).child("calls").child(key).setValue(key)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            newCallViewModel.callCreated();
                        } else {
                            newCallViewModel.sendError("Ошибка при добавлении нового вызова скорой\nПопробуйте позже");
                        }
                    }
                });
        ;
    }


}
