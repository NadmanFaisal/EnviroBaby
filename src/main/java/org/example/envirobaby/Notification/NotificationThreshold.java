package org.example.envirobaby.Notification;

public class NotificationThreshold {

    private int defaultLoudThreshold;
    private double defaultTempUbound;
    private double defaultTempLbound;
    private double defaultHumUbound;
    private double defaultHumLbound;


    private int loudThreshold;
    private double tempUpperBound;
    private double tempLowerBound;
    private double humUpperBound;
    private double humLowerBound;



    public NotificationThreshold(){
        this.loudThreshold = defaultLoudThreshold;
        this.tempUpperBound = defaultTempUbound;
        this.tempLowerBound = defaultTempLbound;
        this.humUpperBound = defaultHumUbound;
        this.humLowerBound = defaultHumLbound;
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
        this.loudThreshold = defaultLoudThreshold;
        this.tempUpperBound = defaultTempUbound;
        this.tempLowerBound = defaultTempLbound;
        this.humUpperBound = defaultHumUbound;
        this.humLowerBound = defaultHumLbound;
    }

    public void setDefaultThresholds(String ageGroupOption) {
        switch (ageGroupOption) {
            case "0-6 Months"->{
                this.defaultLoudThreshold=65;
                this.defaultTempUbound=36;
                this.defaultTempLbound=30;
                this.defaultHumUbound=60;
                this.defaultHumLbound=45;
            }
            case "6-12 Months"->{
                this.defaultLoudThreshold=65;
                this.defaultTempUbound=40;
                this.defaultTempLbound=30;
                this.defaultHumUbound=60;
                this.defaultHumLbound=45;
            }
            case "12-24 Months"->{
                this.defaultLoudThreshold=65;
                this.defaultTempUbound=42;
                this.defaultTempLbound=20;
                this.defaultHumUbound=60;
                this.defaultHumLbound=35;
            }
        }
    }
}
