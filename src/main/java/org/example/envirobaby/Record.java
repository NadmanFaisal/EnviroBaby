package org.example.envirobaby;

public class Record {
    private String recordTime;
    private int recordNoise;
    private double recordTemp;
    private double recordTempF;
    private double recordHum;

    public Record(String recordTime, int recordNoise, double recordTemp, double recordHum) { //object for each recorded row on record table. contained in observable array for data displays
        this.recordTime = recordTime;
        this.recordNoise = recordNoise;
        this.recordTemp = recordTemp;
        this.recordHum = recordHum;
        this.recordTempF = (recordTemp * (9/5)) + 32; // for alternative temp unit view
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
