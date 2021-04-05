package com.oshaev.projectx.customobjects;


import com.google.firebase.database.Exclude;

public class Instrument {

    String TICKER;
    Double PER;
    Long DATE;
    Long TIME;
    Double OPEN;
    Double HIGH;
    Double LOW;
    Double CLOSE;
    Long VOL;


    public Instrument() {
    }


    public Instrument(String TICKER, Double PER, Long DATE, Long TIME, Double OPEN, Double HIGH, Double LOW, Double CLOSE, Long VOL) {
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

    public Double getTimeMinutes(Double timeDifference)
    {
        String dateStr = DATE.toString();
        String dayStr = dateStr.substring(6);
        Long dayMinutes = Long.parseLong(dayStr)*24*60;
        String hoursStr = TIME.toString().substring(0,2);
        String minutesStr = TIME.toString().substring(2,4);
        Double outputTime = Double.parseDouble(hoursStr)*60+Long.parseLong(minutesStr)+dayMinutes-timeDifference;
        return outputTime;
    }

    public Double getTimeMinutes()
    {
        String dateStr = DATE.toString();
        String dayStr = dateStr.substring(6);
        Long dayMinutes = Long.parseLong(dayStr)*24*60;
        String hoursStr = TIME.toString().substring(0,2);
        String minutesStr = TIME.toString().substring(2,4);
        Double outputTime = Double.parseDouble(hoursStr)*60+Long.parseLong(minutesStr)+dayMinutes;

        return outputTime;
    }

    public String getTICKER() {
        return TICKER;
    }

    public void setTICKER(String TICKER) {
        this.TICKER = TICKER;
    }


    public Double getPER() {
        return PER;
    }

    public void setPER(Double PER) {
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

    public Double getOPEN() {
        return OPEN;
    }

    public void setOPEN(Double OPEN) {
        this.OPEN = OPEN;
    }

    public Double getHIGH() {
        return HIGH;
    }

    public void setHIGH(Double HIGH) {
        this.HIGH = HIGH;
    }

    public Double getLOW() {
        return LOW;
    }

    public void setLOW(Double LOW) {
        this.LOW = LOW;
    }

    public Double getCLOSE() {
        return CLOSE;
    }

    public void setCLOSE(Double CLOSE) {
        this.CLOSE = CLOSE;
    }

    public Long getVOL() {
        return VOL;
    }

    public void setVOL(Long VOL) {
        this.VOL = VOL;
    }
}












