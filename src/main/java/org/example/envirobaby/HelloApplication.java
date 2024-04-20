package org.example.envirobaby;

public class HelloApplication {


    public static void main(String[] args) {
        MQTTSubscriber subscriber = new MQTTSubscriber();

        while (true) {
            try {
                Thread.sleep(1000);
                String noiseLevel = subscriber.getLastReceivedMessage();
                if (noiseLevel != null) {
                    System.out.println(noiseLevel);
                }
//                else {
//                    System.out.println("Working");
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}