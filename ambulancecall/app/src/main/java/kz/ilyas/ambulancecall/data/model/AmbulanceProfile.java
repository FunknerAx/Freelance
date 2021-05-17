package kz.ilyas.ambulancecall.data.model;

import java.io.Serializable;
import java.util.HashMap;

public class AmbulanceProfile implements Serializable {

    private String uid;
    private String name;
    private String address;
    private String isFree;
    private String xCoordinate;
    private String yCoordinate;

    public AmbulanceProfile() {
    }

    public AmbulanceProfile(HashMap<String, String> map) {
        this.uid = map.get("uid");
        this.name = map.get("name");
        this.address = map.get("address");
        this.isFree = map.get("free");
        this.xCoordinate = map.get("xCoordinate");
        this.yCoordinate = map.get("yCoordinate");
    }

    public AmbulanceProfile(String uid, String name, String address, String isFree, String xCoordinate, String yCoordinate) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.isFree = isFree;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String isFree() {
        return isFree;
    }

    public void setFree(String free) {
        isFree = free;
    }

    public String getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public String getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
