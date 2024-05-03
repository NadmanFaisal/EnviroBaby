#include "TFT_eSPI.h"
#include "DHT.h"
#include "mqtt_wifi.h"

/*******************Define Pin and Objects*******************************/

#define LOUDNESS_PIN A0 // pin for loudness sensor
#define DHTPIN A4       // pin of DHT
#define DHTTYPE DHT11  // DHT sensor type

DHT dht(DHTPIN, DHTTYPE); // initialize DHT sensor
TFT_eSPI tft; // initialize TFT display

WiFiClient enviroClient; // calling wifi obeject
PubSubClient client(enviroClient); // calling mqtt obejct

bool converToFahren = false;

/***********************Setup******************************/

void setup() {
  Serial.begin(115200); // setting baund as per amount of data needed to be transmitted
  while (!Serial); // wait till the serial is ready

  //WiFi.mode(WIFI_STA); // set wifi to station mode
  //WiFi.disconnect(); // disconnect any previous wifi connection

  //connectWiFi(); // connecting to wifi
  tft.begin(); // starting TFT display
  tft.setRotation(1); // rotate the display to match wio terminal orientation
  tft.fillScreen(TFT_NAVY); // clear the screen

  dht.begin(); // starting DHT sensor


  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  //connectMQTT(); // connecting to MQTT
}


/***********************Loop******************************/

void loop() {

  float temp = dht.readTemperature(); // read temperature
  float humi = dht.readHumidity();    // read humidity
  int loud = analogRead(LOUDNESS_PIN); // read loudness
  
  
  client.loop();
  returnConnection();
  publishToMQTT(temp, humi, loud);
  updateScreen(temp, humi, loud);
  
}

/***********************Methods******************************/

void connectWiFi() { // connect ot wifi

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(2000);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
  
}


/***********************MQTT******************************/

void connectMQTT() { 
  // establish connection with mqtt broker
  Serial.println("Connecting to MQTT...");
  client.connect(mqttClientId);
  client.subscribe(mqttSubTempUnit);

  while (!client.connected()) {
    // Attempt to connect to the MQTT broker
    client.connect(mqttClientId);
    client.subscribe(mqttSubTempUnit);
    Serial.println("Connected to MQTT broker");
  }
    delay(3000);
}


/***********************reconnect******************************/

void returnConnection() { // reconnect wifi and mqtt

  if (!WiFi.isConnected() || !client.connected()) { // check if wifi and mqtt is connected
    Serial.println("Reconnecting to WiFi and MQTT..");
    connectWiFi(); // calling the wifi connect method
    connectMQTT(); // calling the mqtt connect method
  }
}


/***********************PUBLISH******************************/

void publishToMQTT(float temp, float humi, int loud) {

  if (isnan(temp) || isnan(humi)) { // Check if any reading failed for DHT sensor
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  if (client.connected()) { // if connected to mqtt broker publish temp, humi, loud
  String temperatureMsg = String(temp);
  String humidityMsg = String(humi);
  String loudnessMsg = String(loud);

  String topicTemp = String(mqttTopic) + "temp";
  String topicHumi = String(mqttTopic) + "humi";
  String topicLoud = String(mqttTopic) + "loud";

  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    client.publish(topicTemp.c_str(), temperatureMsgFahren.c_str());
    Serial.println("Temperature Sent");
  } else {
    client.publish(topicTemp.c_str(), temperatureMsg.c_str());
    Serial.println("Temperature Sent");
  }

  client.publish(topicHumi.c_str(), humidityMsg.c_str());
  Serial.println("Humidity Sent");
    
  client.publish(topicLoud.c_str(), loudnessMsg.c_str());
  Serial.println("Loudness Sent");
  } else {
    connectMQTT(); // if disconnected again, try to reconnect
  }

}


/***********************wio terminal sreen******************************/

void updateScreen(float temp, float humi, int loud) {

  tft.fillScreen(TFT_NAVY); // clear the screen

  // display ENVIROBABY text at the top middle part
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth(projectTITLE)) / 2, 10); // set the text position
  tft.println(projectTITLE);

  // draw three little boxes for temperature, humidity, and loudness
  int boxWidth = tft.width() - 20; // adjusted to leave some margin
  int boxHeight = 50;
  int startX = 10; // adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temperatureMsgFahren) + " F"); // Draw temperature box
  } else {
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C"); // Draw temperature box
  }

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %"); // Draw humidity box
  
  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db"); // Draw loudness box

  delay(1000); // 1 sec delay according to acceptance criteria

}


/***********************wio boxes value******************************/

void drawValueBox(int x, int y, int width, int height, String label, String value) { // method for small boxes that contain the info for perameters
  int borderRadius = 10; // rounds the corner of box
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // draw the box with rounded corners
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5); // adjust the position for the text
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2); // adjust the position for the text
  tft.println(value);
}


/***********************mqtt callback******************************/

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length];
  for (int i = 0; i < length; i++){
    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i];
  }
  Serial.println();
  buff_p[length] = '\0';
  String msg_p = String(buff_p);

  if(strcmp(topic, mqttSubTempUnit) == 0){
    if(String(msg_p).equals("F")){
      converToFahren = true;
    } else if (String(msg_p).equals("C")){
      converToFahren = false;
    }
  }
}

/***********************Convert To Fahrenheit******************************/

String convertAndPublishToFahrenheit(float temp) {
  float fahrenheit = (temp * 9 / 5) + 32;
  return String(fahrenheit);
}

#include "TFT_eSPI.h"
#include "DHT.h"
#include "mqtt_wifi.h"

/*******************Define Pin and Objects*******************************/

#define LOUDNESS_PIN A0 // pin for loudness sensor
#define DHTPIN A4       // pin of DHT
#define DHTTYPE DHT11  // DHT sensor type

DHT dht(DHTPIN, DHTTYPE); // initialize DHT sensor
TFT_eSPI tft; // initialize TFT display

WiFiClient enviroClient; // calling wifi obeject
PubSubClient client(enviroClient); // calling mqtt obejct

bool converToFahren = false;

/***********************Setup******************************/

void setup() {
  Serial.begin(115200); // setting baund as per amount of data needed to be transmitted
  while (!Serial); // wait till the serial is ready

  //WiFi.mode(WIFI_STA); // set wifi to station mode
  //WiFi.disconnect(); // disconnect any previous wifi connection

  //connectWiFi(); // connecting to wifi
  tft.begin(); // starting TFT display
  tft.setRotation(1); // rotate the display to match wio terminal orientation
  tft.fillScreen(TFT_NAVY); // clear the screen

  dht.begin(); // starting DHT sensor


  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  //connectMQTT(); // connecting to MQTT
}


/***********************Loop******************************/

void loop() {

  float temp = dht.readTemperature(); // read temperature
  float humi = dht.readHumidity();    // read humidity
  int loud = analogRead(LOUDNESS_PIN); // read loudness
  
  
  client.loop();
  returnConnection();
  publishToMQTT(temp, humi, loud);
  updateScreen(temp, humi, loud);
  
}

/***********************Methods******************************/

void connectWiFi() { // connect ot wifi

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(2000);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
  
}


/***********************MQTT******************************/

void connectMQTT() { 
  // establish connection with mqtt broker
  Serial.println("Connecting to MQTT...");
  client.connect(mqttClientId);
  client.subscribe(mqttSubTempUnit);

  while (!client.connected()) {
    // Attempt to connect to the MQTT broker
    client.connect(mqttClientId);
    client.subscribe(mqttSubTempUnit);
    Serial.println("Connected to MQTT broker");
  }
    delay(3000);
}


/***********************reconnect******************************/

void returnConnection() { // reconnect wifi and mqtt

  if (!WiFi.isConnected() || !client.connected()) { // check if wifi and mqtt is connected
    Serial.println("Reconnecting to WiFi and MQTT..");
    connectWiFi(); // calling the wifi connect method
    connectMQTT(); // calling the mqtt connect method
  }
}


/***********************PUBLISH******************************/

void publishToMQTT(float temp, float humi, int loud) {

  if (isnan(temp) || isnan(humi)) { // Check if any reading failed for DHT sensor
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  if (client.connected()) { // if connected to mqtt broker publish temp, humi, loud
  String temperatureMsg = String(temp);
  String humidityMsg = String(humi);
  String loudnessMsg = String(loud);

  String topicTemp = String(mqttTopic) + "temp";
  String topicHumi = String(mqttTopic) + "humi";
  String topicLoud = String(mqttTopic) + "loud";

  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    client.publish(topicTemp.c_str(), temperatureMsgFahren.c_str());
    Serial.println("Temperature Sent");
  } else {
    client.publish(topicTemp.c_str(), temperatureMsg.c_str());
    Serial.println("Temperature Sent");
  }

  client.publish(topicHumi.c_str(), humidityMsg.c_str());
  Serial.println("Humidity Sent");
    
  client.publish(topicLoud.c_str(), loudnessMsg.c_str());
  Serial.println("Loudness Sent");
  } else {
    connectMQTT(); // if disconnected again, try to reconnect
  }

}


/***********************wio terminal sreen******************************/

void updateScreen(float temp, float humi, int loud) {

  tft.fillScreen(TFT_NAVY); // clear the screen

  // display ENVIROBABY text at the top middle part
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth(projectTITLE)) / 2, 10); // set the text position
  tft.println(projectTITLE);

  // draw three little boxes for temperature, humidity, and loudness
  int boxWidth = tft.width() - 20; // adjusted to leave some margin
  int boxHeight = 50;
  int startX = 10; // adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temperatureMsgFahren) + " F"); // Draw temperature box
  } else {
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C"); // Draw temperature box
  }

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %"); // Draw humidity box
  
  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db"); // Draw loudness box

  delay(1000); // 1 sec delay according to acceptance criteria

}


/***********************wio boxes value******************************/

void drawValueBox(int x, int y, int width, int height, String label, String value) { // method for small boxes that contain the info for perameters
  int borderRadius = 10; // rounds the corner of box
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // draw the box with rounded corners
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5); // adjust the position for the text
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2); // adjust the position for the text
  tft.println(value);
}


/***********************mqtt callback******************************/

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length];
  for (int i = 0; i < length; i++){
    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i];
  }
  Serial.println();
  buff_p[length] = '\0';
  String msg_p = String(buff_p);

  if(strcmp(topic, mqttSubTempUnit) == 0){
    if(String(msg_p).equals("F")){
      converToFahren = true;
    } else if (String(msg_p).equals("C")){
      converToFahren = false;
    }
  }
}

/***********************Convert To Fahrenheit******************************/

String convertAndPublishToFahrenheit(float temp) {
  float fahrenheit = (temp * 9 / 5) + 32;
  return String(fahrenheit);
}

