package com.oshaev.projectx.customobjects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Equity{

    private ArrayList<Instrument> currentInstruments;

    @Exclude
    private String key;

    public Equity() {
    }

    public ArrayList<Instrument> getCurrentInstruments() {
        return currentInstruments;
    }

    public void setCurrentInstruments(ArrayList<Instrument> currentInstruments) {
        this.currentInstruments = currentInstruments;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
