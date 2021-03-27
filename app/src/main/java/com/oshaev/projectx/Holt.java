package com.oshaev.projectx;

public class Holt {
    private double holtA;
    private double holtB;

    public Holt(double holtA, double holtB) {
        this.holtA = holtA;
        this.holtB = holtB;
    }

    public double getHoltA() {
        return holtA;
    }

    public void setHoltA(double holtA) {
        this.holtA = holtA;
    }

    public double getHoltB() {
        return holtB;
    }

    public void setHoltB(double holtB) {
        this.holtB = holtB;
    }
}
