package org.example.envirobaby;

// This class is used to ensure the subscription is working by printing in the console.
public class Main {

    public static void main(String[] args) {
        MQTTSubscriber subscriber = new MQTTSubscriber();

        while (true) {
            try {
                Thread.sleep(1000);
                String noiseLevel = subscriber.getLastReceivedMessage();
                if (noiseLevel != null) {
                    System.out.println(noiseLevel);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}