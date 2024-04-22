#include "TFT_eSPI.h"

#define LOUDNESS_PIN A0 // Pin in the terminal for loudness sensor
TFT_eSPI tft; // initialized variable for LCD terminal display

const char* TITLE = "ENVIROBABY";

void setup() {
  Serial.begin(115200);

  while (!Serial)
    delay(10);

  tft.begin();
  tft.setRotation(3);
  tft.fillScreen(TFT_NAVY);
}

void loop() {
  int loudness = analogRead(LOUDNESS_PIN); // Read loudness

  tft.fillScreen(TFT_NAVY); // This method clears the screen

  // Title displays at the top
  tft.setTextSize(3);
  tft.setTextColor(TFT_WHITE);
  tft.setCursor((tft.width() - tft.textWidth(TITLE)) / 2, 10);
  tft.println(TITLE);

  // Draw box for loudness
  int boxWidth = tft.width() - 20;
  int boxHeight = 50;
  int startX = 10;
  int startY = (tft.height() - (boxHeight * 3)) / 2;

  // Loudness section
  drawValueBox(startX, startY, boxWidth, boxHeight, "Loudness", String(loudness) + " db");

  delay(1000); // updates every 1 second.
}

void drawValueBox(int x, int y, int width, int height, String label, String value) {
  int borderRadius = 10;
  tft.fillRoundRect(x, y, width, height, borderRadius, TFT_YELLOW); // Round corner
  tft.setTextSize(2);
  tft.setTextColor(TFT_BLACK);
  tft.setCursor(x + 10, y + 5);
  tft.println(label);
  tft.setCursor(x + 10, y + (height + 5) / 2);
  tft.println(value);
}