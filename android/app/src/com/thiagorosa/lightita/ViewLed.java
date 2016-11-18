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

package com.thiagorosa.lightita;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.thiagorosa.lightita.model.Effect;
import com.thiagorosa.lightita.model.EffectLogic;
import com.thiagorosa.lightita.model.Led;

import java.util.ArrayList;

import static com.thiagorosa.lightita.model.EffectLogic.TYPE_BOUNCE;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_FADE;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_RAINBOW;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_RUNNING;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_SPARKLE;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_STATIC;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_STROBE;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_THEATER;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_TWINKLE;
import static com.thiagorosa.lightita.model.EffectLogic.TYPE_WIPE;

public class ViewLed extends View {

    private static final int NUM_ROWS = 3;
    private static final int NUM_COLUMNS = 1;
    private static final int NUM_LEDS = 24;

    private static final int LED_STRIP_A1_START = 0;
    private static final int LED_STRIP_A1_SIZE = 24;
    private static final int LED_STRIP_A1_DIRECTION = 1;
    private static final int LED_STRIP_A2_START = 24;
    private static final int LED_STRIP_A2_SIZE = 24;
    private static final int LED_STRIP_A2_DIRECTION = 1;
    private static final int LED_STRIP_A3_START = 48;
    private static final int LED_STRIP_A3_SIZE = 24;
    private static final int LED_STRIP_A3_DIRECTION = 1;

    private static final int COLOR_BACKGROUND = 0xFF666666;

    private ArrayList<Led> mLeds = new ArrayList<>();
    private ArrayList<Effect> mSequences = null;
    private EffectLogic mEffectLogic = null;
    private Thread mThread = null;
    private Paint mPaintLed = null;
    private boolean isRunning = false;

    private int mCellWidth = 0;
    private int mCellHeight = 0;
    private int mCellMargin = 0;
    private int mLedDiameter = 0;

    /*******************************************************************************************
     *******************************************************************************************/

    public ViewLed(Context context) {
        super(context);
    }

    public ViewLed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewLed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewLed(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public void load(ArrayList<Effect> sequence) {
        mEffectLogic = new EffectLogic(mLeds);
        mSequences = sequence;

        mLeds.clear();
        for (int i = 0; i < NUM_LEDS * NUM_ROWS * NUM_COLUMNS; i++) {
            mLeds.add(new Led(Color.BLACK));
        }

        mPaintLed = new Paint(Paint.ANTI_ALIAS_FLAG);

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean isDouble = false;
                int startIndex = 0;
                int endIndex = 0;
                int direction = 0;

                isRunning = true;
                while (isRunning) {
                    try {
                        if (mSequences.size() == 0) {
                            for (int i = 0; i < NUM_LEDS * NUM_ROWS * NUM_COLUMNS; i++) {
                                mLeds.get(i).setColor(Color.BLACK);
                            }
                        }

                        for (Effect sequence : mSequences) {
                            switch (sequence.getIndex()) {
                                case 1:
                                case 2:
                                case 3:
                                    isDouble = false;
                                    break;
                                case 4:
                                    isDouble = true;
                                    break;
                            }

                            startIndex = getStartIndex(sequence.getIndex(), 0);
                            endIndex = getEndIndex(sequence.getIndex(), 0);
                            direction = getDirection(sequence.getIndex(), 0, sequence.getType());

                            switch (sequence.getEffect()) {
                                case TYPE_WIPE:
                                    if (isDouble) {
                                        mEffectLogic.wipeColorDouble(sequence.getColor(), getStartIndex(sequence.getIndex(), 1), getEndIndex(sequence.getIndex(), 1), getStartIndex(sequence.getIndex(), 2), getEndIndex(sequence.getIndex(), 2), getStartIndex(sequence.getIndex(), 3), getEndIndex(sequence.getIndex(), 3), getDirection(sequence.getIndex(), 1, sequence.getType()), getDirection(sequence.getIndex(), 2, sequence.getType()), getDirection(sequence.getIndex(), 3, sequence.getType()), sequence.getSpeed());
                                    } else {
                                        mEffectLogic.wipeColor(sequence.getColor(), startIndex, endIndex, direction, sequence.getSpeed());
                                    }
                                    break;
                                case TYPE_THEATER:
                                    if (isDouble && sequence.getType() <= 1) {
                                        mEffectLogic.theaterColorDouble(sequence.getColor(), getStartIndex(sequence.getIndex(), 1), getEndIndex(sequence.getIndex(), 1), getStartIndex(sequence.getIndex(), 2), getEndIndex(sequence.getIndex(), 2), getStartIndex(sequence.getIndex(), 3), getEndIndex(sequence.getIndex(), 3), getDirection(sequence.getIndex(), 1, sequence.getType()), getDirection(sequence.getIndex(), 2, sequence.getType()), getDirection(sequence.getIndex(), 3, sequence.getType()), sequence.getSpeed(), sequence.getLength(), sequence.getRepeat());
                                    } else {
                                        switch (sequence.getType()) {
                                            case 0:
                                            case 1:
                                                mEffectLogic.theaterColor(sequence.getColor(), startIndex, endIndex, direction, sequence.getSpeed(), sequence.getLength(), sequence.getRepeat());
                                                break;
                                            case 2:
                                            case 3:
                                                mEffectLogic.theaterRainbow(startIndex, endIndex, direction, sequence.getSpeed(), sequence.getLength());
                                                break;
                                        }
                                    }
                                    break;
                                case TYPE_RAINBOW:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.rainbowLinear(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.rainbowCycle(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_STATIC:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.staticColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.staticRandom(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_TWINKLE:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.twinkleColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getType() == 1 ? 1 : 0, sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.twinkleColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getType() == 1 ? 1 : 0, sequence.getRepeat());
                                            break;
                                        case 3:
                                            mEffectLogic.twinkleRandom(startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getType() == 3 ? 1 : 0, sequence.getRepeat());
                                            break;
                                        case 4:
                                            mEffectLogic.twinkleRandom(startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getType() == 3 ? 1 : 0, sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_FADE:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.fadeInColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.fadeOutColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_STROBE:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.strobeColorConstant(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.strobeColorCrazy(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 3:
                                            mEffectLogic.strobeRandomConstant(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 4:
                                            mEffectLogic.strobeRandomCrazy(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_SPARKLE:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.sparkleColorDark(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.sparkleColorLight(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                                case TYPE_BOUNCE:
                                    mEffectLogic.bounceColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getLength(), sequence.getRepeat());
                                    break;
                                case TYPE_RUNNING:
                                    switch (sequence.getType()) {
                                        case 1:
                                            mEffectLogic.runningColor(sequence.getColor(), startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                        case 2:
                                            mEffectLogic.runningRandom(startIndex, endIndex, sequence.getSpeed(), sequence.getRepeat());
                                            break;
                                    }
                                    break;
                            }
                        }

                    } catch (Exception ignored) {
                    }
                }
            }
        });
        mThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(COLOR_BACKGROUND);

        if (getWidth() == 0 || getHeight() == 0 || mSequences == null) {
            invalidate();
            return;
        }

        if (mCellWidth == 0 || mCellHeight == 0 || mLedDiameter == 0) {
            mCellWidth = getWidth() / NUM_COLUMNS;
            mCellHeight = getHeight() / NUM_ROWS;
            mLedDiameter = mCellWidth / (NUM_LEDS + 2);
            mCellMargin = mLedDiameter;
        }

        for (int c = 0; c < NUM_COLUMNS; c++) {
            for (int r = 0; r < NUM_ROWS; r++) {
                for (int l = 0; l < NUM_LEDS; l++) {
                    mPaintLed.setColor(Color.BLACK);
                    canvas.drawCircle(c * mCellWidth + mCellMargin + l * mLedDiameter + mLedDiameter / 2, r * mCellHeight + mCellHeight / 2, mLedDiameter / 3, mPaintLed);
                    mPaintLed.setColor(mLeds.get(c * (NUM_COLUMNS * NUM_LEDS) + r * NUM_LEDS + l).getColor());
                    canvas.drawCircle(c * mCellWidth + mCellMargin + l * mLedDiameter + mLedDiameter / 2, r * mCellHeight + mCellHeight / 2, mLedDiameter / 3, mPaintLed);
                }
            }
        }

        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    private int getStartIndex(int index, int subindex) {
        switch (index) {
            case 1:
                return LED_STRIP_A1_START;
            case 2:
                return LED_STRIP_A2_START;
            case 3:
                return LED_STRIP_A3_START;
            case 4:
                if (subindex == 1) {
                    return LED_STRIP_A1_START;
                } else if (subindex == 2) {
                    return LED_STRIP_A2_START;
                } else if (subindex == 3) {
                    return LED_STRIP_A3_START;
                } else {
                    return LED_STRIP_A1_START;
                }
        }
        return 0;
    }

    private int getEndIndex(int index, int subindex) {
        switch (index) {
            case 1:
                return LED_STRIP_A1_START + LED_STRIP_A1_SIZE;
            case 2:
                return LED_STRIP_A2_START + LED_STRIP_A2_SIZE;
            case 3:
                return LED_STRIP_A3_START + LED_STRIP_A3_SIZE;
            case 4:
                if (subindex == 1) {
                    return LED_STRIP_A1_START + LED_STRIP_A1_SIZE;
                } else if (subindex == 2) {
                    return LED_STRIP_A2_START + LED_STRIP_A2_SIZE;
                } else if (subindex == 3) {
                    return LED_STRIP_A3_START + LED_STRIP_A3_SIZE;
                } else {
                    return LED_STRIP_A1_START + LED_STRIP_A1_SIZE + LED_STRIP_A2_SIZE + LED_STRIP_A3_SIZE;
                }
        }
        return NUM_LEDS;
    }

    private int getDirection(int index, int subindex, int forward) {
        int mult = (forward == 1 || forward == 3) ? 1 : -1;
        switch (index) {
            case 1:
                return mult * LED_STRIP_A1_DIRECTION;
            case 2:
                return mult * LED_STRIP_A2_DIRECTION;
            case 3:
                return mult * LED_STRIP_A3_DIRECTION;
            case 4:
                if (subindex == 1) {
                    return mult * LED_STRIP_A1_DIRECTION;
                } else if (subindex == 2) {
                    return mult * LED_STRIP_A2_DIRECTION;
                } else if (subindex == 3) {
                    return mult * LED_STRIP_A3_DIRECTION;
                } else {
                    return mult;
                }
        }
        return 1;
    }

}