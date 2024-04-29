package org.example.envirobaby;

public class ParameterData {

    private int loudValue;
    private double tempValue;
    private double humValue;

    public ParameterData() {
        this.loudValue=0;
        this.tempValue=0;
        this.humValue=0;
    }

    public void setLoudValue(int loudLvl){
        this.loudValue=loudLvl;
    }
    public void setTempValue(double tempLvl){
        this.tempValue=tempLvl;
    }
    public void setHumValue(double humLvl){
        this.humValue=humLvl;
    }

    public int getLoudValue() {
        return loudValue;
    }

    public double getTempValue() {
        return tempValue;
    }

    public double getHumValue() {
        return humValue;
    }
}
