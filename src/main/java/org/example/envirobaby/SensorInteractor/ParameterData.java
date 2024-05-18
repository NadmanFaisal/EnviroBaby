package org.example.envirobaby.SensorInteractor;

public class ParameterData {

    private int loudValue;
    private double tempValue;
    private double humValue;

    public ParameterData() { //Object to constantly update with the most recent parameter readings
        this.loudValue=0; //values should almost immediately be changed upon initialisation in mqttreciever
        this.tempValue=0;
        this.humValue=0;
    }

    public void setLoudValue(int loudLvl){  //used by mqttreciever callback function
        this.loudValue=loudLvl;
    }
    public void setTempValue(double tempLvl){ //used by mqttreciever callback function
        this.tempValue=tempLvl;
    }
    public void setHumValue(double humLvl){ //used by mqttreciever callback function
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
