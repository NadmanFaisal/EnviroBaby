package org.example.envirobaby;

public class Record {
    private String recordTime;
    private int recordNoise;
    private double recordTemp;
    private double recordTempF;
    private double recordHum;

    public Record(String recordTime, int recordNoise, double recordTemp, double recordHum) {
        this.recordTime = recordTime;
        this.recordNoise = recordNoise;
        this.recordTemp = recordTemp;
        this.recordHum = recordHum;
        this.recordTempF = (recordTemp * (9/5)) + 32;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public double getRecordTemp() {
        return recordTemp;
    }

    public double getRecordHum() {
        return recordHum;
    }

    public int getRecordNoise() {
        return recordNoise;
    }

    public double getRecordTempF() {
        return recordTempF;
    }
}
