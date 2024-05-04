#include "mqtt_wifi.h"

const char* ssid = "ssid"; // ssid of wifi
const char* password = "passqord"; //password of wifi

const char* mqttServer = "broker.hivemq.com"; // mqtt server address
const int mqttPort = 1883; // deafault port for hivemq broker
const char* mqttUsername = ""; // username for mqtt broker, not needed if public
const char* mqttPassword = ""; // password for mqtt broker, not needed if public
const char* mqttTopic = "envirobaby/"; // mqtt broker topic
const char* mqttClientId = "EnviroBaby"; // mqtt client id
const char* projectTITLE = "ENVIROBABY";  // project title
const char* mqttSubTempUnit = "/envirobaby/tempunit"; // mqtt sub topic for temp. unit conversion
const char* mqttRoom1Buzzer = "/envirobaby/room1/buzzer";
const char* mqttRoom2Buzzer = "/envirobaby/room2/buzzer";
const char* mqttRoom3Buzzer = "/envirobaby/room3/buzzer";
const char* mqttRoom4Buzzer = "/envirobaby/room4/buzzer";
