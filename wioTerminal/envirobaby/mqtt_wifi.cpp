#include "mqtt_wifi.h"

WiFiClient enviroClient;            // calling wifi obeject
PubSubClient client(enviroClient);  // calling mqtt obejct using wifi client

/*-------------------------------------------------------------------------*/

const char* ssid = "ssid"; // ssid of wifi
const char* password = "pass"; //password of wifi

const char* mqttServer = "broker.hivemq.com"; // mqtt server address
const int mqttPort = 1883; // default port for hivemq broker
const char* mqttUsername = ""; // username for mqtt broker, not needed if public
const char* mqttPassword = ""; // password for mqtt broker, not needed if public
const char* mqttMainTopic = "/envirobaby/room1/"; // mqtt broker topic
const char* mqttClientId = "EnviroBabyWIoRoom1"; // mqtt client id
const char* mqttSubTempUnit = "/envirobaby/tempunit"; // mqtt sub topic for temp. unit conversion
const char* mqttRoomBuzzer = "/envirobaby/room1/buzzer"; // mqtt sub topic for buzzer


/*-------------------------------------------------------------------------*/

// connect ot wifi function
void connectWiFi() {

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password); // starting wifi connection

  while (WiFi.status() != WL_CONNECTED) { // keep retrying wifi connection untill conneted
    delay(2000);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password); // retrying wifi connection
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // serial print the ip address
  
}

/*-------------------------------------------------------------------------*/

// establish connection with mqtt broker function
void connectMQTT() { 

  Serial.println("Connecting to MQTT...");
  client.connect(mqttClientId, mqttUsername, mqttPassword); // connects to MQTT broker
  client.subscribe(mqttSubTempUnit); // subscribe to broker for temp. change unit
  client.subscribe(mqttRoomBuzzer); // subscribe to broker for buzzer
  Serial.println("Connected to MQTT broker");
  Serial.println(String("Broker Info- ") + "Server: " + mqttServer + " Port: " + mqttPort); // printing mqtt broker connection info


  while (!client.connected()) { // attempt to connect to the MQTT broker
    client.connect(mqttClientId, mqttUsername, mqttPassword); // connects to MQTT broker
    client.subscribe(mqttSubTempUnit); // subscribe to broker for temp. change unit
    client.subscribe(mqttRoomBuzzer); // subscribe to broker for buzzer
    Serial.println("Connected to MQTT broker");
  }
  delay(3000);
}

/*-------------------------------------------------------------------------*/

// reconnect wifi and mqtt function
void returnConnection() {

  if (!WiFi.isConnected() || !client.connected()) { // check if wifi and mqtt is connected
    Serial.println("Attempting Connection to WiFi and MQTT..");
    connectWiFi(); // calling the wifi connect method
    connectMQTT(); // calling the mqtt connect method
    }
  }

/*-------------------------------------------------------------------------*/

// publshing sensor values function
void publishToMQTT(float temp, float humi, int loud) {

  if (isnan(temp) || isnan(humi)) {  // check if any reading failed for DHT sensor
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  if (client.connected()) { // if connected to mqtt broker publish temp, humi, loud
  
  String temperatureMsg = String(temp); // storting temp value
  String humidityMsg = String(humi); // storting humi value
  String loudnessMsg = String(loud); // storting loud value

  String topicTemp = String(mqttMainTopic) + "temp"; // topic for temperature
  String topicHumi = String(mqttMainTopic) + "humi"; // topic for humidity
  String topicLoud = String(mqttMainTopic) + "loud"; // topic for loudness

  client.publish(topicTemp.c_str(), temperatureMsg.c_str()); // publish temperature values
  Serial.println("Temperature Published");

  client.publish(topicHumi.c_str(), humidityMsg.c_str()); // publish humidity values
  Serial.println("Humidity Published");
  
  client.publish(topicLoud.c_str(), loudnessMsg.c_str()); // publish loudness values
  Serial.println("Loudness Published");
  
  } else {
    Serial.println("MQTT Publishing Failed....."); // print message if mqtt server is not connected
  }
}

/*-------------------------------------------------------------------------*/

// handles messages recieved from mqtt function
void callback(char* topic, byte* payload, unsigned int length) { 
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length]; // buffer that stores the payload
  for (int i = 0; i < length; i++) { // converting payload to string from byte array

    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i]; // store character in buffer

  }
  Serial.println();
  buff_p[length] = '\0'; // null terminate the buffer
  String msg_p = String(buff_p); // convert c-string buffer to string object

  if(strcmp(topic, mqttSubTempUnit) == 0){ // check if the topic matches the tempuni conversion topic

    if(String(msg_p).equals("F")){ // check if payload matches the string
      converToFahren = true; // set flag for ferheinheit conversion
      
      } else if (String(msg_p).equals("C")){ // check if payload matches the string
      converToFahren = false; // set flag for celsius conversion

    }
  }

  if (strcmp(topic, mqttRoomBuzzer) == 0) { // check if the topic matches the buzzer topic
    roomBuzzerCommand = msg_p; // buzzer command

  }
}
