#include "mqtt_wifi.h"

const char* ssid = "ssid"; // ssid of wifi
const char* password = "password"; //password of wifi

const char* mqttServer = "broker.hivemq.com"; // mqtt server address
const int mqttPort = 1883; // deafault port for hivemq broker
const char* mqttUsername = ""; // username for mqtt broker, not needed if public
const char* mqttPassword = ""; // password for mqtt broker, not needed if public
const char* mqttTopic = "envirobaby/"; // mqtt broker topic
extern const char* mqttClientId = "EnviroBabyWIO";
const char* projectTITLE = "ENVIROBABY";  // project title