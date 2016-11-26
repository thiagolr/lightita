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

// update the strip
void showStrip() {
  strip.show();
}

// set the brightness
void setBrightness(uint8_t value) {
  strip.setBrightness(value);
}

// set a single pixel
void setPixel(uint16_t pixel, uint8_t red, uint8_t green, uint8_t blue) {
  setPixel(pixel, strip.Color(red, green, blue));
}

// set a single pixel
void setPixel(uint16_t pixel, uint32_t color) {
  strip.setPixelColor(pixel, color);
}

// set all pixels
void setAll(uint8_t red, uint8_t green, uint8_t blue, uint16_t startIndex, uint16_t endIndex) {
  setAll(strip.Color(red, green, blue), startIndex, endIndex);
}

// set all pixels
void setAll(uint32_t color, uint16_t startIndex, uint16_t endIndex) {
  for (uint16_t i = startIndex; i < endIndex; i++) {
    setPixel(i, color);
  }
}

// red component
uint8_t colorRed(uint32_t color) {
  return (color >> 16) & 0xFF;
}

// green component
uint8_t colorGreen(uint32_t color) {
  return (color >> 8) & 0xFF;
}

// blue component
uint8_t colorBlue(uint32_t color) {
  return color & 0xFF;
}

// get a random color
uint32_t colorRandom() {
  switch (random(6)) {
  case 0:
    return 0xFF0000;
  case 1:
    return 0x00FF00;
  case 2:
    return 0x0000FF;
  case 3:
    return 0xFFFF00;
  case 4:
    return 0xFF00FF;
  case 5:
    return 0x00FFFF;
  }
  return 0xFFFFFF;
}

// get a rainbow color
uint32_t colorWheel(uint8_t pos) {
  pos = 255 - pos;
  if (pos < 85) {
    return strip.Color(255 - pos * 3, 0, pos * 3);
  }
  if (pos < 170) {
    pos -= 85;
    return strip.Color(0, pos * 3, 255 - pos * 3);
  }
  pos -= 170;
  return strip.Color(pos * 3, 255 - pos * 3, 0);
}

// #########################################################################################################
// #########################################################################################################

// set the pixels one after the other with single color
void wipeColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait) {
#ifdef PRINT
  Serial.println("wipeColor");
#endif
  for (uint16_t i = startIndex; i < endIndex; i++) {
    setPixel(forward == 1 ? i : endIndex - (i - startIndex) - 1, color);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// set the pixels one after the other with single color on all rows simultaneously
void wipeColorDouble(uint32_t color, uint16_t startIndex1, uint16_t endIndex1, uint16_t startIndex2, uint16_t endIndex2, uint16_t startIndex3, uint16_t endIndex3, uint8_t forward1, uint8_t forward2, uint8_t forward3, uint16_t wait) {
#ifdef PRINT
  Serial.println("wipeColorDouble");
#endif
  for (uint16_t i = startIndex1, j = startIndex2, k = startIndex3; i < endIndex1 && j < endIndex2 && k < endIndex3; i++, j++, k++) {
    setPixel(forward1 == 1 ? i : endIndex1 - (i - startIndex1) - 1, color);
    setPixel(forward2 == 1 ? j : endIndex2 - (j - startIndex2) - 1, color);
    setPixel(forward3 == 1 ? k : endIndex3 - (k - startIndex3) - 1, color);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// theater-style crawling lights with single color
void theaterColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("theaterColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, color);
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// theater-style crawling lights with single color on all rows simultaneously
void theaterColorDouble(uint32_t color, uint16_t startIndex1, uint16_t endIndex1, uint16_t startIndex2, uint16_t endIndex2, uint16_t startIndex3, uint16_t endIndex3, uint8_t forward1, uint8_t forward2, uint8_t forward3, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("theaterColorDouble");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
        setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, color);
        setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, color);
        setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, color);
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
        setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, 0);
        setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, 0);
        setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// theater-style crawling lights with rainbow colors
void theaterRainbow(uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait, uint16_t length) {
#ifdef PRINT
  Serial.println("theaterRainbow");
#endif
  for (uint16_t j = 0; j < 256; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, colorWheel((i + j) % 255));
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// linear rainbow colors
void rainbowLinear(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("rainbowLinear");
#endif
  for (uint16_t k = 0; k <= repeat; k++) {
    for (uint16_t j = 0; j < 256; j++) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setPixel(i, colorWheel((i + j) & 255));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// cyclic rainbow colors
void rainbowCycle(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("rainbowCycle");
#endif
  for (uint16_t k = 0; k < repeat; k++) {
    for (uint16_t j = 0; j < 256; j++) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setPixel(i, colorWheel(((i * 256 / (endIndex - startIndex)) + j) & 255));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// static single color
void staticColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  if (count != 0) {
    Serial.println("staticColor");
  }
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t i = startIndex; i < endIndex; i++) {
      setPixel(i, color);
    }
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// static random color
void staticRandom(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("staticRandom");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t i = startIndex; i < endIndex; i++) {
      setPixel(i, colorRandom());
    }
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// twinkle effect with single color
void twinkleColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint16_t count, uint8_t single, uint8_t repeat) {
#ifdef PRINT
  Serial.println("twinkleColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(0, startIndex, endIndex);
    for (uint16_t i = 0; i < count; i++) {
      setPixel(startIndex + random(endIndex - startIndex), color);
      showStrip();
      delay(wait);
      if (single == 1) {
        setAll(0, startIndex, endIndex);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// twinkle effect with random color
void twinkleRandom(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint16_t count, uint8_t single, uint8_t repeat) {
#ifdef PRINT
  Serial.println("twinkleRandom");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(0, startIndex, endIndex);
    for (uint16_t i = 0; i < count; i++) {
      setPixel(startIndex + random(endIndex - startIndex), colorRandom());
      showStrip();
      delay(wait);
      if (single == 1) {
        setAll(0, startIndex, endIndex);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// single color fade in
void fadeInColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("fadeInColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t k = 0; k < 256; k++) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setAll(color, startIndex, endIndex);
      }
      setBrightness(k);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        setBrightness(brightness);
        return;
      }
    }
  }
  setBrightness(brightness);
}

// single color fade out
void fadeOutColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("fadeOutColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t k = 255; k >= 0; k--) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setAll(color, startIndex, endIndex);
      }
      setBrightness(k);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        setBrightness(brightness);
        return;
      }
    }
  }
  setBrightness(brightness);
}

// strobe effect with single color and constant delay
void strobeColorConstant(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("strobeColorConstant");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(color, startIndex, endIndex);
    showStrip();
    delay(wait);
    setAll(0, startIndex, endIndex);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
  delay(wait * 4);
}

// strobe effect with random color and constant delay
void strobeRandomConstant(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("strobeRandomConstant");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(colorRandom(), startIndex, endIndex);
    showStrip();
    delay(wait);
    setAll(0, startIndex, endIndex);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
  delay(wait * 4);
}

// strobe effect with single color and random delay
void strobeColorCrazy(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("strobeColorCrazy");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(color, startIndex, endIndex);
    showStrip();
    delay(10 + random(wait));
    setAll(0, startIndex, endIndex);
    showStrip();
    delay(10 + random(wait));

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
  delay(wait * 4);
}

// strobe effect with random color and random delay
void strobeRandomCrazy(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("strobeRandomCrazy");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(colorRandom(), startIndex, endIndex);
    showStrip();
    delay(10 + random(wait));
    setAll(0, startIndex, endIndex);
    showStrip();
    delay(10 + random(wait));

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
  delay(wait * 4);
}

// sparkle effect with dark background and single color
void sparkleColorDark(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("sparkleColorDark");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(strip.Color(0, 0, 15), startIndex, endIndex);
    showStrip();

    for (uint16_t k = 0; k < length; k++) {
      uint16_t pixel = random(endIndex - startIndex);
      uint32_t original = strip.getPixelColor(startIndex + pixel);

      setPixel(startIndex + pixel, color);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }

    setAll(strip.Color(0, 0, 15), startIndex, endIndex);
    showStrip();
  }
}

// sparkle effect with light background and single color
void sparkleColorLight(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("sparkleColorLight");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    setAll(strip.Color(100, 100, 100), startIndex, endIndex);
    showStrip();

    for (uint16_t k = 0; k < length; k++) {
      uint16_t pixel = random(endIndex - startIndex);
      uint32_t original = strip.getPixelColor(startIndex + pixel);

      setPixel(startIndex + pixel, color);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }

    setAll(strip.Color(100, 100, 100), startIndex, endIndex);
    showStrip();
  }
}

// bounce effect with single color
void bounceColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("bounceColor");
#endif
  for (uint16_t k = 0; k < repeat; k++) {
    for (uint16_t i = startIndex; i < endIndex - length - 2; i++) {
      setAll(0, startIndex, endIndex);
      setPixel(i, colorRed(color) / 10, colorGreen(color) / 10, colorBlue(color) / 10);
      for (uint16_t j = 1; j <= length; j++) {
        setPixel(i + j, color);
      }
      setPixel(i + length + 1, colorRed(color) / 10, colorGreen(color) / 10, colorBlue(color) / 10);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
    delay(wait);

    for (uint16_t i = endIndex - length - 2; i > 0; i--) {
      setAll(0, startIndex, endIndex);
      setPixel(i, colorRed(color) / 10, colorGreen(color) / 10, colorBlue(color) / 10);
      for (uint16_t j = 1; j <= length; j++) {
        setPixel(i + j, color);
      }
      setPixel(i + length + 1, colorRed(color) / 10, colorGreen(color) / 10, colorBlue(color) / 10);
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
    delay(wait);
  }
}

// running effect with single color
void runningColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("runningColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    uint16_t pos = 0;
    for (uint16_t i = startIndex; i < endIndex * 2; i++)  {
      pos++;
      for (uint16_t i = 0; i < endIndex; i++) {
        setPixel(i, ((sin(i + pos) * 127 + 128) / 255) * colorRed(color), ((sin(i + pos) * 127 + 128) / 255) * colorGreen(color), ((sin(i + pos) * 127 + 128) / 255) * colorBlue(color));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// running effect with random color
void runningRandom(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("runningRandom");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    uint16_t pos = 0;
    for (uint16_t i = startIndex; i < endIndex * 2; i++)  {
      pos++;
      for (uint16_t i = 0; i < endIndex; i++) {
        setPixel(i, ((sin(i + pos) * 127 + 128) / 255) * random(256), ((sin(i + pos) * 127 + 128) / 255) * random(256), ((sin(i + pos) * 127 + 128) / 255) * random(256));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

