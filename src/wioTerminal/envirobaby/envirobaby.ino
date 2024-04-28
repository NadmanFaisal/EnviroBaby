#include "TFT_eSPI.h"
#include "DHT.h"
#include "mqtt_wifi.h"

/*******************Define Pin and Objects*******************************/

#define LOUDNESS_PIN A0 // pin for loudness sensor
#define DHTPIN A4       // pin of DHT
#define DHTTYPE DHT11  // DHT sensor type

DHT dht(DHTPIN, DHTTYPE); // initialize DHT sensor
TFT_eSPI tft; // initialize TFT display

WiFiClient espClient; // calling wifi obeject
MQTTClient client; // calling mqtt obejct

/***********************Setup******************************/

void setup() {
  Serial.begin(115200); // setting baund as per amount of data needed to be transmitted
  while (!Serial); // wait till the serial is ready

  WiFi.mode(WIFI_STA); // set wifi to station mode
  WiFi.disconnect(); // disconnect any previous wifi connection

  connectWiFi(); // connecting to wifi
  tft.begin(); // starting TFT display
  tft.setRotation(1); // rotate the display to match wio terminal orientation
  tft.fillScreen(TFT_RED); // clear the screen

  dht.begin(); // starting DHT sensor

  connectMQTT(); // connecting to MQTT
}


/***********************Loop******************************/

void loop() {
  float temp = dht.readTemperature(); // read temperature
  float humi = dht.readHumidity();    // read humidity
  int loud = analogRead(LOUDNESS_PIN); // read loudness
  
  if (!WiFi.isConnected() || !client.connected()) { // check if wifi and mqtt is connected
    reconnect(); // not connected? reconnect!
  }

  if (client.connected()) { // if connected to mqtt broker publish temp, humi, loud

    String temperatureMsg = "Temperature: " + String(temp) + " C";
    client.publish(String(mqttTopic) + "temp", temperatureMsg.c_str());
    Serial.println("Temperature Sent");

    String humidityMsg = "Humidity: " + String(humi) + " %"; // if connected to mqtt broker publish humidity
    client.publish(String(mqttTopic) + "humi", humidityMsg.c_str());
    Serial.println("Humidity Sent");

    String loudnessMsg = "Loudness: " + String(loud) + " db"; // if connected to mqtt broker publish loudness
    client.publish(String(mqttTopic) + "loud", loudnessMsg.c_str());
    Serial.println("Loudness Sent");
  } else {
    connectMQTT(); // if disconnected again, try to reconnect
  }

  if (isnan(temp) || isnan(humi)) { // Check if any reading failed for DHT sensor
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

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

  drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C"); // Draw temperature box

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %"); // Draw humidity box
  
  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db"); // Draw loudness box

  delay(1000); // 1 sec delay according to acceptance criteria
}

/***********************Methods******************************/

void connectWiFi() { // connect ot wifi
  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(3000);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
}


void connectMQTT() { // establish connection with mqtt broker
  Serial.println("Connecting to MQTT..");
  client.begin(mqttServer, mqttPort, espClient);
  while (!client.connect(mqttClientId, mqttUsername, mqttPassword)) {
    // if (!WiFi.isConnected()){
    // connectWiFi();
    // }
    Serial.println("MQTT Connection failed. Trying again in 5 seconds...");
    delay(5000);
  }
  Serial.println("Connected to MQTT broker");
}


void reconnect() { // reconnect wifi and mqtt
  Serial.println("Reconnecting to WiFi and MQTT..");
  connectWiFi(); // calling the wifi connect method
  connectMQTT(); // calling the mqtt connect method
}

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

