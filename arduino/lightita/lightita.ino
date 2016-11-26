/*
  Copyright (c) 2016 Thiago Lopes Rosa

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#include <Adafruit_NeoPixel.h>
#include <SoftwareSerial.h>

#define LED_A
//#define LED_B

//#define PRINT

// LED STRIP A
#ifdef LED_A
#define NUM_LEDS 108

#define LED_STRIP_A1_START 0
#define LED_STRIP_A1_SIZE 54
#define LED_STRIP_A1_DIRECTION -1
#define LED_STRIP_A2_START 54
#define LED_STRIP_A2_SIZE 54
#define LED_STRIP_A2_DIRECTION -1
#define LED_STRIP_A3_START 108
#define LED_STRIP_A3_SIZE 54
#define LED_STRIP_A3_DIRECTION -1
#endif

// LED STRIP B
#ifdef LED_B
#define NUM_LEDS 82

#define LED_STRIP_B1_START 0
#define LED_STRIP_B1_SIZE 41
#define LED_STRIP_B1_DIRECTION 1
#define LED_STRIP_B2_START 41
#define LED_STRIP_B2_SIZE 41
#define LED_STRIP_B2_DIRECTION 1
#define LED_STRIP_B3_START 82
#define LED_STRIP_B3_SIZE 41
#define LED_STRIP_B3_DIRECTION 1
#endif

// BRIGHTNESS
#define BRIGHTNESS 39

// COMMANDS
#define MAX_COMMANDS 100
#define COMMAND_CLEAR 0
#define COMMAND_UNDO 1
#define COMMAND_COLOR 2
#define COMMAND_WIPE 3
#define COMMAND_THEATER 4
#define COMMAND_RAINBOW 5
#define COMMAND_STATIC 6
#define COMMAND_TWINKLE 7
#define COMMAND_FADE 8
#define COMMAND_STROBE 9
#define COMMAND_SPARKLE 10
#define COMMAND_BOUNCE 11
#define COMMAND_RUNNING 12
#define COMMAND_BRIGHTNESS 13

// PINS
#define PIN_BLUETOOTH_RX 10
#define PIN_BLUETOOTH_TX 11
#define PIN_LEDS 6

// #########################################################################################################
// #########################################################################################################

SoftwareSerial Bluetooth(10, 11); // RX, TX

Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_LEDS, PIN_LEDS, NEO_RGB + NEO_KHZ800);
uint8_t brightness = BRIGHTNESS;
uint32_t color = 0;

uint8_t startIndex = 0;
uint8_t endIndex = 0;
uint8_t directionIndex = 0;
uint8_t indexValue = 0;
uint8_t typeValue = 0;
uint8_t speedValue = 0;
uint8_t lengthValue = 0;
uint8_t repeatValue = 0;

byte command[MAX_COMMANDS];
byte param1[MAX_COMMANDS];
byte param2[MAX_COMMANDS];
byte param3[MAX_COMMANDS];
byte param4[MAX_COMMANDS];
byte param5[MAX_COMMANDS];
byte count = 0;
int debug = 0;

// #########################################################################################################
// #########################################################################################################

void setup() {

#ifdef PRINT
  // setup the serial module
  Serial.begin(9600);
#endif

  // setup the bluetooth serial module
  Bluetooth.begin(9600);

  // setup the bluetooth module
  pinMode(PIN_BLUETOOTH_RX, INPUT_PULLUP);

  // setup the random seed
  randomSeed(analogRead(0));

  // setup the strip
  strip.begin();
  strip.setBrightness(BRIGHTNESS);
  strip.show();

  // initialize the commands
  for (int i = 0; i < MAX_COMMANDS; i++) {
    command[i] = 0;
    param1[i] = 0;
    param2[i] = 0;
    param3[i] = 0;
    param4[i] = 0;
    param5[i] = 0;
  }
  count = 0;

  // add initial effect
  command[count] = COMMAND_RAINBOW;
  param1[count] = 4;
  param2[count] = 2;
  param3[count] = 0;
  param4[count] = 0;
  param5[count] = 1;
  count++;

}

void process() {
  while (Bluetooth.available() >= 6)  {

    // read the command
    byte type = Bluetooth.read();
    byte read1 = Bluetooth.read();
    byte read2 = Bluetooth.read();
    byte read3 = Bluetooth.read();
    byte read4 = Bluetooth.read();
    byte read5 = Bluetooth.read();

#ifdef PRINT
    switch (type) {
    case COMMAND_CLEAR:
      Serial.println("COMMAND_CLEAR");
      break;
    case COMMAND_UNDO:
      Serial.println("COMMAND_UNDO");
      break;
    case COMMAND_BRIGHTNESS:
      Serial.println("COMMAND_BRIGHTNESS");
      break;
    case COMMAND_COLOR:
      Serial.println("COMMAND_COLOR");
      break;
    case COMMAND_WIPE:
      Serial.println("COMMAND_WIPE");
      break;
    case COMMAND_THEATER:
      Serial.println("COMMAND_THEATER");
      break;
    case COMMAND_RAINBOW:
      Serial.println("COMMAND_RAINBOW");
      break;
    case COMMAND_STATIC:
      Serial.println("COMMAND_STATIC");
      break;
    case COMMAND_TWINKLE:
      Serial.println("COMMAND_TWINKLE");
      break;
    case COMMAND_FADE:
      Serial.println("COMMAND_FADE");
      break;
    case COMMAND_STROBE:
      Serial.println("COMMAND_STROBE");
      break;
    case COMMAND_SPARKLE:
      Serial.println("COMMAND_SPARKLE");
      break;
    case COMMAND_BOUNCE:
      Serial.println("COMMAND_BOUNCE");
      break;
    case COMMAND_RUNNING:
      Serial.println("COMMAND_RUNNING");
      break;
    default:
      Serial.println("UNKNOWN");
      break;
    }

    Serial.println(read1);
    Serial.println(read2);
    Serial.println(read3);
    Serial.println(read4);
    Serial.println(read5);
#endif

    switch (type) {
    case COMMAND_CLEAR:
      for (int i = 0; i < MAX_COMMANDS; i++) {
        command[i] = 0;
        param1[i] = 0;
        param2[i] = 0;
        param3[i] = 0;
        param4[i] = 0;
        param5[i] = 0;
      }
      count = 0;
      break;
    case COMMAND_UNDO:
      count = count - 2;
      if (count < 0) {
        count = 0;
      }
      break;
    case COMMAND_BRIGHTNESS:
      brightness = read1;
      strip.setBrightness(brightness);
      break;
    case COMMAND_COLOR:
    case COMMAND_WIPE:
    case COMMAND_THEATER:
    case COMMAND_RAINBOW:
    case COMMAND_STATIC:
    case COMMAND_TWINKLE:
    case COMMAND_FADE:
    case COMMAND_STROBE:
    case COMMAND_SPARKLE:
    case COMMAND_BOUNCE:
    case COMMAND_RUNNING:
      command[count] = type;
      param1[count] = read1;
      param2[count] = read2;
      param3[count] = read3;
      param4[count] = read4;
      param5[count] = read5;
      count++;
      break;
    }
  }
}

void loop() {

  // process the bluetooth commands
  process();

  // if no command available, turn off all the leds
  if (count == 0) {
#ifdef PRINT
    Serial.println("no effects");
#endif

    // turn off all pixels
    staticColor(0, 0, NUM_LEDS, 2000, 1);

    //wipeColor(0xFF0000, 0, 54, getDirection(1, 0, 1), 100);
    //theaterColor(0xFF0000, 0, NUM_LEDS, 1, 100, 5, 5);
    //rainbowLinear(0, NUM_LEDS, 0, 1);
    //rainbowCycle(0, NUM_LEDS, 100, 1);
    //staticColor(0XFF0000, 0, NUM_LEDS, 1000, 1);
    //bounceColor(0xFF0000, 0, NUM_LEDS, 200, 4, 1);
    //twinkleColor(0xFF0000, 0, NUM_LEDS, 200, 4, 0, 1);
    //sparkleColorDark(0xFF0000, 0, NUM_LEDS, 200, 10, 1);
    //sparkleColorLight(0xFF0000, 0, NUM_LEDS, 200, 10, 1);
    //strobeColorConstant(0xFF0000, 0, NUM_LEDS, 30, 5);
    //strobeRandomConstant(0, NUM_LEDS, 30, 5);
    //runningColor(0xFF0000, 0, NUM_LEDS, 50, 1);
  }

  // execute all the commands available
  for (int s = 0; s < count; s++) {

    // stop if a new command is available
    if (Bluetooth.available() >= 6)  {
      break;
    }

    // parameters
    indexValue = param1[s];
    typeValue = param2[s];
    speedValue = param3[s];
    lengthValue = param4[s];
    repeatValue = param5[s];

    // the start index
    startIndex = getStartIndex(indexValue, 0);

    // the end index
    endIndex = getEndIndex(indexValue, 0);

    // the direction index
    directionIndex = getDirection(indexValue, 0, typeValue);

    // execute the specific command
    switch (command[s]) {
    case COMMAND_COLOR:
      color = strip.Color(indexValue, typeValue, speedValue);
      break;
    case COMMAND_WIPE:
    case COMMAND_THEATER:
      switch (indexValue) {
      case 1:
      case 2:
      case 3:
        switch (command[s]) {
        case COMMAND_WIPE:
          wipeColor(color, startIndex, endIndex, directionIndex, speedValue);
          break;
        case COMMAND_THEATER:
          theaterColor(color, startIndex, endIndex, directionIndex, speedValue, lengthValue, repeatValue);
          break;
        }
        break;
      case 4:
        switch (command[s]) {
        case COMMAND_WIPE:
          wipeColorDouble(color, getStartIndex(indexValue, 1), getEndIndex(indexValue, 1), getStartIndex(indexValue, 2), getEndIndex(indexValue, 2), getStartIndex(indexValue, 3), getEndIndex(indexValue, 3), getDirection(indexValue, 1, typeValue), getDirection(indexValue, 2, typeValue), getDirection(indexValue, 3, typeValue), speedValue);
          break;
        case COMMAND_THEATER:
          if (typeValue == 0 || typeValue == 1) {
            theaterColorDouble(color, getStartIndex(indexValue, 1), getEndIndex(indexValue, 1), getStartIndex(indexValue, 2), getEndIndex(indexValue, 2), getStartIndex(indexValue, 3), getEndIndex(indexValue, 3), getDirection(indexValue, 1, typeValue), getDirection(indexValue, 2, typeValue), getDirection(indexValue, 3, typeValue), speedValue, lengthValue, repeatValue);
          }
          else if (typeValue == 2 || typeValue == 3) {
            theaterRainbow(startIndex, endIndex, directionIndex, speedValue, lengthValue);
          }
          break;
        }
        break;
      }
      break;
    case COMMAND_RAINBOW:
      switch (typeValue) {
      case 1:
        rainbowLinear(startIndex, endIndex, speedValue, repeatValue);
        break;
      case 2:
        rainbowCycle(startIndex, endIndex, speedValue, repeatValue);
        break;
      }
      break;
    case COMMAND_STATIC:
      switch (typeValue) {
      case 1:
        staticColor(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      case 2:
        staticRandom(startIndex, endIndex, speedValue, repeatValue);
        break;
      }
      break;
    case COMMAND_RUNNING:
      switch (typeValue) {
      case 1:
        runningColor(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      case 2:
        runningRandom(startIndex, endIndex, speedValue, repeatValue);
        break;
      }
      break;
    case COMMAND_FADE:
      switch (typeValue) {
      case 1:
        fadeInColor(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      case 2:
        fadeOutColor(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      }
      break;
    case COMMAND_TWINKLE:
      switch (typeValue) {
      case 1:
        twinkleColor(color, startIndex, endIndex, speedValue, lengthValue, typeValue == 1 ? 1 : 0, repeatValue);
        break;
      case 2:
        twinkleColor(color, startIndex, endIndex, speedValue, lengthValue, typeValue == 1 ? 1 : 0, repeatValue);
        break;
      case 3:
        twinkleRandom( startIndex, endIndex, speedValue, lengthValue, typeValue == 3 ? 1 : 0, repeatValue);
        break;
      case 4:
        twinkleRandom( startIndex, endIndex, speedValue, lengthValue, typeValue == 3 ? 1 : 0, repeatValue);
        break;
      }
      break;
    case COMMAND_STROBE:
      switch (typeValue) {
      case 1:
        strobeColorConstant(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      case 2:
        strobeColorCrazy(color, startIndex, endIndex, speedValue, repeatValue);
        break;
      case 3:
        strobeRandomConstant(startIndex, endIndex, speedValue, repeatValue);
        break;
      case 4:
        strobeRandomCrazy(startIndex, endIndex, speedValue, repeatValue);
        break;
      }
      break;
    case COMMAND_SPARKLE:
      switch (typeValue) {
      case 1:
        sparkleColorDark(color, startIndex, endIndex, speedValue, lengthValue, repeatValue);
        break;
      case 2:
        sparkleColorLight(color, startIndex, endIndex, speedValue, lengthValue, repeatValue);
        break;
      }
      break;
    case COMMAND_BOUNCE:
      bounceColor(color, startIndex, endIndex, speedValue, lengthValue, repeatValue);
      break;
    }
  }
}

// #########################################################################################################
// #########################################################################################################

// the start index, based on the index and subindex
uint16_t getStartIndex(uint8_t index, uint8_t subindex) {
  switch (index) {
#ifdef LED_A
  case 1:
    return LED_STRIP_A1_START;
  case 2:
    return LED_STRIP_A2_START;
  case 3:
    return LED_STRIP_A3_START;
  case 4:
    if (subindex == 1) {
      return LED_STRIP_A1_START;
    }
    else if (subindex == 2) {
      return LED_STRIP_A2_START;
    }
    else if (subindex == 3) {
      return LED_STRIP_A3_START;
    }
    else {
      return LED_STRIP_A1_START;
    }
#endif
#ifdef LED_B
  case 1:
    return LED_STRIP_B1_START;
  case 2:
    return LED_STRIP_B2_START;
  case 3:
    return LED_STRIP_B3_START;
  case 4:
    if (subindex == 1) {
      return LED_STRIP_B1_START;
    }
    else if (subindex == 2) {
      return LED_STRIP_B2_START;
    }
    else if (subindex == 3) {
      return LED_STRIP_B3_START;
    }
    else {
      return LED_STRIP_B1_START;
    }
#endif
  }
  return 0;
}

// the end index, based on the index and subindex
uint16_t getEndIndex(uint8_t index, uint8_t subindex) {
  switch (index) {
#ifdef LED_A
  case 1:
    return LED_STRIP_A1_START + LED_STRIP_A1_SIZE;
  case 2:
    return LED_STRIP_A2_START + LED_STRIP_A2_SIZE;
  case 3:
    return LED_STRIP_A3_START + LED_STRIP_A3_SIZE;
  case 4:
    if (subindex == 1) {
      return LED_STRIP_A1_START + LED_STRIP_A1_SIZE;
    }
    else if (subindex == 2) {
      return LED_STRIP_A2_START + LED_STRIP_A2_SIZE;
    }
    else if (subindex == 3) {
      return LED_STRIP_A3_START + LED_STRIP_A3_SIZE;
    }
    else {
      return LED_STRIP_A1_START + LED_STRIP_A1_SIZE + LED_STRIP_A2_SIZE + LED_STRIP_A3_SIZE;
    }
#endif
#ifdef LED_B
  case 1:
    return LED_STRIP_B1_START + LED_STRIP_B1_SIZE;
  case 2:
    return LED_STRIP_B2_START + LED_STRIP_B2_SIZE;
  case 3:
    return LED_STRIP_B3_START + LED_STRIP_B3_SIZE;
  case 4:
    if (subindex == 1) {
      return LED_STRIP_B1_START + LED_STRIP_B1_SIZE;
    }
    else if (subindex == 2) {
      return LED_STRIP_B2_START + LED_STRIP_B2_SIZE;
    }
    else if (subindex == 3) {
      return LED_STRIP_B3_START + LED_STRIP_B3_SIZE;
    }
    else {
      return LED_STRIP_B1_START + LED_STRIP_B1_SIZE + LED_STRIP_B2_SIZE + LED_STRIP_B3_SIZE;
    }
#endif
  }
  return NUM_LEDS;
}

// the direction, based on the index, subindex and forward value
uint16_t getDirection(uint8_t index, uint8_t subindex, uint8_t forward) {
  int mult = (forward == 1 || forward == 3) ? 1 : -1;
  switch (index) {
#ifdef LED_A
  case 1:
    return mult * LED_STRIP_A1_DIRECTION;
  case 2:
    return mult * LED_STRIP_A2_DIRECTION;
  case 3:
    return mult * LED_STRIP_A3_DIRECTION;
  case 4:
    if (subindex == 1) {
      return mult * LED_STRIP_A1_DIRECTION;
    }
    else if (subindex == 2) {
      return mult * LED_STRIP_A2_DIRECTION;
    }
    else if (subindex == 3) {
      return mult * LED_STRIP_A3_DIRECTION;
    }
    else {
      return mult;
    }
#endif
#ifdef LED_B
  case 1:
    return mult * LED_STRIP_B1_DIRECTION;
  case 2:
    return mult * LED_STRIP_B2_DIRECTION;
  case 3:
    return mult * LED_STRIP_B3_DIRECTION;
  case 4:
    if (subindex == 1) {
      return mult * LED_STRIP_B1_DIRECTION;
    }
    else if (subindex == 2) {
      return mult * LED_STRIP_B2_DIRECTION;
    }
    else if (subindex == 3) {
      return mult * LED_STRIP_B3_DIRECTION;
    }
    else {
      return mult;
    }
#endif
  }
  return mult;
}

