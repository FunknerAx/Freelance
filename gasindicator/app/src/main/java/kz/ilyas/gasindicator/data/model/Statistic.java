package kz.ilyas.gasindicator.data.model;

public class Statistic {
    private String time;
    private String value;

    public Statistic() {
    }

    public Statistic(String time, String value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public Double getValueDouble(){
        return Double.valueOf(value.replace(',','.'));
    }

    public void setValue(String value) {
        this.value = value;
    }
}
