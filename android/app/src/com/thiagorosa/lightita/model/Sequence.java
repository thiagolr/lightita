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

package com.thiagorosa.lightita.model;

import com.thiagorosa.lightita.common.Logger;

import java.util.ArrayList;

public class Sequence {

    private ArrayList<Effect> mEffects = new ArrayList<>();
    private long mId = 0;

    public Sequence(long id, String sequence) {
        mId = id;

        String effects[] = sequence.split("@");
        for (int i = 0; i < effects.length; i++) {
            Logger.EFFECT("" + effects[i]);
            mEffects.add(new Effect(effects[i]));
        }
    }

    public long getId() {
        return mId;
    }

    public ArrayList<Effect> getEffects() {
        return mEffects;
    }

    public void show() {
        for (Effect effect : mEffects) {
            effect.show();
        }
    }

}
