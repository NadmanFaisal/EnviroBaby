#include "TFT_eSPI.h" // lcd library
#include "rpcWiFi.h" //wifi library
#include "MQTT.h" //mqtt library

/********************************************************/

const char* ssid = "nadmans batcave"; // wifi SSID
const char* password =  "0736773532"; // wifi password

/********************************************************/

const char* mqttServer = "broker.hivemq.com"; // hivemq mqtt server address
const int mqttPort = 1883; // hivemq mqtt port
const char* mqttUsername = ""; // username for public hivemq broker
const char* mqttPassword = ""; // password for public hivemq broker
const char* mqttTopic = "envirobaby"; // topic for hivemq broker 

/********************************************************/

WiFiClient espClient; //declaring wifi object
MQTTClient client; //declaring mqtt object

/********************************************************/

TFT_eSPI tft = TFT_eSPI(); // Invoke custom library for tft display
int loudness;

/********************************************************/

void setup() {
    Serial.begin(9600); //begin serial communication with baud rate 9600
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

  tft.init(); // initialize the  display
  tft.setRotation(3); // rotate display if necessary
  tft.fillScreen(TFT_BLACK); // fill screen, black colour

    
  tft.setTextColor(TFT_GREEN); // set text color
  tft.setTextSize(2); // set text size

  client.begin(mqttServer, mqttPort, espClient); // initialize mqtt client
  client.connect("WioTerminalClient", mqttUsername, mqttPassword); // connect to the MQTT server
}

/********************************************************/

void loop() {

    loudness = analogRead(0); // read analog input from pin A0

  if (client.connected()) { 
  String loudnessValue = String(loudness) + " db";
  client.publish(mqttTopic, loudnessValue.c_str()); // publish loudness value to mqtt topict
  Serial.println("Loudness Sent");
  }else {
  Serial.println("MQTT Disconnected"); 
}

  Serial.println(loudness); // print loudness db to serial monitor

    
    tft.fillRect(0, 0, tft.width(), tft.height(), TFT_BLACK); // clear previous text

    
    tft.setCursor(0, 0); 
    tft.print("Decibel: "); // print loudness value
    tft.println(loudness);

    
    delay(200); // delay for smooth visualization
}