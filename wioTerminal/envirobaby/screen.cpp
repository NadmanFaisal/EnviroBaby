#include "screen.h" // custom lib for tft screen
#include "features.h" // custom lib for features

bool converToFahren = false; // flag for temperature unit
const char* projectTITLE = "ENVIROBABY";  // project title

/*-------------------------------------------------------------------------*/

// updating screen with sensor values function
void updateScreen(float temp, float humi, int loud) {

  tft.fillScreen(TFT_NAVY); // clear the screen

  // display 'ENVIROBABY' title at the top middle part of screen
  tft.setTextSize(3); // set text size
  tft.setTextColor(TFT_WHITE); // set text color
  tft.setCursor((tft.width() - tft.textWidth(projectTITLE)) / 2, 10);  // set the text position
  tft.println(projectTITLE); // printing project title in screen

  // draw three little boxes for temperature, humidity, and loudness
  int boxWidth = tft.width() - 20;  // adjusted to leave some margin
  int boxHeight = 50; // height of the box
  int startX = 10;  // adjusted to leave some margin
  int startY = (tft.height() - (boxHeight * 3)) / 2; //  centering boxes vertically

  // draw the boxes with containing sensor values 
  if(converToFahren){
    String temperatureMsgFahren = convertAndPublishToFahrenheit(temp); // converting temp. to fahrenheit
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temperatureMsgFahren) + " F"); // draw temperature box in fahrenheit
  } else {
    drawValueBox(startX, startY, boxWidth, boxHeight, "Temperature", String(temp) + " C"); // draw temperature box in celcius
  }

  drawValueBox(startX, startY + boxHeight + 10, boxWidth, boxHeight, "Humidity", String(humi) + " %");  // draw humidity box

  drawValueBox(startX, startY + 2 * (boxHeight + 10), boxWidth, boxHeight, "Loudness", String(loud) + " db");  // draw loudness box

  delay(1000);  // 1 sec delay according to acceptance criteria
}

/*-------------------------------------------------------------------------*/

// draw value box function
void drawValueBox(int x, int y, int width, int height, String label, String value) {  // method for small boxes that contain the info for sensor values
  int borderRadius = 10; // rounds the corner of box
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // draw the box with rounded corners
  tft.setTextSize(2); // set text size
  tft.setTextColor(TFT_BLACK); // set text color
  tft.setCursor(x + 10, y + 5);  // adjust the position for the text
  tft.println(label); // priting the label
  tft.setCursor(x + 10, y + (height + 5) / 2);  // adjust the position for the text
  tft.println(value); // print the recieved value
}