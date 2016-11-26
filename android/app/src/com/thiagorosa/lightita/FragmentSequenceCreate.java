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

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.thiagorosa.lightita.common.CustomFragment;
import com.thiagorosa.lightita.manager.DatabaseManager;
import com.thiagorosa.lightita.manager.PreferencesManager;
import com.thiagorosa.lightita.model.Effect;

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

public class FragmentSequenceCreate extends CustomFragment {

    private ArrayList<Effect> mEffects = new ArrayList<>();

    private LinearLayout mListEffect = null;
    private ViewLed mViewLed = null;

    private Button mButtonRed = null;
    private Button mButtonGreen = null;
    private Button mButtonBlue = null;
    private Button mButtonYellow = null;
    private Button mButtonMagenta = null;
    private Button mButtonCyan = null;
    private Button mButtonWhite = null;
    private Button mButtonBlack = null;

    private Button mButtonMore1 = null;
    private Button mButtonMore2 = null;
    private Button mButtonMore3 = null;
    private Button mButtonMore4 = null;
    private Button mButtonMore5 = null;
    private Button mButtonMore6 = null;
    private Button mButtonMore7 = null;
    private Button mButtonMore8 = null;

    private Button mButtonRainbowLinear = null;
    private Button mButtonRainbowCycle = null;
    private Button mButtonStaticColor = null;
    private Button mButtonStaticRandom = null;
    private Button mButtonRunningColor = null;
    private Button mButtonRunningRandom = null;
    private Button mButtonFadeInColor = null;
    private Button mButtonFadeOutColor = null;

    private SeekBar mCustom1SpeedValue = null;
    private TextView mCustom1SpeedText = null;
    private int mCustom1Speed = 0;
    private SeekBar mCustom1RepeatValue = null;
    private TextView mCustom1RepeatText = null;
    private int mCustom1Repeat = 0;

    private Button mButtonTwinkleColorSingle = null;
    private Button mButtonTwinkleColorKeep = null;
    private Button mButtonTwinkleRandomSingle = null;
    private Button mButtonTwinkleRandomKeep = null;
    private Button mButtonStrobeColorConstant = null;
    private Button mButtonStrobeRandomConstant = null;
    private Button mButtonStrobeColorCrazy = null;
    private Button mButtonStrobeRandomCrazy = null;
    private Button mButtonSparkleColorDark = null;
    private Button mButtonSparkleColorLight = null;
    private Button mButtonBounceColor = null;

    private SeekBar mCustom2SpeedValue = null;
    private TextView mCustom2SpeedText = null;
    private int mCustom2Speed = 0;
    private SeekBar mCustom2LengthValue = null;
    private TextView mCustom2LengthText = null;
    private int mCustom2Length = 0;
    private SeekBar mCustom2RepeatValue = null;
    private TextView mCustom2RepeatText = null;
    private int mCustom2Repeat = 0;

    private Button mButtonWipeA1R = null;
    private Button mButtonWipeA2R = null;
    private Button mButtonWipeA3R = null;
    private Button mButtonWipeA1L = null;
    private Button mButtonWipeA2L = null;
    private Button mButtonWipeA3L = null;
    private Button mButtonWipeAR = null;
    private Button mButtonWipeAL = null;
    private SeekBar mWipeSpeedValue = null;
    private TextView mWipeSpeedText = null;
    private int mWipeSpeed = 0;

    private Button mButtonTheaterA1R = null;
    private Button mButtonTheaterA2R = null;
    private Button mButtonTheaterA3R = null;
    private Button mButtonTheaterA1L = null;
    private Button mButtonTheaterA2L = null;
    private Button mButtonTheaterA3L = null;
    private Button mButtonTheaterAR = null;
    private Button mButtonTheaterAL = null;
    private Button mButtonTheaterRainbowR = null;
    private Button mButtonTheaterRainbowL = null;

    private SeekBar mTheaterSpeedValue = null;
    private TextView mTheaterSpeedText = null;
    private int mTheaterSpeed = 0;
    private SeekBar mTheaterLengthValue = null;
    private TextView mTheaterLengthText = null;
    private int mTheaterLength = 0;
    private SeekBar mTheaterRepeatValue = null;
    private TextView mTheaterRepeatText = null;
    private int mTheaterRepeat = 0;

    private SeekBar mBrightnessValue = null;
    private TextView mBrightnessText = null;
    private int mBrightness = 0;

    private int mColor = Color.WHITE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create, null);

        mListEffect = (LinearLayout) view.findViewById(R.id.sequence_list);

        mViewLed = (ViewLed) view.findViewById(R.id.led_view);
        mViewLed.load(mEffects);

        mButtonRed = (Button) view.findViewById(R.id.red);
        mButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.RED);
            }
        });

        mButtonGreen = (Button) view.findViewById(R.id.green);
        mButtonGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.GREEN);
            }
        });

        mButtonBlue = (Button) view.findViewById(R.id.blue);
        mButtonBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.BLUE);
            }
        });

        mButtonYellow = (Button) view.findViewById(R.id.yellow);
        mButtonYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.YELLOW);
            }
        });

        mButtonMagenta = (Button) view.findViewById(R.id.magenta);
        mButtonMagenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.MAGENTA);
            }
        });

        mButtonCyan = (Button) view.findViewById(R.id.cyan);
        mButtonCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.CYAN);
            }
        });

        mButtonWhite = (Button) view.findViewById(R.id.white);
        mButtonWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.WHITE);
            }
        });

        mButtonBlack = (Button) view.findViewById(R.id.black);
        mButtonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.BLACK);
            }
        });

        mButtonMore1 = (Button) view.findViewById(R.id.more1);
        mButtonMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(1);
            }
        });

        mButtonMore2 = (Button) view.findViewById(R.id.more2);
        mButtonMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(2);
            }
        });

        mButtonMore3 = (Button) view.findViewById(R.id.more3);
        mButtonMore3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(3);
            }
        });

        mButtonMore4 = (Button) view.findViewById(R.id.more4);
        mButtonMore4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(4);
            }
        });

        mButtonMore5 = (Button) view.findViewById(R.id.more5);
        mButtonMore5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(5);
            }
        });

        mButtonMore6 = (Button) view.findViewById(R.id.more6);
        mButtonMore6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(6);
            }
        });

        mButtonMore7 = (Button) view.findViewById(R.id.more7);
        mButtonMore7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(7);
            }
        });

        mButtonMore8 = (Button) view.findViewById(R.id.more8);
        mButtonMore8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(8);
            }
        });


        mButtonRainbowLinear = (Button) view.findViewById(R.id.rainbow_linear);
        mButtonRainbowLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainbow(4, 1);
            }
        });

        mButtonRainbowCycle = (Button) view.findViewById(R.id.rainbow_cycle);
        mButtonRainbowCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainbow(4, 2);
            }
        });

        mButtonStaticColor = (Button) view.findViewById(R.id.static_color);
        mButtonStaticColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                static2(4, 1);
            }
        });

        mButtonStaticRandom = (Button) view.findViewById(R.id.static_random);
        mButtonStaticRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                static2(4, 2);
            }
        });

        mButtonRunningColor = (Button) view.findViewById(R.id.running_color);
        mButtonRunningColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running(4, 1);
            }
        });

        mButtonRunningRandom = (Button) view.findViewById(R.id.running_random);
        mButtonRunningRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running(4, 2);
            }
        });

        mButtonFadeInColor = (Button) view.findViewById(R.id.fade_in);
        mButtonFadeInColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fade(4, 1);
            }
        });

        mButtonFadeOutColor = (Button) view.findViewById(R.id.fade_out);
        mButtonFadeOutColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fade(4, 2);
            }
        });

        mCustom1SpeedValue = (SeekBar) view.findViewById(R.id.custom1_speed_value);
        mCustom1SpeedValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCustom1SpeedText.setText(progress + getText(R.string.create_milliseconds).toString());
                mCustom1Speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mCustom1SpeedText = (TextView) view.findViewById(R.id.custom1_speed_text);
        mCustom1SpeedText.setText(mCustom1SpeedValue.getProgress() + getText(R.string.create_milliseconds).toString());
        mCustom1Speed = mCustom1SpeedValue.getProgress();

        mCustom1RepeatValue = (SeekBar) view.findViewById(R.id.custom1_repeat_value);
        mCustom1RepeatValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                mCustom1RepeatText.setText(progress + getText(R.string.create_repeat).toString());
                mCustom1Repeat = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mCustom1RepeatText = (TextView) view.findViewById(R.id.custom1_repeat_text);
        mCustom1Repeat = mCustom1RepeatValue.getProgress() + 1;
        mCustom1RepeatText.setText(mCustom1Repeat + getText(R.string.create_repeat).toString());

        mButtonTwinkleColorSingle = (Button) view.findViewById(R.id.twinkle_color_single);
        mButtonTwinkleColorSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twinkle(4, 1);
            }
        });

        mButtonTwinkleColorKeep = (Button) view.findViewById(R.id.twinkle_color_keep);
        mButtonTwinkleColorKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twinkle(4, 2);
            }
        });

        mButtonTwinkleRandomSingle = (Button) view.findViewById(R.id.twinkle_random_single);
        mButtonTwinkleRandomSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twinkle(4, 3);
            }
        });

        mButtonTwinkleRandomKeep = (Button) view.findViewById(R.id.twinkle_random_keep);
        mButtonTwinkleRandomKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twinkle(4, 4);
            }
        });

        mButtonStrobeColorConstant = (Button) view.findViewById(R.id.strobe_color_constant);
        mButtonStrobeColorConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strobe(4, 1);
            }
        });

        mButtonStrobeColorCrazy = (Button) view.findViewById(R.id.strobe_color_crazy);
        mButtonStrobeColorCrazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strobe(4, 2);
            }
        });

        mButtonStrobeRandomConstant = (Button) view.findViewById(R.id.strobe_random_constant);
        mButtonStrobeRandomConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strobe(4, 3);
            }
        });

        mButtonStrobeRandomCrazy = (Button) view.findViewById(R.id.strobe_random_crazy);
        mButtonStrobeRandomCrazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strobe(4, 4);
            }
        });

        mButtonSparkleColorDark = (Button) view.findViewById(R.id.sparkle_dark);
        mButtonSparkleColorDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sparkle(4, 1);
            }
        });

        mButtonSparkleColorLight = (Button) view.findViewById(R.id.sparkle_light);
        mButtonSparkleColorLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sparkle(4, 2);
            }
        });

        mButtonBounceColor = (Button) view.findViewById(R.id.bounce_color);
        mButtonBounceColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounce(4, 1);
            }
        });

        mCustom2SpeedValue = (SeekBar) view.findViewById(R.id.custom2_speed_value);
        mCustom2SpeedValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCustom2SpeedText.setText(progress + getText(R.string.create_milliseconds).toString());
                mCustom2Speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mCustom2SpeedText = (TextView) view.findViewById(R.id.custom2_speed_text);
        mCustom2SpeedText.setText(mCustom2SpeedValue.getProgress() + getText(R.string.create_milliseconds).toString());
        mCustom2Speed = mCustom2SpeedValue.getProgress();

        mCustom2LengthValue = (SeekBar) view.findViewById(R.id.custom2_length_value);
        mCustom2LengthValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                mCustom2LengthText.setText(progress + " " + getText(R.string.create_leds).toString());
                mCustom2Length = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mCustom2LengthText = (TextView) view.findViewById(R.id.custom2_length_text);
        mCustom2Length = mCustom2LengthValue.getProgress() + 1;
        mCustom2LengthText.setText(mCustom2Length + " " + getText(R.string.create_leds).toString());

        mCustom2RepeatValue = (SeekBar) view.findViewById(R.id.custom2_repeat_value);
        mCustom2RepeatValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                mCustom2RepeatText.setText(progress + getText(R.string.create_repeat).toString());
                mCustom2Repeat = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mCustom2RepeatText = (TextView) view.findViewById(R.id.custom2_repeat_text);
        mCustom2Repeat = mCustom2RepeatValue.getProgress() + 1;
        mCustom2RepeatText.setText(mCustom2Repeat + getText(R.string.create_repeat).toString());

        mButtonWipeA1R = (Button) view.findViewById(R.id.wipe_a1r);
        mButtonWipeA1R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(1, 1);
            }
        });

        mButtonWipeA2R = (Button) view.findViewById(R.id.wipe_a2r);
        mButtonWipeA2R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(2, 1);
            }
        });

        mButtonWipeA3R = (Button) view.findViewById(R.id.wipe_a3r);
        mButtonWipeA3R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(3, 1);
            }
        });

        mButtonWipeA1L = (Button) view.findViewById(R.id.wipe_a1l);
        mButtonWipeA1L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(1, 0);
            }
        });

        mButtonWipeA2L = (Button) view.findViewById(R.id.wipe_a2l);
        mButtonWipeA2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(2, 0);
            }
        });

        mButtonWipeA3L = (Button) view.findViewById(R.id.wipe_a3l);
        mButtonWipeA3L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(3, 0);
            }
        });

        mButtonWipeAR = (Button) view.findViewById(R.id.wipe_ar);
        mButtonWipeAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(4, 1);
            }
        });

        mButtonWipeAL = (Button) view.findViewById(R.id.wipe_al);
        mButtonWipeAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe(4, 0);
            }
        });

        mWipeSpeedValue = (SeekBar) view.findViewById(R.id.wipe_speed_value);
        mWipeSpeedValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWipeSpeedText.setText(progress + getText(R.string.create_milliseconds).toString());
                mWipeSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mWipeSpeedText = (TextView) view.findViewById(R.id.wipe_speed_text);
        mWipeSpeedText.setText(mWipeSpeedValue.getProgress() + getText(R.string.create_milliseconds).toString());
        mWipeSpeed = mWipeSpeedValue.getProgress();

        mButtonTheaterA1R = (Button) view.findViewById(R.id.theater_a1r);
        mButtonTheaterA1R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(1, 1);
            }
        });

        mButtonTheaterA2R = (Button) view.findViewById(R.id.theater_a2r);
        mButtonTheaterA2R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(2, 1);
            }
        });

        mButtonTheaterA3R = (Button) view.findViewById(R.id.theater_a3r);
        mButtonTheaterA3R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(3, 1);
            }
        });

        mButtonTheaterA1L = (Button) view.findViewById(R.id.theater_a1l);
        mButtonTheaterA1L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(1, 0);
            }
        });

        mButtonTheaterA2L = (Button) view.findViewById(R.id.theater_a2l);
        mButtonTheaterA2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(2, 0);
            }
        });

        mButtonTheaterA3L = (Button) view.findViewById(R.id.theater_a3l);
        mButtonTheaterA3L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(3, 0);
            }
        });

        mButtonTheaterAR = (Button) view.findViewById(R.id.theater_ar);
        mButtonTheaterAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(4, 1);
            }
        });

        mButtonTheaterAL = (Button) view.findViewById(R.id.theater_al);
        mButtonTheaterAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(4, 0);
            }
        });

        mButtonTheaterRainbowR = (Button) view.findViewById(R.id.theater_rainbowr);
        mButtonTheaterRainbowR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(4, 3);
            }
        });

        mButtonTheaterRainbowL = (Button) view.findViewById(R.id.theater_rainbowl);
        mButtonTheaterRainbowL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater(4, 2);
            }
        });

        mTheaterSpeedValue = (SeekBar) view.findViewById(R.id.theater_speed_value);
        mTheaterSpeedValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTheaterSpeedText.setText(progress + getText(R.string.create_milliseconds).toString());
                mTheaterSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mTheaterSpeedText = (TextView) view.findViewById(R.id.theater_speed_text);
        mTheaterSpeedText.setText(mTheaterSpeedValue.getProgress() + getText(R.string.create_milliseconds).toString());
        mTheaterSpeed = mTheaterSpeedValue.getProgress();

        mTheaterLengthValue = (SeekBar) view.findViewById(R.id.theater_length_value);
        mTheaterLengthValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 2;
                mTheaterLengthText.setText(progress + " " + getText(R.string.create_leds).toString());
                mTheaterLength = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mTheaterLengthText = (TextView) view.findViewById(R.id.theater_length_text);
        mTheaterLength = mTheaterLengthValue.getProgress() + 2;
        mTheaterLengthText.setText(mTheaterLength + " " + getText(R.string.create_leds).toString());

        mTheaterRepeatValue = (SeekBar) view.findViewById(R.id.theater_repeat_value);
        mTheaterRepeatValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                mTheaterRepeatText.setText(progress + getText(R.string.create_repeat).toString());
                mTheaterRepeat = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mTheaterRepeatText = (TextView) view.findViewById(R.id.theater_repeat_text);
        mTheaterRepeat = mTheaterRepeatValue.getProgress() + 1;
        mTheaterRepeatText.setText(mTheaterRepeat + getText(R.string.create_repeat).toString());

        mBrightnessValue = (SeekBar) view.findViewById(R.id.brightness_value);
        mBrightnessValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrightnessText.setText(progress + getText(R.string.create_percentage).toString());
                mBrightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Effect.brightness(mBrightness);
            }
        });

        mBrightnessText = (TextView) view.findViewById(R.id.brightness_text);
        mBrightness = mBrightnessValue.getProgress();
        mBrightnessText.setText(mBrightness + getText(R.string.create_percentage).toString());

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(null);

        update(R.string.create_title, true);

        updateColors(mColor);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create, menu);

        MenuItem item = menu.findItem(R.id.save);
        if (item != null) {
            item.setVisible(mEffects.size() > 0);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.undo) {
            if (mEffects.size() > 0) {
                Effect.undo();
                mEffects.remove(mEffects.size() - 1);
            }
            updateSequence();
            return true;
        } else if (item.getItemId() == R.id.clear) {
            Effect.clear();
            mEffects.clear();
            updateSequence();
            return true;
        } else if (item.getItemId() == R.id.save) {
            if (mEffects.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Effect effect : mEffects) {
                    sb.append(effect.getInfo() + "@");
                }
                DatabaseManager.getInstance().updateSequence(0, "", sb.toString().substring(0, sb.toString().lastIndexOf("@")));
                Toast.makeText(getContext(), R.string.create_sequence_saved, Toast.LENGTH_LONG).show();

                Effect.clear();
                mEffects.clear();
                updateSequence();
            }
            return true;
        } else if (item.getItemId() == R.id.reset) {
            PreferencesManager.getInstance().setColor(1, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(2, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(3, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(4, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(5, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(6, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(7, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(8, Color.TRANSPARENT);
            updateColors(Color.WHITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateColors(int color) {
        mColor = color;

        mButtonMore1.setBackgroundColor(PreferencesManager.getInstance().getColor(1));
        mButtonMore2.setBackgroundColor(PreferencesManager.getInstance().getColor(2));
        mButtonMore3.setBackgroundColor(PreferencesManager.getInstance().getColor(3));
        mButtonMore4.setBackgroundColor(PreferencesManager.getInstance().getColor(4));
        mButtonMore5.setBackgroundColor(PreferencesManager.getInstance().getColor(5));
        mButtonMore6.setBackgroundColor(PreferencesManager.getInstance().getColor(6));
        mButtonMore7.setBackgroundColor(PreferencesManager.getInstance().getColor(7));
        mButtonMore8.setBackgroundColor(PreferencesManager.getInstance().getColor(8));

        mButtonMore1.setText(PreferencesManager.getInstance().getColor(1) == 0 ? "+" : "");
        mButtonMore2.setText(PreferencesManager.getInstance().getColor(2) == 0 ? "+" : "");
        mButtonMore3.setText(PreferencesManager.getInstance().getColor(3) == 0 ? "+" : "");
        mButtonMore4.setText(PreferencesManager.getInstance().getColor(4) == 0 ? "+" : "");
        mButtonMore5.setText(PreferencesManager.getInstance().getColor(5) == 0 ? "+" : "");
        mButtonMore6.setText(PreferencesManager.getInstance().getColor(6) == 0 ? "+" : "");
        mButtonMore7.setText(PreferencesManager.getInstance().getColor(7) == 0 ? "+" : "");
        mButtonMore8.setText(PreferencesManager.getInstance().getColor(8) == 0 ? "+" : "");

        mButtonStaticColor.setTextColor(mColor);
        mButtonRunningColor.setTextColor(mColor);
        mButtonFadeInColor.setTextColor(mColor);
        mButtonFadeOutColor.setTextColor(mColor);

        mButtonTwinkleColorSingle.setTextColor(mColor);
        mButtonTwinkleColorKeep.setTextColor(mColor);
        mButtonStrobeColorConstant.setTextColor(mColor);
        mButtonStrobeColorCrazy.setTextColor(mColor);
        mButtonSparkleColorDark.setTextColor(mColor);
        mButtonSparkleColorLight.setTextColor(mColor);
        mButtonBounceColor.setTextColor(mColor);

        mButtonWipeA1R.setTextColor(mColor);
        mButtonWipeA2R.setTextColor(mColor);
        mButtonWipeA3R.setTextColor(mColor);
        mButtonWipeA1L.setTextColor(mColor);
        mButtonWipeA2L.setTextColor(mColor);
        mButtonWipeA3L.setTextColor(mColor);
        mButtonWipeAR.setTextColor(mColor);
        mButtonWipeAL.setTextColor(mColor);

        mButtonTheaterA1R.setTextColor(mColor);
        mButtonTheaterA2R.setTextColor(mColor);
        mButtonTheaterA3R.setTextColor(mColor);
        mButtonTheaterA1L.setTextColor(mColor);
        mButtonTheaterA2L.setTextColor(mColor);
        mButtonTheaterA3L.setTextColor(mColor);
        mButtonTheaterAR.setTextColor(mColor);
        mButtonTheaterAL.setTextColor(mColor);
    }

    private void updateSequence() {
        mListEffect.removeAllViews();

        for (Effect effect : mEffects) {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            tv.setLayoutParams(lp);
            tv.setText(effect.toString());
            tv.setTextColor(effect.getColor());

            mListEffect.addView(tv);
        }

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void selectColor(final int index) {
        if (PreferencesManager.getInstance().getColor(index) == 0) {
            final ColorPicker cp = new ColorPicker(getActivity(), Color.red(PreferencesManager.getInstance().getColor(index)), Color.green(PreferencesManager.getInstance().getColor(index)), Color.blue(PreferencesManager.getInstance().getColor(index)));
            cp.show();

            Button ok = (Button) cp.findViewById(R.id.okColorButton);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int color = Color.rgb(cp.getRed(), cp.getGreen(), cp.getBlue());
                    PreferencesManager.getInstance().setColor(index, color);
                    cp.dismiss();

                    updateColors(color);
                }
            });
        } else {
            updateColors(PreferencesManager.getInstance().getColor(index));
        }
    }

    private void wipe(int index, int direction) {
        mEffects.add(new Effect(mColor, TYPE_WIPE, index, direction, mWipeSpeed, 0, 0));
        updateSequence();
    }

    private void theater(int index, int direction) {
        mEffects.add(new Effect(mColor, TYPE_THEATER, index, direction, mTheaterSpeed, mTheaterLength, mTheaterRepeat));
        updateSequence();
    }

    private void rainbow(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_RAINBOW, index, type, mCustom1Speed, 0, mCustom1Repeat));
        updateSequence();
    }

    private void static2(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_STATIC, index, type, mCustom1Speed, 0, mCustom1Repeat));
        updateSequence();
    }

    private void running(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_RUNNING, index, type, mCustom1Speed, 0, mCustom1Repeat));
        updateSequence();
    }

    private void fade(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_FADE, index, type, mCustom1Speed, 0, mCustom1Repeat));
        updateSequence();
    }

    private void twinkle(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_TWINKLE, index, type, mCustom2Speed, mCustom2Length, mCustom2Repeat));
        updateSequence();
    }

    private void strobe(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_STROBE, index, type, mCustom1Speed, 0, mCustom1Repeat));
        updateSequence();
    }

    private void sparkle(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_SPARKLE, index, type, mCustom2Speed, mCustom2Length, mCustom2Repeat));
        updateSequence();
    }

    private void bounce(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_BOUNCE, index, type, mCustom2Speed, mCustom2Length, mCustom2Repeat));
        updateSequence();
    }

}