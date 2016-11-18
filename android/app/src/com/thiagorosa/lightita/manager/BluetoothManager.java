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

package com.thiagorosa.lightita.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.thiagorosa.lightita.common.Logger;
import com.thiagorosa.lightita.model.Device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {

    private static final BluetoothManager INSTANCE = new BluetoothManager();

    // UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;

    // DEVICE A
    private BluetoothDevice mDeviceA = null;
    private BluetoothSocket mDeviceSocketA = null;
    private OutputStream mOutputStreamA = null;
    private InputStream mInputStreamA = null;
    private boolean isConnectedA = false;

    // DEVICE B
    private BluetoothDevice mDeviceB = null;
    private BluetoothSocket mDeviceSocketB = null;
    private OutputStream mOutputStreamB = null;
    private InputStream mInputStreamB = null;
    private boolean isConnectedB = false;

    /*******************************************************************************************
     *******************************************************************************************/

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        return INSTANCE;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public BluetoothAdapter getAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isSupported() {
        return getAdapter() != null;
    }

    public boolean isEnabled() {
        return getAdapter().isEnabled();
    }

    public void promptToEnable(Fragment fragment) {
        if (!isEnabled() && (fragment != null)) {
            fragment.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isDiscovering() {
        return getAdapter().isDiscovering();
    }

    public void startDiscovery() {
        if (isDiscovering()) {
            cancelDiscovery();
        }
        getAdapter().startDiscovery();
    }

    public void cancelDiscovery() {
        getAdapter().cancelDiscovery();
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isConnectedA() {
        return isConnectedA;
    }

    public boolean isConnectedB() {
        return isConnectedB;
    }

    public boolean connect(Device device) {
        cancelDiscovery();

        if (device.isDeviceA()) {
            mDeviceA = getAdapter().getRemoteDevice(device.getMAC());
        } else if (device.isDeviceB()) {
            mDeviceB = getAdapter().getRemoteDevice(device.getMAC());
        }

        try {
            if (device.isDeviceA()) {
                mDeviceSocketA = mDeviceA.createRfcommSocketToServiceRecord(SPP_UUID);
            } else if (device.isDeviceB()) {
                mDeviceSocketB = mDeviceB.createRfcommSocketToServiceRecord(SPP_UUID);
            }
        } catch (IOException e) {
            Logger.BT("ERROR: socket create failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            if (device.isDeviceA()) {
                mDeviceSocketA.connect();
            } else if (device.isDeviceB()) {
                mDeviceSocketB.connect();
            }
        } catch (IOException e) {
            Logger.BT("ERROR: open socket failed (" + e.getMessage() + ")");
            try {
                if (device.isDeviceA()) {
                    if (mDeviceSocketA != null) {
                        mDeviceSocketA.close();
                        mDeviceSocketA = null;
                    }
                } else if (device.isDeviceB()) {
                    if (mDeviceSocketB != null) {
                        mDeviceSocketB.close();
                        mDeviceSocketB = null;
                    }
                }
            } catch (IOException ignored) {
                Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
            }
            return false;
        }

        try {
            if (device.isDeviceA()) {
                mOutputStreamA = mDeviceSocketA.getOutputStream();
            } else if (device.isDeviceB()) {
                mOutputStreamB = mDeviceSocketB.getOutputStream();
            }
        } catch (IOException e) {
            Logger.BT("ERROR: get output stream failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            if (device.isDeviceA()) {
                mInputStreamA = mDeviceSocketA.getInputStream();
            } else if (device.isDeviceB()) {
                mInputStreamB = mDeviceSocketB.getInputStream();
            }
        } catch (IOException e) {
            Logger.BT("ERROR: get input stream failed (" + e.getMessage() + ")");
            return false;
        }

        if (device.isDeviceA()) {
            isConnectedA = true;
        } else if (device.isDeviceB()) {
            isConnectedB = true;
        }

        return true;
    }

    public void disconnect() {
        disconnectA();
        disconnectB();
    }

    public void disconnectA() {
        if (isConnectedA) {
            if (mDeviceSocketA != null) {
                try {
                    mDeviceSocketA.close();
                    mDeviceSocketA = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
                }
            }

            if (mOutputStreamA != null) {
                try {
                    mOutputStreamA.close();
                    mOutputStreamA = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close output stream failed (" + e.getMessage() + ")");
                }
            }

            if (mInputStreamA != null) {
                try {
                    mInputStreamA.close();
                    mInputStreamA = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close input stream failed (" + e.getMessage() + ")");
                }
            }

            isConnectedA = false;
        }
    }

    public void disconnectB() {
        if (isConnectedB) {
            if (mDeviceSocketB != null) {
                try {
                    mDeviceSocketB.close();
                    mDeviceSocketB = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
                }
            }

            if (mOutputStreamB != null) {
                try {
                    mOutputStreamB.close();
                    mOutputStreamB = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close output stream failed (" + e.getMessage() + ")");
                }
            }

            if (mInputStreamB != null) {
                try {
                    mInputStreamB.close();
                    mInputStreamB = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close input stream failed (" + e.getMessage() + ")");
                }
            }

            isConnectedB = false;
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean write(int type, int param1, int param2, int param3, int param4, int param5) {
        try {
            Logger.BT("WRITE: " + type + "," + param1 + "," + param2 + "," + param3 + "," + param4 + "," + param5);

            if (isConnectedA) {
                mOutputStreamA.write(type);
                mOutputStreamA.write(param1);
                mOutputStreamA.write(param2);
                mOutputStreamA.write(param3);
                mOutputStreamA.write(param4);
                mOutputStreamA.write(param5);
            }
            if (isConnectedB) {
                mOutputStreamB.write(type);
                mOutputStreamB.write(param1);
                mOutputStreamB.write(param2);
                mOutputStreamB.write(param3);
                mOutputStreamB.write(param4);
                mOutputStreamB.write(param5);
            }
        } catch (IOException e) {
            Logger.BT("ERROR: failed to write (" + e.getMessage() + ")");
            return false;
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }

}
