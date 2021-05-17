package kz.ilyas.ambulancecall.data.model;

import java.util.ArrayList;

public class ItemOfList {
    private String id;
    private String displayNam;
    private String address;
    private ClientProfile clientProfile;
    private ArrayList<Symtom> symtomArrayList;
    private String category;
    private String state;
    private boolean client;

    public ItemOfList() {
    }

    public ItemOfList(String id, String displayNam, String address, String category, String state, boolean client, ClientProfile clientProfile, ArrayList<Symtom> symtomArrayList) {
        this.id = id;
        this.displayNam = displayNam;
        this.address = address;
        this.category = category;
        this.state = state;
        this.client = client;
        this.clientProfile = clientProfile;
        this.symtomArrayList = symtomArrayList;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public ArrayList<Symtom> getSymtomArrayList() {
        return symtomArrayList;
    }

    public void setSymtomArrayList(ArrayList<Symtom> symtomArrayList) {
        this.symtomArrayList = symtomArrayList;
    }

    public boolean client() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayNam() {
        return displayNam;
    }

    public void setDisplayNam(String displayNam) {
        this.displayNam = displayNam;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
