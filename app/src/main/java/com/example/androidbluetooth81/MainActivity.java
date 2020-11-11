package com.example.androidbluetooth81;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn, mdevicechose;
    boolean count = false;
    BluetoothAdapter mBlueAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);
        mBlueIv = findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);
        mdevicechose = findViewById(R.id.devicechose);
        mOffBtn.setEnabled(false);
        mDiscoverBtn.setEnabled(false);
        mPairedBtn.setEnabled(false);
        mdevicechose.setEnabled(false);
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if bluetooth is available or not
        if (mBlueAdapter != null) {
            mStatusBlueTv.setText("系統：您的裝置支援藍芽");
        } else {
            mStatusBlueTv.setText("系統：您的裝置不支援藍芽");
        }

        //set image according to bluetooth status(on/off)
        if (mBlueAdapter.isEnabled()) {
            mBlueIv.setImageResource(R.drawable.ic_action_on);
        } else {
            mBlueIv.setImageResource(R.drawable.ic_action_off);
        }
        //on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("開啟藍牙中...");
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    showToast("藍牙已經處於開啟狀態");
                }
                mOffBtn.setEnabled(true);
                mDiscoverBtn.setEnabled(true);
                mPairedBtn.setEnabled(true);
                mOnBtn.setEnabled(false);
            }
        });
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mBlueAdapter.disable();
                    showToast("關閉藍牙中...");
                    mBlueIv.setImageResource(R.drawable.ic_action_off);
                    mPairedTv.setText("");
                    count = false;
                } else {
                    showToast("藍牙已經處於關閉狀態");
                }
                mOffBtn.setEnabled(false);
                mDiscoverBtn.setEnabled(false);
                mPairedBtn.setEnabled(false);
                mOnBtn.setEnabled(true);
                mdevicechose.setEnabled(false);
            }
        });
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isDiscovering()) {
                    showToast("已設定讓您的設備可被找到");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                    startActivity(intent);
                }
            }
        });
        //off btn click
        //get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("您配對的裝置：");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device : devices) {
                        if (device.getName() != "") {
                            count = true;
                            mPairedTv.append("\nDevice: " + device.getName() + ", " + device);
                            mdevicechose.setEnabled(true);
                        }
                    }
                    if (count == false) {
                        mPairedTv.append("\n你尚未配對到裝置");
                    }
                } else {
                    //bluetooth is off so can't get paired devices
                    showToast("請開啟藍牙尋找裝置");
                }
            }
        });
        mdevicechose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendevicechose();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    showToast("藍牙已開啟");
                } else {
                    //user denied to turn bluetooth on
                    showToast("無法開啟藍牙");
                    mOffBtn.setEnabled(false);
                    mDiscoverBtn.setEnabled(false);
                    mPairedBtn.setEnabled(false);
                    mOnBtn.setEnabled(true);
                    mdevicechose.setEnabled(false);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void opendevicechose() {
        Intent intent = new Intent(this, Page2.class);
        startActivity(intent);
    }
    //toast message function
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
