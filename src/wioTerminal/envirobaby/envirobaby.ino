/************************************************************************
#SOURCES USED IN THE PROJECT:

callback function inspired by: https://www.hackster.io/Salmanfarisvp/mqtt-on-wio-terminal-4ea8f8
wifi and mqtt function inspired by: https://youtu.be/5tG3JXFYrUo?si=w6-r8EEOvKpCsA0b
buzzer code inspired by: https://wiki.seeedstudio.com/Wio-Terminal-Buzzer/
code refactorig inspired by: https://github.com/FalckJoshua/DIT113-System-Development-Locus-Imperium, https://github.com/michalspano/terminarium/

************************************************************************/

#include "DHT.h" // lib for dht sensor
#include "mqtt_wifi.h" // custom lib for mqtt connect
#include "screen.h" // custom lib for tft screen
#include "features.h" // custom lib for features
#include "wio_pins.h" // custom lib for pin

/*-------------------------------------------------------------------------*/

DHT dht(DHTPIN, DHTTYPE);  // initialize DHT sensor
TFT_eSPI tft;              // initialize TFT display object

<<<<<<< HEAD
/*-------------------------------------------------------------------------*/
=======
WiFiClient enviroClient;            // calling wifi object
PubSubClient client(enviroClient);  // calling mqtt object


/***********************Setup******************************/
>>>>>>> 663c7510bd98567a699032a7ca3fa28f2cdd02f0

void setup() {
  Serial.begin(115200); // setting baund as per amount of data needed to be transmitted
  while (!Serial); // wait till the serial is ready

  connectWiFi(); // connect to wifi
  tft.begin(); // starting TFT display
  tft.setRotation(1); // rotate the display to match wio terminal orientation
  tft.fillScreen(TFT_NAVY); // clear the screen

  dht.begin();  // starting DHT sensor

  pinMode(BUZZER_PIN, OUTPUT); // setting pin as output

  client.setServer(mqttServer, mqttPort); // setting mqtt broker server and port
  client.setCallback(callback); // setting callback method for mqtt messages

  connectMQTT(); // connect to mqtt server
}


/*-------------------------------------------------------------------------*/

void loop() {

  float temp = dht.readTemperature(); // read temperature
  float humi = dht.readHumidity();    // read humidity
  int loud = analogRead(LOUDNESS_PIN); // read loudness
  
  client.loop(); // maintains connection with mqtt

  returnConnection(); // ensuring connection is restored

  publishToMQTT(temp, humi, loud); // publshing sensor values

  updateScreen(temp, humi, loud); // updating screen with sensor values

  buzzRoom(); // buzzer funtion
}
<<<<<<< HEAD
=======

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
  client.subscribe(mqttRoom1Buzzer);
  client.subscribe(mqttRoom2Buzzer);
  client.subscribe(mqttRoom3Buzzer);
  client.subscribe(mqttRoom4Buzzer);

  while (!client.connected()) {
    // Attempt to connect to the MQTT broker
    client.connect(mqttClientId);
    client.subscribe(mqttSubTempUnit);
    client.subscribe(mqttRoom1Buzzer);
    client.subscribe(mqttRoom2Buzzer);
    client.subscribe(mqttRoom3Buzzer);
    client.subscribe(mqttRoom4Buzzer);
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

  if (isnan(temp) || isnan(humi)) {  // Check if any reading failed for DHT sensor
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

  if(converToFahren){ // if true the temp unit converts to fahrenheit and pubslishes to mqtt
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    client.publish(topicTemp.c_str(), temperatureMsgFahren.c_str());
    Serial.println("Temperature Sent");
  } else { // else temp unit is and pubslished as celsius to mqtt
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
  tft.setCursor((tft.width() - tft.textWidth(projectTITLE)) / 2, 10);  // set the text position
  tft.println(projectTITLE);

  // draw three little boxes for temperature, humidity and loudness
  int boxWidth = tft.width() - 20;  // adjusted to leave some margin
  int boxHeight = 50;
  int startX = 10;  // adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp);
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temperatureMsgFahren) + " F"); // Draw temperature box
  } else {
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C"); // Draw temperature box
  }

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %");  // Draw humidity box

  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db");  // Draw loudness box

  if (room1BuzzerCommand.equals("BUZZ")) {
    if (!buzzerRoom1Active) {
      buzzerStartTime = millis(); // set the start time for the buzzer
    buzzerRoom1Active = true; // set the boolean to true so that this if statement is not retriggered
      tone(BUZZER_PIN, 2000); // starts the buzzing noise
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


/***********************wio boxes value******************************/

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

/***********************mqtt callback******************************/

void callback(char* topic, byte* payload, unsigned int length) {  // callback method in order to receive messages from mqtt

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

  if(strcmp(topic, mqttSubTempUnit) == 0){
    if(String(msg_p).equals("F")){
      converToFahren = true;
    } else if (String(msg_p).equals("C")){
      converToFahren = false;
    }
  }

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

/***********************Convert To Fahrenheit******************************/

String convertAndPublishToFahrenheit(float temp) {  // method that converts celsius to fahrenheit
  float fahrenheit = (temp * 9 / 5) + 32;
  return String(fahrenheit);
}
>>>>>>> 663c7510bd98567a699032a7ca3fa28f2cdd02f0
