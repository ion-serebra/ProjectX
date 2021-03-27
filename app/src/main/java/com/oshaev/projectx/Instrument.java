package com.oshaev.projectx;


import com.google.firebase.database.Exclude;

public class Instrument {

    String TICKER;
    Long PER;
    Long DATE;
    Long TIME;
    Long OPEN;
    Long HIGH;
    Long LOW;
    Long CLOSE;
    Long VOL;


    public Instrument() {
    }

    public Instrument(String TICKER, Long PER, Long DATE, Long TIME, Long OPEN, Long HIGH, Long LOW, Long CLOSE, Long VOL) {
        this.TICKER = TICKER;
        this.PER = PER;
        this.DATE = DATE;
        this.TIME = TIME;
        this.OPEN = OPEN;
        this.HIGH = HIGH;
        this.LOW = LOW;
        this.CLOSE = CLOSE;
        this.VOL = VOL;
    }

    public String getTICKER() {
        return TICKER;
    }

    public void setTICKER(String TICKER) {
        this.TICKER = TICKER;
    }

    public Long getPER() {
        return PER;
    }

    public void setPER(Long PER) {
        this.PER = PER;
    }

    public Long getDATE() {
        return DATE;
    }

    public void setDATE(Long DATE) {
        this.DATE = DATE;
    }

    public Long getTIME() {
        return TIME;
    }

    public void setTIME(Long TIME) {
        this.TIME = TIME;
    }

    public Long getOPEN() {
        return OPEN;
    }

    public void setOPEN(Long OPEN) {
        this.OPEN = OPEN;
    }

    public Long getHIGH() {
        return HIGH;
    }

    public void setHIGH(Long HIGH) {
        this.HIGH = HIGH;
    }

    public Long getLOW() {
        return LOW;
    }

    public void setLOW(Long LOW) {
        this.LOW = LOW;
    }

    public Long getCLOSE() {
        return CLOSE;
    }

    public void setCLOSE(Long CLOSE) {
        this.CLOSE = CLOSE;
    }

    public Long getVOL() {
        return VOL;
    }

    public void setVOL(Long VOL) {
        this.VOL = VOL;
    }
}












