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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.thiagorosa.lightita.common.CustomFragment;

public class FragmentMain extends CustomFragment {

    private CardView mSelectDevice = null;
    private CardView mSequenceCreate = null;
    private CardView mSequenceLoad = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, null);

        mSelectDevice = (CardView) view.findViewById(R.id.select_device);
        mSelectDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentDeviceList();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragment, "fragment");
                transactionControl.addToBackStack(null);
                transactionControl.commitAllowingStateLoss();

                update(R.string.app_title, true);
            }
        });

        mSequenceCreate = (CardView) view.findViewById(R.id.create);
        mSequenceCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentSequenceCreate();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragment, "fragment");
                transactionControl.addToBackStack(null);
                transactionControl.commitAllowingStateLoss();
            }
        });

        mSequenceLoad = (CardView) view.findViewById(R.id.load);
        mSequenceLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentSequenceList();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragment, "fragment");
                transactionControl.addToBackStack(null);
                transactionControl.commitAllowingStateLoss();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_led);

        update(R.string.app_title, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}