package kz.ilyas.ambulancecall.data.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CallStructure {
    private String uid;
    private String address;
    private int category;
    private String ambulanceName;
    private String status;
    private ClientProfile clientProfile;
    private ArrayList<Symtom> symptoms;

    public CallStructure(String uid, String address, int category, String ambulanceName, String status, ClientProfile clientProfile, ArrayList<Symtom> symptoms) {
        this.uid = uid;
        this.address = address;
        this.category = category;
        this.ambulanceName = ambulanceName;
        this.status = status;
        this.clientProfile = clientProfile;
        this.symptoms = symptoms;
    }

    public CallStructure(HashMap<String, Object> in) {
        this.uid = (String) in.get("uid");
        this.address = (String) in.get("address");
        this.category = (int) ((long) in.get("category"));
        this.ambulanceName = (String) in.get("uid");
        this.status = (String) in.get("status");
        this.clientProfile = new ClientProfile((HashMap<String, String>) in.get("clientProfile"));
        this.symptoms = new ArrayList<Symtom>();
        ArrayList<Object> inS2 = (ArrayList<Object>) in.get("symptoms");
        if (inS2 != null){
            for (int i = 0; i < inS2.size(); i++){
                this.symptoms.add(new Symtom((HashMap<String,Object>) inS2.get(i)));
            }
        }

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getAmbulanceName() {
        return ambulanceName;
    }

    public void setAmbulanceName(String ambulanceName) {
        this.ambulanceName = ambulanceName;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Symtom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symtom> symptoms) {
        this.symptoms = symptoms;
    }
}
