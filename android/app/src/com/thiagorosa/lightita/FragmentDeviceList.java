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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thiagorosa.lightita.common.CustomFragment;
import com.thiagorosa.lightita.common.Logger;
import com.thiagorosa.lightita.manager.BluetoothManager;
import com.thiagorosa.lightita.manager.PreferencesManager;
import com.thiagorosa.lightita.model.Device;
import com.thiagorosa.lightita.model.Device.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class FragmentDeviceList extends CustomFragment {

    public static final String EXTRA_MAC = "mac-address";

    private List<Device> mDevices = new ArrayList<Device>();
    private RecyclerView mList = null;
    private DeviceAdapter mAdapter = null;
    private TextView mStatus = null;
    private ProgressBar mProgress = null;
    private boolean isConnecting = false;
    private boolean isAutoConnecting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list, null);

        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mStatus = (TextView) view.findViewById(R.id.status);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new DeviceAdapter();

        mList = (RecyclerView) view.findViewById(R.id.list);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);

        if (!BluetoothManager.getInstance().isSupported()) {
            Logger.BT("Bluetooth is not supported!");
            if (mStatus != null) {
                mStatus.setVisibility(View.VISIBLE);
                mStatus.setText(R.string.device_status_unsupported);
            }
        } else if (!BluetoothManager.getInstance().isEnabled()) {
            Logger.BT("Bluetooth is not enabled!");
            if (mStatus != null) {
                mStatus.setVisibility(View.VISIBLE);
                mStatus.setText(R.string.device_status_disabled);
            }
            BluetoothManager.getInstance().promptToEnable(this);
        } else {
            populateDevices();
        }

        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED));
        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if (BluetoothManager.getInstance().isDiscovering()) {
                        BluetoothManager.getInstance().cancelDiscovery();
                        return true;
                    } else if (isConnecting) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (getArguments() != null) {
            String extra = getArguments().getString(EXTRA_MAC, "");

            if (!TextUtils.isEmpty(extra)) {
                ArrayList<Integer> positions = new ArrayList<>();
                String mac[] = extra.split("#");

                for (int m = 0; m < mac.length; m++) {
                    if (!TextUtils.isEmpty(mac[m])) {
                        for (int i = 0; i < mDevices.size(); i++) {
                            if (mac[m].equals(mDevices.get(i).getMAC())) {
                                isAutoConnecting = true;
                                positions.add(i);
                            }
                        }
                    }
                }

                if (positions.size() > 0) {
                    connect(positions);
                }

            }
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BluetoothManager.getInstance().cancelDiscovery();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(null);

        update(!isAutoConnecting ? R.string.device_title : R.string.device_connecting, !isAutoConnecting);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BluetoothManager.REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    populateDevices();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.device, menu);

        MenuItem item = menu.findItem(R.id.search);
        if (item != null) {
            item.setVisible(!isConnecting && !BluetoothManager.getInstance().isDiscovering() && BluetoothManager.getInstance().isEnabled());
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isConnecting) {
                return true;
            }
        }
        if (item.getItemId() == R.id.search) {
            BluetoothManager.getInstance().startDiscovery();

            mProgress.setVisibility(View.VISIBLE);

            if (getActivity() != null) {
                getActivity().invalidateOptionsMenu();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private void populateDevices() {
        Set<BluetoothDevice> pairedDevices = BluetoothManager.getInstance().getAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            Logger.BT("Paired devices:");
            for (BluetoothDevice bt : pairedDevices) {
                Device device = new Device(bt.getName(), bt.getAddress(), Status.BONDED);
                if (device.isDeviceA() && BluetoothManager.getInstance().isConnectedA() || device.isDeviceB() && BluetoothManager.getInstance().isConnectedB()) {
                    device.setStatus(Status.CONNECTED);
                }

                Logger.BT("    " + device);
                mDevices.add(device);
            }

            Collections.sort(mDevices, new Comparator<Device>() {
                @Override
                public int compare(Device lhs, Device rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (mStatus != null) {
            mStatus.setVisibility(View.INVISIBLE);
        }
    }

    private void connect(final ArrayList<Integer> positions) {

        new AsyncTask<Void, Void, Void>() {

            private boolean success = false;

            @Override
            protected void onPreExecute() {
                isConnecting = true;

                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
                if (mList != null) {
                    mList.setVisibility(View.INVISIBLE);
                }
                if (mProgress != null) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                if (mStatus != null) {
                    mStatus.setVisibility(View.VISIBLE);
                    mStatus.setText(getText(R.string.device_status_connecting));
                }

                update(R.string.device_connecting, false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Logger.BT("Connecting:");
                for (Integer position : positions) {
                    Logger.BT("    " + mDevices.get(position));
                    boolean connected = BluetoothManager.getInstance().connect(mDevices.get(position));

                    if (connected) {
                        String mac = mDevices.get(position).getMAC();
                        String stored = PreferencesManager.getInstance().getDeviceMAC();
                        mDevices.get(position).setStatus(Device.Status.CONNECTED);

                        if (TextUtils.isEmpty(stored)) {
                            PreferencesManager.getInstance().setDeviceMAC(mac);
                        } else if (!stored.contains(mac)) {
                            PreferencesManager.getInstance().setDeviceMAC(stored + "#" + mac);
                        }
                    }

                    success |= connected;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                isConnecting = false;

                for (Device device : mDevices) {
                    device.setStatus(Device.Status.BONDED);
                    if (device.isDeviceA() && BluetoothManager.getInstance().isConnectedA() || device.isDeviceB() && BluetoothManager.getInstance().isConnectedB()) {
                        device.setStatus(Device.Status.CONNECTED);
                    }
                }

                if (getActivity() != null) {
                    Toast.makeText(getActivity(), success ? R.string.device_status_connected : R.string.device_status_failed, Toast.LENGTH_SHORT).show();
                }

                if (mList != null) {
                    mList.setVisibility(View.VISIBLE);
                }
                if (mProgress != null) {
                    mProgress.setVisibility(View.INVISIBLE);
                }
                if (mStatus != null) {
                    mStatus.setVisibility(View.INVISIBLE);
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }

                update(R.string.device_title, true);
            }

        }.execute();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bt = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bt.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Device device = new Device(bt.getName(), bt.getAddress(), Status.NEW);
                    if (!mDevices.contains(device)) {
                        Logger.BT("Found:");
                        Logger.BT("    " + device);
                        mDevices.add(device);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mProgress != null) {
                    mProgress.setVisibility(View.INVISIBLE);
                }
                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Logger.BT("Bluetooth device connected!");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Logger.BT("Bluetooth device disconnected!");
            }
        }
    };

    /*******************************************************************************************
     *******************************************************************************************/

    private class DeviceAdapter extends RecyclerView.Adapter<DeviceHolder> {

        @Override
        public void onBindViewHolder(DeviceHolder holder, final int position) {

            if (((position < 0) || (position >= mDevices.size()) || (getActivity() == null))) {
                return;
            }

            holder.background.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = mDevices.get(position);
                    if (device.getStatus() == Status.CONNECTED) {
                        String mac = device.getMAC();
                        String stored = PreferencesManager.getInstance().getDeviceMAC();

                        boolean disconnected = false;
                        if (device.isDeviceA()) {
                            BluetoothManager.getInstance().disconnectA();
                            disconnected = true;
                        } else if (device.isDeviceB()) {
                            BluetoothManager.getInstance().disconnectB();
                            disconnected = true;
                        }

                        if (disconnected) {
                            device.setStatus(Status.BONDED);
                            if (stored.contains(mac)) {
                                PreferencesManager.getInstance().setDeviceMAC(stored.replace(mac, "").replace("#", ""));
                            }

                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }

                            update(!isAutoConnecting ? R.string.device_title : R.string.device_connecting, !isAutoConnecting);
                        }
                    } else {
                        ArrayList<Integer> positions = new ArrayList<>();
                        positions.add(position);
                        connect(positions);
                    }
                }
            });

            holder.name.setText(mDevices.get(position).getName());
            holder.mac.setText(mDevices.get(position).getMAC());

            switch (mDevices.get(position).getStatus()) {
                case NEW:
                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText(R.string.device_new);
                    break;
                case BONDED:
                    holder.status.setVisibility(View.GONE);
                    break;
                case CONNECTED:
                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText(R.string.device_connected);
                    break;
                default:
                    holder.status.setVisibility(View.GONE);
                    break;
            }

            if (mDevices.size() == position) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DeviceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false));
        }

        @Override
        public int getItemCount() {
            return mDevices.size();
        }

    }

    private static class DeviceHolder extends RecyclerView.ViewHolder {

        public RelativeLayout background = null;
        public TextView name = null;
        public TextView mac = null;
        public TextView status = null;
        public View divider = null;

        public DeviceHolder(View parent) {
            super(parent);

            background = (RelativeLayout) parent.findViewById(R.id.background);
            name = (TextView) parent.findViewById(R.id.name);
            mac = (TextView) parent.findViewById(R.id.mac);
            status = (TextView) parent.findViewById(R.id.status);
            divider = parent.findViewById(R.id.divider);
        }

    }

}