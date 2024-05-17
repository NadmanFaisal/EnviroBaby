#ifndef FEATURES_H                                        
#define FEATURES_H
#include "Arduino.h" // lib for core arduino lib (need for String)
#include "wio_pins.h" // custom lib for pin

/*-------------------------------------------------------------------------*/

extern bool buzzerRoomActive; // flag for buzzer 
extern unsigned long buzzerStartTime; // store the start time for buzzer
extern String roomBuzzerCommand; // store commands for room buzzer

/*-------------------------------------------------------------------------*/

extern void buzzRoom(); // activate or stop the buzzer function

extern String convertAndPublishToFahrenheit(float temp); // convert temperature value to fahrenheit function

#endif