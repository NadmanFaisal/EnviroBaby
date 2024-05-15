#include "features.h" // custom lib for features

bool buzzerRoomActive = false; // flag for buzzer 
unsigned long buzzerStartTime = 0; // store the start time for buzzer
String roomBuzzerCommand; // store commands for room buzzer

/*-------------------------------------------------------------------------*/

// activate or stop the buzzer function
void buzzRoom() {
  if (roomBuzzerCommand.equals("BUZZ")) {
    if (!buzzerRoomActive) {
      buzzerStartTime = millis(); // set the start time for the buzzer
      buzzerRoomActive = true; // set the boolean to true so that this if statement is not retriggered
      tone(BUZZER_PIN, 2000); // starts the buzzing noise
    }
    // this if statement checks whether 10 seconds have passed.
    if (millis() - buzzerStartTime >= 10000) { // millis() - buzzerStartTime so that it can check if 10 seconds have passed within an infinite time set
      noTone(BUZZER_PIN); // buzzer stops after 10 seconds have passed
      buzzerRoomActive = false; // set the boolean to false to indicate the buzzer is not buzzing
      roomBuzzerCommand = "STOP"; // sets the command to STOP so that this if statement is not retriggered
      }
    } else if (roomBuzzerCommand.equals("STOP")) {
      noTone(BUZZER_PIN); // stops the buzzer if "STOP" message is received
      buzzerRoomActive = false; // sets the boolean to false to indicate the buzzer is not buzzing
  }
}

/*-------------------------------------------------------------------------*/

// convert temperature value to fahrenheit function
String convertAndPublishToFahrenheit(float temp) { 
  float fahrenheit = (temp * 9 / 5) + 32;
  return String(fahrenheit);
}