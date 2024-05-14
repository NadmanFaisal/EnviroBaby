package org.example.envirobaby.Notification;

public class NotificationThreshold {

    private static final int DEFAULT_LOUD_THRESHOLD =90;
    private static final double DEFAULT_CTEMP_UBOUND =25.00;
    private static final double DEFAULT_CTEMP_LBOUND =18.00;
    private static final double DEFAULT_HUM_UBOUND =60.00;
    private static final double DEFAULT_HUM_LBOUND =30.00;


    private int loudThreshold;
    private double tempUpperBound;
    private double tempLowerBound;
    private double humUpperBound;
    private double humLowerBound;



    public NotificationThreshold(){
        this.loudThreshold = DEFAULT_LOUD_THRESHOLD;
        this.tempUpperBound = DEFAULT_CTEMP_UBOUND;
        this.tempLowerBound = DEFAULT_CTEMP_LBOUND;
        this.humUpperBound = DEFAULT_HUM_UBOUND;
        this.humLowerBound = DEFAULT_HUM_LBOUND;
    }


    public void setLoudThreshold(int loudThreshold) {
        this.loudThreshold = loudThreshold;
    }
    public void setTempUpperBound(double tempUpperBound) {
        this.tempUpperBound = tempUpperBound;
    }
    public void setTempLowerBound(double tempLowerBound) {
        this.tempLowerBound = tempLowerBound;
    }
    public void setHumUpperBound(double humUpperBound) {
        this.humUpperBound = humUpperBound;
    }
    public void setHumLowerBound(double humLowerBound) {
        this.humLowerBound = humLowerBound;
    }

    public int getLoudThreshold() {
        return loudThreshold;
    }
    public double getTempUpperBound() {
        return tempUpperBound;
    }
    public double getTempLowerBound() {
        return tempLowerBound;
    }

    public double getHumUpperBound() {
        return humUpperBound;
    }

    public double getHumLowerBound() {
        return humLowerBound;
    }

    public void setAllThresholds(int maxNoise, double maxTemp, double minTemp, double maxHum, double minHum){
        this.loudThreshold=maxNoise;
        this.tempUpperBound=maxTemp;
        this.tempLowerBound=minTemp;
        this.humUpperBound=maxHum;
        this.humLowerBound=minHum;
    }

    /**
     * Resets the threshold values to default values
     * such that if the default values are breached,
     * it triggers notification.
     */
    public void resetThresholds(){
        this.loudThreshold = DEFAULT_LOUD_THRESHOLD;
        this.tempUpperBound = DEFAULT_CTEMP_UBOUND;
        this.tempLowerBound = DEFAULT_CTEMP_LBOUND;
        this.humUpperBound = DEFAULT_HUM_UBOUND;
        this.humLowerBound = DEFAULT_HUM_LBOUND;
    }
}
