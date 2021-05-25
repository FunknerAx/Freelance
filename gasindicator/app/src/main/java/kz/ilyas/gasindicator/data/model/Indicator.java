package kz.ilyas.gasindicator.data.model;

import java.io.Serializable;
import java.util.HashMap;

public class Indicator implements Serializable {
    private String id;
    private String address;
    private String position;
    private String currentValue;
    private String criticalValue;
    private String password;

    public Indicator(String id, String address, String position, String currentValue, String criticalValue, String password) {
        this.id = id;
        this.address = address;
        this.position = position;
        this.currentValue = currentValue;
        this.criticalValue = criticalValue;
        this.password = password;
    }

    public Indicator(HashMap<String, String> object) {
        this.id = object.get("id");
        this.address = object.get("address");
        this.position = object.get("position");
        this.currentValue = object.get("currentValue");
        this.criticalValue = object.get("criticalValue");
    }

    public Indicator() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getCriticalValue() {
        return criticalValue;
    }

    public void setCriticalValue(String criticalValue) {
        this.criticalValue = criticalValue;
    }
}
