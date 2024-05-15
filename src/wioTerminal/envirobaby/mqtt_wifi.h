#ifndef MQTT_WIFI_H                                        
#define MQTT_WIFI_H

/*-------------------------------------------------------------------------*/

#include "rpcWiFi.h" // lib for wifi
#include "PubSubClient.h" // lib for mqtt
#include "features.h" // custom lib for features
#include "screen.h" // custom lib for tft screen

/*-------------------------------------------------------------------------*/

extern WiFiClient enviroClient; // calling wifi obeject
extern PubSubClient client;  // calling mqtt obejct

extern const char* ssid; // ssid of wifi
extern const char* password; //password of wifi

extern const char* mqttServer; // mqtt server address
extern const int mqttPort; // deafault port for hivemq broker
extern const char* mqttUsername; // username for mqtt broker, not needed if public
extern const char* mqttPassword; // password for mqtt broker, not needed if public
extern const char* mqttMainTopic; // mqtt broker topic
extern const char* mqttClientId; // mqtt client id
extern const char* mqttSubTempUnit; // mqtt sub topic for temp. unit conversion
extern const char* mqttRoomBuzzer; // mqtt sub topic for buzzer

/*-------------------------------------------------------------------------*/

extern void connectWiFi(); // connect to wifi function 

extern void connectMQTT(); // connect to mqtt server function

extern void returnConnection(); // ensuring connection is restored function

extern void publishToMQTT(float temp, float humi, int loud); // publshing sensor values function

extern void callback(char* topic, byte* payload, unsigned int length); // handles messages recieved from mqtt function

#endif