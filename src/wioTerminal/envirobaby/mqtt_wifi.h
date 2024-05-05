#ifndef MQTT_WIFI_H                                        
#define MQTT_WIFI_H

#include "rpcWiFi.h"
#include "PubSubClient.h"

extern const char* ssid; // ssid of wifi
extern const char* password; //password of wifi

extern const char* mqttServer; // mqtt server address
extern const int mqttPort; // deafault port for hivemq broker
extern const char* mqttUsername; // username for mqtt broker, not needed if public
extern const char* mqttPassword; // password for mqtt broker, not needed if public
extern const char* mqttTopic; // mqtt broker topic
extern const char* mqttClientId; // mqtt client id
extern const char* projectTITLE;  // project title
extern const char* mqttSubTempUnit; // mqtt sub topic for temp. unit conversion
extern const char* mqttRoom1Buzzer;
extern const char* mqttRoom2Buzzer;
extern const char* mqttRoom3Buzzer;
extern const char* mqttRoom4Buzzer;

#endif