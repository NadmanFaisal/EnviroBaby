#include "TFT_eSPI.h" //LCD library
#include "DHT.h" // DHT library
#include <rpcWiFi.h> //wifi library
#include "MQTT.h" //mqtt library

/********************************************************/

const char* ssid = "iPhone (5)";
const char* password =  "evhb010hikc5";

const char* mqttServer = "broker.hivemq.com"; // HiveMQ MQTT server address
const int mqttPort = 1883; // HiveMQ MQTT port
const char* mqttUsername = ""; // username for public HiveMQ broker
const char* mqttPassword = ""; // password for public HiveMQ broker

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


  WiFi.mode(WIFI_STA);
  WiFi.disconnect();

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password); 
  }
  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // print device's IP address

  while (!Serial)
    delay(10);

  tft.begin(); // Start TFT display 
  tft.setRotation(3); // Rotate the display to match Wio Terminal orientation
  tft.fillScreen(TFT_BLACK); // Clear the screen

  dht.begin(); // Start DHT sensor 

  client.begin(mqttServer, mqttPort, espClient);
  client.connect("WioTerminalClient", mqttUsername, mqttPassword);

}

void loop() {

  float t = dht.readTemperature(); // Read temperature
  float h = dht.readHumidity();    // Read humidity

  if (client.connected()) {
  // Publish temperature 
  String temperatureMsg = "Temperature: " + String(t) + " C";
  client.publish("envirobaby/temp", temperatureMsg.c_str());
  Serial.println("Temperature Sent");

  // Publish humidity 
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