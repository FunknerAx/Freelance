package kz.ilyas.gasindicator.data.model;

public class Indicator {
    private String id;
    private String address;
    private String position;
    private String currentValue;
    private String criticalValue;

    public Indicator(String id, String address, String position, String currentValue, String criticalValue) {
        this.id = id;
        this.address = address;
        this.position = position;
        this.currentValue = currentValue;
        this.criticalValue = criticalValue;
    }

    public Indicator() {
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
