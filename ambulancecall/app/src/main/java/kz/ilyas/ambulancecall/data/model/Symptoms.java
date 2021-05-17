package kz.ilyas.ambulancecall.data.model;

import java.util.ArrayList;

public class Symptoms {
    private ArrayList<Symtom> symtoms;

    public Symptoms(ArrayList<Symtom> symtoms) {
        this.symtoms = symtoms;
    }

    public Symptoms() {
    }

    public ArrayList<Symtom> getSymtoms() {
        return symtoms;
    }

    public void setSymtoms(ArrayList<Symtom> symtoms) {
        this.symtoms = symtoms;
    }
}
