package kz.ilyas.gasindicator.data.model;

import java.util.ArrayList;

public class Statistics {
    private String date;
    private ArrayList<Statistic> statics;

    public Statistics() {
    }

    public Statistics(String date, ArrayList<Statistic> statics) {
        this.date = date;
        this.statics = statics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Statistic> getStatics() {
        return statics;
    }

    public void setStatics(ArrayList<Statistic> statics) {
        this.statics = statics;
    }
}
