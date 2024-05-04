#include "TFT_eSPI.h"
#include "DHT.h"
#include "mqtt_wifi.h"

/*******************Define Pin and Objects*******************************/

#define LOUDNESS_PIN A0  // pin for loudness sensor
#define DHTPIN A4        // pin of DHT
#define DHTTYPE DHT11    // DHT sensor type

#define BUZZER_PIN WIO_BUZZER

unsigned long buzzerStartTime = 0;

bool buzzerRoom1Active = false;
bool buzzerRoom2Active = false;
bool buzzerRoom3Active = false;
bool buzzerRoom4Active = false;

String room1BuzzerCommand;
String room2BuzzerCommand;
String room3BuzzerCommand;
String room4BuzzerCommand;

DHT dht(DHTPIN, DHTTYPE);  // initialize DHT sensor
TFT_eSPI tft;              // initialize TFT display

WiFiClient enviroClient;            // calling wifi obeject
PubSubClient client(enviroClient);  // calling mqtt obejct

/***********************Setup******************************/

void setup() {
  Serial.begin(115200); // setting baund as per amount of data needed to be transmitted
  while (!Serial); // wait till the serial is ready

  WiFi.mode(WIFI_STA);  // set wifi to station mode
  WiFi.disconnect();    // disconnect any previous wifi connection

  connectWiFi();             // connecting to wifi
  tft.begin();               // starting TFT display
  tft.setRotation(1);        // rotate the display to match wio terminal orientation
  tft.fillScreen(TFT_NAVY);  // clear the screen

  dht.begin();  // starting DHT sensor

  pinMode(BUZZER_PIN, OUTPUT);

  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  connectMQTT();  // connecting to MQTT
}


/***********************Loop******************************/

void loop() {
  float temp = dht.readTemperature();   // read temperature
  float humi = dht.readHumidity();      // read humidity
  int loud = analogRead(LOUDNESS_PIN);  // read loudness

  if (!WiFi.isConnected() || !client.connected()) {  // check if wifi and mqtt is connected
    reconnect();                                     // not connected? reconnect!
  }

  client.loop();

  if (client.connected()) {  // if connected to mqtt broker publish temp, humi, loud

    String temperatureMsg = String(temp);
    String humidityMsg = String(humi);
    String loudnessMsg = String(loud);

    String topicTemp = String(mqttTopic) + "temp";
    String topicHumi = String(mqttTopic) + "humi";
    String topicLoud = String(mqttTopic) + "loud";

    client.publish(topicTemp.c_str(), temperatureMsg.c_str());
    Serial.println("Temperature Sent");

    client.publish(topicHumi.c_str(), humidityMsg.c_str());
    Serial.println("Humidity Sent");

    client.publish(topicLoud.c_str(), loudnessMsg.c_str());
    Serial.println("Loudness Sent");

  } else {
    connectMQTT();  // if disconnected again, try to reconnect
  }

  if (isnan(temp) || isnan(humi)) {  // Check if any reading failed for DHT sensor
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  tft.fillScreen(TFT_NAVY);  // clear the screen

  // display ENVIROBABY text at the top middle part
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth(projectTITLE)) / 2, 10);  // set the text position
  tft.println(projectTITLE);

  // draw three little boxes for temperature, humidity, and loudness
  int boxWidth = tft.width() - 20;  // adjusted to leave some margin
  int boxHeight = 50;
  int startX = 10;  // adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C");  // Draw temperature box

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %");  // Draw humidity box

  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db");  // Draw loudness box


  if (room1BuzzerCommand.equals("BUZZ")) {
    if (!buzzerRoom1Active) {
      buzzerStartTime = millis(); // set the start time for the buzzer
    buzzerRoom1Active = true; // set the boolean to true so that this if statement is not retriggered
      tone(BUZZER_PIN, 1000); // starts the buzzing noise
    }
    // this if statement checks whether 10 seconds have passed.
    if (millis() - buzzerStartTime >= 10000) { // millis() - buzzerStartTime so that it can check if 10 seconds have passed within an infinite time set
      noTone(BUZZER_PIN); // buzzer stops after 10 seconds have passed
      buzzerRoom1Active = false; // set the boolean to false to indicate the buzzer is not buzzing
      room1BuzzerCommand = "STOP"; // sets the command to STOP so that this if statement is not retriggered
    }
  } else if (room1BuzzerCommand.equals("STOP")) {
    noTone(BUZZER_PIN); // stops the buzzer if "STOP" message is received
    buzzerRoom1Active = false; // sets the boolean to false to indicate the buzzer is not buzzing
  }

  // if (room2BuzzerCommand.equals("BUZZ")) {
  //   if (!buzzerRoom2Active) {
  //     buzzerStartTime = millis(); // set the start time for the buzzer
  //     buzzerRoom2Active = true; // set the boolean to true so that this if statement is not retriggered
  //     tone(BUZZER_PIN, 1000); // starts the buzzing noise
  //   }
  //   // this if statement checks whether 10 seconds have passed.
  //   if (millis() - buzzerStartTime >= 10000) { // millis() - buzzerStartTime so that it can check if 10 seconds have passed within an infinite time set
  //     noTone(BUZZER_PIN); // buzzer stops after 10 seconds have passed
  //     buzzerRoom2Active = false; // set the boolean to false to indicate the buzzer is not buzzing
  //     room2BuzzerCommand = "STOP"; // sets the command to STOP so that this if statement is not retriggered
  //   }
  // } else if (room2BuzzerCommand.equals("STOP")) {
  //   noTone(BUZZER_PIN); // stops the buzzer if "STOP" message is received
  //   buzzerRoom2Active = false; // sets the boolean to false to indicate the buzzer is not buzzing
  // }

  // if (room3BuzzerCommand.equals("BUZZ")) {
  //   if (!buzzerRoom3Active) {
  //     buzzerStartTime = millis(); // set the start time for the buzzer
  //     buzzerRoom3Active = true; // set the boolean to true so that this if statement is not retriggered
  //     tone(BUZZER_PIN, 1000); // starts the buzzing noise
  //   }
  //   // this if statement checks whether 10 seconds have passed.
  //   if (millis() - buzzerStartTime >= 10000) { // millis() - buzzerStartTime so that it can check if 10 seconds have passed within an infinite time set
  //     noTone(BUZZER_PIN); // buzzer stops after 10 seconds have passed
  //     buzzerRoom3Active = false; // set the boolean to false to indicate the buzzer is not buzzing
  //     room3BuzzerCommand = "STOP"; // sets the command to STOP so that this if statement is not retriggered
  //   }
  // } else if (room3BuzzerCommand.equals("STOP")) {
  //   noTone(BUZZER_PIN); // stops the buzzer if "STOP" message is received
  //   buzzerRoom3Active = false; // sets the boolean to false to indicate the buzzer is not buzzing
  // }

  // if (room4BuzzerCommand.equals("BUZZ")) {
  //   if (!buzzerRoom4Active) {
  //     buzzerStartTime = millis(); // set the start time for the buzzer
  //     buzzerRoom4Active = true; // set the boolean to true so that this if statement is not retriggered
  //     tone(BUZZER_PIN, 1000); // starts the buzzing noise
  //   }
  //   // this if statement checks whether 10 seconds have passed.
  //   if (millis() - buzzerStartTime >= 10000) { // millis() - buzzerStartTime so that it can check if 10 seconds have passed within an infinite time set
  //     noTone(BUZZER_PIN); // buzzer stops after 10 seconds have passed
  //     buzzerRoom4Active = false; // set the boolean to false to indicate the buzzer is not buzzing
  //     room4BuzzerCommand = "STOP"; // sets the command to STOP so that this if statement is not retriggered
  //   }
  // } else if (room4BuzzerCommand.equals("STOP")) {
  //   noTone(BUZZER_PIN); // stops the buzzer if "STOP" message is received
  //   buzzerRoom4Active = false; // sets the boolean to false to indicate the buzzer is not buzzing
  // }


  delay(1000);  // 1 sec delay according to acceptance criteria
}

/***********************Methods******************************/

void connectWiFi() {  // connect ot wifi

  Serial.println("Connecting to WiFi..");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
}


void connectMQTT() {
  // establish connection with mqtt broker
  Serial.println("Connecting to MQTT..");
  while (!client.connected()) {
    // Attempt to connect to the MQTT broker

    if (client.connect(mqttClientId)) {
      client.subscribe(mqttRoom1Buzzer);
      client.subscribe(mqttRoom2Buzzer);
      client.subscribe(mqttRoom3Buzzer);
      client.subscribe(mqttRoom4Buzzer);
      Serial.println("Connected to MQTT broker");
    } else {
      Serial.println("MQTT Connection failed. Trying again in 5 seconds...");
      delay(5000);
    }
  }
}

void reconnect() {  // reconnect wifi and mqtt
  Serial.println("Reconnecting to WiFi and MQTT..");
  connectWiFi();  // calling the wifi connect method
  connectMQTT();  // calling the mqtt connect method
}

void drawValueBox(int x, int y, int width, int height, String label, String value) {  // method for small boxes that contain the info for perameters
  int borderRadius = 10;                                                              // rounds the corner of box
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW);                   // draw the box with rounded corners
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5);  // adjust the position for the text
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2);  // adjust the position for the text
  tft.println(value);
}

void callback(char* topic, byte* payload, unsigned int length) {

  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length];
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i];
  }
  Serial.println();
  buff_p[length] = '\0';
  String msg_p = String(buff_p);

  if (strcmp(topic, mqttRoom1Buzzer) == 0) {
    room1BuzzerCommand = msg_p;
  } else if (strcmp(topic, mqttRoom2Buzzer) == 0) {
    room2BuzzerCommand = msg_p;
  } else if (strcmp(topic, mqttRoom3Buzzer) == 0) {
    room3BuzzerCommand = msg_p;
  } else if (strcmp(topic, mqttRoom4Buzzer) == 0) {
    room4BuzzerCommand = msg_p;
  }
}
