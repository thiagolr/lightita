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

package com.thiagorosa.lightita.common;

import android.util.Log;

public class Logger {

    private static final String TAG = "[LIGHTITA]";

    public static void BT(String text) {
        if (Constants.DEBUG_LOG_BLUETOOTH) {
            Log.d(TAG, "[BT] " + text);
        }
    }

    public static void DB(String text) {
        if (Constants.DEBUG_LOG_DATABASE) {
            Log.d(TAG, "[DB] " + text);
        }
    }

    public static void EFFECT(String text) {
        if (Constants.DEBUG_LOG_EFFECT) {
            Log.d(TAG, "[EF] " + text);
        }
    }

}
