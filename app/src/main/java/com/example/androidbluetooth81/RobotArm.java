package com.example.androidbluetooth81;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class RobotArm extends AppCompatActivity {
    private static final String FILE_NAME = "example.txt";
    private EditText editText;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String newAddress = null;
    private SeekBar mSpeed;
    private SeekBar mGrip;
    private SeekBar mWristRitch;
    private SeekBar mWristRoll;
    private SeekBar mElbow;
    private SeekBar mShoulder;
    private SeekBar mWaist;
    private TextView mProgressTv;
    Button Save, Run, Reset, Stop, datain, dataout;
    Spinner spinner;
    static String data = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_arm);
        //藍芽
        addKeyListener();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        //導入按鈕
        Save = (Button) findViewById(R.id.Save);
        Run = (Button) findViewById(R.id.Run);
        Reset = (Button) findViewById(R.id.Reset);
        Stop = (Button) findViewById(R.id.Stop);
        datain = (Button) findViewById(R.id.datain);
        dataout = (Button) findViewById(R.id.dataout);
        spinner = (Spinner)findViewById(R.id.spinner);
        //按鈕觸發設定
        Save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(72);
                Toast.makeText(getBaseContext(), "儲存中", Toast.LENGTH_SHORT).show();
            }
        });
        Run.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(73);
                Toast.makeText(getBaseContext(), "執行中", Toast.LENGTH_SHORT).show();
            }
        });
        Reset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(74);
                Toast.makeText(getBaseContext(), "重啟中", Toast.LENGTH_SHORT).show();
                mGrip.setProgress(95);
                mWristRitch.setProgress(135);
                mWristRoll.setProgress(100);
                mElbow.setProgress(50);
                mShoulder.setProgress(80);
                mWaist.setProgress(90);
                mSpeed.setProgress(5);
            }
        });
        Stop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(75);
                Toast.makeText(getBaseContext(), "停止中", Toast.LENGTH_SHORT).show();
            }
        });
        datain.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name.isEmpty() == true)
                {
                    Toast.makeText(getBaseContext(), "名稱不可為空白", Toast.LENGTH_SHORT).show();
                }else{

                    //先讀取資料確認是否有其他設置
                    FileInputStream fis1 = null;
                    try {
                        fis1 = openFileInput(FILE_NAME);
                        InputStreamReader isr1 = new InputStreamReader(fis1);
                        BufferedReader br1 = new BufferedReader(isr1);
                        StringBuilder sb1 = new StringBuilder();
                        String text1;
                        while ((text1 = br1.readLine()) != null) {
                            sb1.append(text1).append("\n");
                        }
                        data = sb1.toString();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis1 != null) {
                            try {
                                fis1.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    //獲取各軸值
                    int Grip = mGrip.getProgress();
                    int WristRitch = mWristRitch.getProgress();
                    int WristRoll = mWristRoll.getProgress();
                    int Elbow = mElbow.getProgress();
                    int Shoulder = mShoulder.getProgress();
                    int Waist = mWaist.getProgress();
                    int Speed = mSpeed.getProgress();
                    //寫入資料
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);//Context.MODE_PRIVATE：為默認操作模式，代表該文件是私有數據，只能被應用本身訪問，在該模式下，寫入的內容會覆蓋原文件的內容，如果想把新寫入的內容追加到原文件中。可以使用Context.MODE_APPEND
                        if(data.isEmpty() == true)
                        {

                            String iuiu = name+","+Grip+","+WristRitch+","+WristRoll+","+Elbow+","+Shoulder+","+Waist+","+Speed;
                            fos.write(iuiu.getBytes());
                        }else{

                            String iuiu = data+","+name+","+Grip+","+WristRitch+","+WristRoll+","+Elbow+","+Shoulder+","+Waist+","+Speed;
                            fos.write(iuiu.getBytes());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //讀取資料
                    FileInputStream fis2 = null;
                    try {
                        fis2 = openFileInput(FILE_NAME);
                        InputStreamReader isr2 = new InputStreamReader(fis2);
                        BufferedReader br2 = new BufferedReader(isr2);
                        StringBuilder sb2 = new StringBuilder();
                        String text2;
                        while ((text2 = br2.readLine()) != null) {
                            sb2.append(text2).append("\n");
                        }
                        String test = sb2.toString();
                        //按逗號分割
                        final String[] tempor = test.split(",");
                        //取出資料總數，及資料名稱
                        int count = tempor.length;
                        int total = 0;
                        do{
                            if(count/8>=1){
                                count=count-8;
                                total++;
                            }
                        }while(count / 8 >= 1);
                        //把資料丟進下拉式方塊
                        final String[] lunch = new String[total];
                        for(int i = 0 ; i < total ; i++)
                        {
                            lunch[i]=tempor[0+i*8];
                            //mProgressTv.setText(total+"後"+name+"尾");
                        }
                        ArrayAdapter<String> lunchList = new ArrayAdapter<>(RobotArm.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                lunch);spinner.setAdapter(lunchList);
                         spinner.setAdapter(lunchList);
                         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                             @Override
                             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                 int index = position*8;
                                 Toast.makeText(getBaseContext(), index+"您選擇了:" + lunch[position], Toast.LENGTH_SHORT).show();
                                 mGrip.setProgress(Integer.parseInt(tempor[index+1]));
                                 mWristRitch.setProgress(Integer.parseInt(tempor[index+2]));
                                 mWristRoll.setProgress(Integer.parseInt(tempor[index+3]));
                                 mElbow.setProgress(Integer.parseInt(tempor[index+4]));
                                 mShoulder.setProgress(Integer.parseInt(tempor[index+5]));
                                 mWaist.setProgress(Integer.parseInt(tempor[index+6]));
                                 //mSpeed.setProgress(Integer.parseInt(tempor[index+7]));
                             }
                             @Override
                             public void onNothingSelected(AdapterView<?> parent) {

                             }
                         });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis2 != null) {
                            try {
                                fis2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    editText.setText("");
                    data="";
                }
            }
        });
        dataout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    String iuiu = "";
                    fos.write(iuiu.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                final String[] lunch = {};
                ArrayAdapter<String> lunchList = new ArrayAdapter<>(RobotArm.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        lunch);spinner.setAdapter(lunchList);
                Toast.makeText(getBaseContext(), "已刪除所有資料", Toast.LENGTH_SHORT).show();
            }
        });
        //SeekBar模塊導入
        mProgressTv = (TextView) findViewById(R.id.pb_tv); // %數textview
        mGrip = (SeekBar) findViewById(R.id.Grip);
        mGrip.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(70);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mWristRitch = (SeekBar) findViewById(R.id.WristRitch);
        mWristRitch.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(69);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mWristRoll = (SeekBar) findViewById(R.id.WristRoll);
        mWristRoll.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(68);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mElbow = (SeekBar) findViewById(R.id.Elbow);
        mElbow.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(67);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mShoulder = (SeekBar) findViewById(R.id.Shoulder);
        mShoulder.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(66);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mWaist = (SeekBar) findViewById(R.id.Waist);
        mWaist.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(65);
                sendData(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSpeed = (SeekBar) findViewById(R.id.Speed);
        mSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            //進度變更過程觸發，設定進度改變時要做的事
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressTv.setText(""+progress +"%");
                sendData(71);
                sendData(progress);
            }
            @Override
            //被使用者觸摸的當下觸發
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            //使用者手指離開 SeekBar 時當下觸發
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }//開啟前執行
    //藍芽
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        newAddress = intent.getStringExtra(DeviceList.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = btAdapter.getRemoteDevice(newAddress);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e1) {
            Toast.makeText(getBaseContext(), "ERROR - Could not create Bluetooth socket", Toast.LENGTH_SHORT).show();
        }
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Toast.makeText(getBaseContext(), "ERROR - Could not close Bluetooth socket", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR - Could not create bluetooth outstream", Toast.LENGTH_SHORT).show();
        }
    }//手機執行中
    @Override
    public void onPause() {
        super.onPause();
        try     {
            btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "ERROR - Failed to close Bluetooth socket", Toast.LENGTH_SHORT).show();
        }
    }//關閉介面時
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    private void checkBTState() {
        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "ERROR - Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    //傳輸資料
    private void sendData(int message) {
        try {
            outStream.write(message);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "錯誤 - 設備無開啟藍芽或距離太遠", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void addKeyListener() {
        editText = (EditText) findViewById(R.id.editText1);
        editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    //sendData();
                    Toast.makeText(getBaseContext(), "發送中", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}