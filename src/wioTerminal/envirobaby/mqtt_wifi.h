#ifndef MQTT_WIFI_H                                        
#define MQTT_WIFI_H

#include "rpcWiFi.h"
#include "MQTT.h"

extern const char* ssid; // ssid of wifi
extern const char* password; //password of wifi

extern const char* mqttServer; // mqtt server address
extern const int mqttPort; // deafault port for hivemq broker
extern const char* mqttUsername; // username for mqtt broker, not needed if public
extern const char* mqttPassword; // password for mqtt broker, not needed if public
extern const char* mqttTopic; // mqtt broker topic
extern const char* mqttClientId;
extern const char* projectTITLE;  // project title

#endif