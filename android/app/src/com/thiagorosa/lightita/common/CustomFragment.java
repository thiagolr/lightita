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

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.thiagorosa.lightita.ActivityMain;
import com.thiagorosa.lightita.R;
import com.thiagorosa.lightita.manager.BluetoothManager;

public abstract class CustomFragment extends Fragment {

    public void setScreenTitle(String title, String subtitle) {
        if (getActivity() != null) {
            ((ActivityMain) getActivity()).setScreenTitle(title, subtitle);
        }
    }

    public ActionBar getSupportActionBar() {
        if (getActivity() != null) {
            return ((ActivityMain) getActivity()).getSupportActionBar();
        }
        return null;
    }

    public Toolbar getToolbar() {
        if (getActivity() != null) {
            return ((ActivityMain) getActivity()).getToolbar();
        }
        return null;
    }

    public void update(int title, boolean show) {
        String connection = getText(R.string.device_not_connected).toString();

        String devices = "";
        if (BluetoothManager.getInstance().isConnectedA()) {
            devices = Constants.LED_A_NAME;
        }
        if (BluetoothManager.getInstance().isConnectedB()) {
            devices += (TextUtils.isEmpty(devices) ? "" : ", ") + Constants.LED_B_NAME;
        }

        setScreenTitle(getText(title).toString(), show ? TextUtils.isEmpty(devices) ? connection : devices : "");

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

}
