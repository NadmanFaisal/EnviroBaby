#ifndef SCREEN_H                                        
#define SCREEN_H

#include "TFT_eSPI.h" // lib for tft screen

/*-------------------------------------------------------------------------*/

extern bool converToFahren; // flag for temperature unit
extern const char* projectTITLE;  // project title

extern TFT_eSPI tft; // tft display object

/*-------------------------------------------------------------------------*/

extern void updateScreen(float temp, float humi, int loud); // updating screen with sensor values function

extern void drawValueBox(int x, int y, int width, int height, String label, String value); // draw value box on the screen function

#endif

