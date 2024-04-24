#include "TFT_eSPI.h"
#include "DHT.h"
#include "rpcWiFi.h"
#include "MQTT.h"

/********************************************************/

const char* ssid = "ssid";
const char* password =  "password";

const char* mqttServer = "broker.hivemq.com"; //HiveMQ server address
const int mqttPort = 1883; // Default MQTT port
const char* mqttUsername = ""; // username for public HiveMQ broker
const char* mqttPassword = ""; // password for public HiveMQ broker
const char* mqttTopic = "envirobaby/";

/********************************************************/

#define DHTPIN 4       // Define signal Pin of DHT
#define DHTTYPE DHT11  // Define DHT Sensor Type

DHT dht(DHTPIN, DHTTYPE); // Initialize DHT sensor
TFT_eSPI tft; // Initialize TFT display
WiFiClient espClient;
MQTTClient client;

/********************************************************/

void setup() {
  Serial.begin(115200);
  while(!Serial); // Wait for Serial to be ready

  // Set WiFi to station mode and disconnect from an AP if it was previously connected
  WiFi.mode(WIFI_STA);
  //WiFi.disconnect();

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }
  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // prints out the device's IP address

  while (!Serial)
    delay(10);

  tft.begin(); // Start TFT display
  tft.setRotation(3); // Rotate the display to match Wio Terminal orientation
  tft.fillScreen(TFT_NAVY); // Clear the screen

  dht.begin(); // Start DHT sensor

  client.begin(mqttServer, mqttPort, espClient);
  client.connect("WioTerminal", mqttUsername, mqttPassword);
}

void loop() {
  float temperature = dht.readTemperature(); // Read temperature
  float humidity = dht.readHumidity();    // Read humidity


  if (client.connected()) {
    // Publish temperature in desired format
    String temperatureMsg = "Temperature: " + String(temperature) + " C";
    client.publish(String(mqttTopic) + "temp", temperatureMsg.c_str());
    Serial.println("Temperature Sent");

    // Publish humidity in desired format
    String humidityMsg = "Humidity: " + String(humidity) + " %";
    client.publish(String(mqttTopic) + "humi", humidityMsg.c_str());
    Serial.println("Humidity Sent");

  } else {
    Serial.println("MQTT Disconnected");
  }

  if (isnan(temperature) || isnan(humidity)) { // Check if any reading failed
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  tft.fillScreen(TFT_NAVY); // Clear the screen

  // Display "ENVIROBABY" text at the top middle part
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth("ENVIROBABY")) / 2, 10);
  tft.println("ENVIROBABY");

  // Draw three little boxes for temperature, humidity, and loudness
  int boxWidth = tft.width() - 20; // Adjusted to leave some margin
  int boxHeight = 50;
  int startX = 10; // Adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  // Draw temperature box
  drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temperature) + " C");

  // Draw humidity box
  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humidity) + " %");

  delay(1000); // Delay for readability
}

void drawValueBox(int x, int y, int width, int height, String label, String value) {
  int borderRadius = 10;
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // Draw the box with rounded corners
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5); // Adjust the position for the text
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2); // Adjust the position for the text
  tft.println(value);
}