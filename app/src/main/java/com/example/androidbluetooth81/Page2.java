package com.example.androidbluetooth81;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Page2 extends AppCompatActivity  {
    private Button goledctrl, gorobotarmctrl;
    static int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        setContentView(R.layout.activity_page2);
        goledctrl = (Button) findViewById(R.id.goledctrl);
        gorobotarmctrl = (Button) findViewById(R.id.gorobotarmctrl);
        goledctrl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                a=1;
                chose();
            }
        });
        gorobotarmctrl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                a=2;
                chose();
            }
        });
    }
    public void chose(){
        if( a == 1)
        {
            Intent intent = new Intent();
            intent.setClass(Page2.this,DeviceList.class);
            Bundle bundle = new Bundle();
            bundle.putString("chose","1");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if ( a == 2)
        {
            Intent intent = new Intent();
            intent.setClass(Page2.this,DeviceList.class);
            Bundle bundle = new Bundle();
            bundle.putString("chose","2");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
