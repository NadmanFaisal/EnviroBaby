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

/*-------------------------------------------------------------------------*/

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
