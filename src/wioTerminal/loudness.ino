#include "rpcWiFi.h" //wifi library
#include "MQTT.h" //mqtt library
#include "TFT_eSPI.h"

#define LOUDNESS_PIN A0 // Pin in the terminal for loudness sensor
TFT_eSPI tft; // initialized variable for LCD terminal display

/********************************************************/

const char* ssid = "enterSSID"; // wifi SSID
const char* password =  "enterPASS"; // wifi password

const char* mqttServer = "broker.hivemq.com"; // hivemq mqtt server address
const int mqttPort = 1883; // hivemq mqtt port
const char* mqttUsername = ""; // username for public hivemq broker
const char* mqttPassword = ""; // password for public hivemq broker
const char* mqttTopic = "envirobaby/"; // topic for hivemq broker

/********************************************************/

WiFiClient espClient; //declaring wifi object
MQTTClient client; //declaring mqtt object

/********************************************************/

const char* TITLE = "ENVIROBABY";

void setup() {
  Serial.begin(115200);
  while(!Serial); // waiting for serial monitor to open

  WiFi.mode(WIFI_STA); // set WiFi mode to station (client)
  WiFi.disconnect(); // disconnect any previous wifi connection

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password); // connecting to wifi

  while (WiFi.status() != WL_CONNECTED) { // waiting until wifi is being connected
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }
  Serial.println("Connected to the WiFi network"); 
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // prints wifi ip address

  while (!Serial)
    delay(10);

  tft.begin();
  tft.setRotation(3);
  tft.fillScreen(TFT_NAVY);

  client.begin(mqttServer, mqttPort, espClient); // initialize mqtt client
  client.connect("WioTerminalClient", mqttUsername, mqttPassword); // connect to the MQTT server
}

void loop() {
  int loudness = analogRead(LOUDNESS_PIN); // Read loudness

  if (client.connected()) {
    String loudnessValue = String(loudness) + " db";
    client.publish(String(mqttTopic) + "loud", loudnessValue.c_str()); // publish loudness value to mqtt topict
    Serial.println("Loudness Sent");
  }else {
    Serial.println("MQTT Disconnected");
  }

  tft.fillScreen(TFT_NAVY); // This method clears the screen


  // Title displays at the top
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth(TITLE)) / 2, 10);
  tft.println(TITLE);

  // Draw box for loudness
  int boxWidth = tft.width() - 20;
  int boxHeight = 50;
  int startX = 10;
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  // Loudness section
  drawValueBox(startX, startY, boxWidth, boxHeight, "Loudness", String(loudness) + " db");

  delay(1000); // updates every 1 second.
}

void drawValueBox(int x, int y, int width, int height, String label, String value) {
  int borderRadius = 10;
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // Round corner
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5);
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2);
  tft.println(value);
}
