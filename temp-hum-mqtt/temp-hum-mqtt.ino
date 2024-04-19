#include "TFT_eSPI.h"
#include "DHT.h" // Include DHT library

#include <rpcWiFi.h>
#include "MQTT.h"

/********************************************************/

const char* ssid = "your ssid";
const char* password =  "your password";

const char* mqttServer = "broker.hivemq.com"; // Replace with your MQTT server address
const int mqttPort = 1883; // Default MQTT port
const char* mqttUsername = ""; // No username required for public HiveMQ broker
const char* mqttPassword = ""; // No password required for public HiveMQ broker

/********************************************************/
#define DHTPIN 0       // Define signal Pin of DHT
#define DHTTYPE DHT11  // Define DHT Sensor Type

DHT dht(DHTPIN, DHTTYPE); // Initialize DHT sensor

TFT_eSPI tft; // Initialize TFT display

/********************************************************/

WiFiClient espClient;
MQTTClient client;

/********************************************************/

void setup() {
  Serial.begin(115200);
  while(!Serial); // Wait for Serial to be ready

  // Set WiFi to station mode and disconnect from an AP if it was previously connected
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password); //OK
  }
  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // prints out the device's IP address

  while (!Serial)
    delay(10);

  tft.begin(); // Start TFT display OK
  tft.setRotation(3); // Rotate the display to match Wio Terminal orientation
  tft.fillScreen(TFT_BLACK); // Clear the screen

  dht.begin(); // Start DHT sensor OK

  client.begin(mqttServer, mqttPort, espClient);
  client.connect("WioTerminalClient", mqttUsername, mqttPassword);

}

void loop() {

  float t = dht.readTemperature(); // Read temperature
  float h = dht.readHumidity();    // Read humidity

  if (client.connected()) {
  // Publish temperature in desired format
  String temperatureMsg = "Temperature: " + String(t) + " C";
  client.publish("envirobaby/temp", temperatureMsg.c_str());
  Serial.println("Temperature Sent");

  // Publish humidity in desired format
  String humidityMsg = "Humidity: " + String(h) + " %";
  client.publish("envirobaby/hum", humidityMsg.c_str());
  Serial.println("Humidity Sent");
} else {
  Serial.println("MQTT Disconnected");
}

  if (isnan(t) || isnan(h)) { // Check if any reading failed
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  tft.fillScreen(TFT_BLACK); // Clear the screen
  tft.setCursor(0, 0);       // Set cursor to top-left corner
  tft.setTextSize(2);         // Set text size

  // Display temperature and humidity on the TFT display
  tft.setTextColor(TFT_WHITE);
  tft.println("Temperature:");
  tft.setTextColor(TFT_GREEN);
  tft.print(t);
  tft.println("C");
  tft.setTextColor(TFT_WHITE);
  tft.println("Humidity:");
  tft.setTextColor(TFT_BLUE);
  tft.print(h);
  tft.println("%");

  delay(5000); // Delay for readability
}