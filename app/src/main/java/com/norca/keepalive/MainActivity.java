package com.norca.keepalive;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okio.Okio;


/**
 * 这个demo结合前台service和一像素activity保活的方法，
 * 只使用前台service在息屏后很可能会被杀死，在这台华为的6.0中，若没有添加为白名单，必然会被杀死。
 * 而一像素保活只能确保不会在息屏时被杀死。
 *
 * 在6.0中，同时使用这两个方法，依然可以手动清理后台杀死
 * 而在4.2中，几乎无法被杀死
 *
 * 在不被杀死的情况下，service的网络访问都能成功
 */
public class MainActivity extends AppCompatActivity {
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("count","onCreate");
        //setContentView(R.layout.activity_main);
        Log.i("count",getPackageName()+ " " +getComponentName());
        final Button floatingButton = new Button(this);
        floatingButton.setText("button");
        floatingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this,"adafsaefsafea",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );
        // flag 设置 Window 属性
        layoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // type 设置 Window 类别（层级）
        layoutParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        layoutParams.gravity = Gravity.LEFT |Gravity.TOP;
        WindowManager windowManager = getWindowManager();
        windowManager.addView(floatingButton, layoutParams);
        Okio
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("count","onResume");
    }

    public void startKeepAliveService(View v){
        Intent intent = new Intent(this,KeepLiveService.class);
        startService(intent);
    }

    public void registerReceiver(View v){
        //注册广播接受屏幕开关广播
       OnepxReceiver.register1pxReceiver(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;
//                while (true){
//                    try {
//                        url = new URL("https://www.baidu.com");
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                        String string = bufferedReader.readLine();
//                        Log.i("count",string);
//                        if (string != null){
//                            count++;
//                            Log.i("count","访问网络次数："+count);
//                        }
//                        Thread.sleep(10000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }).start();
    }
}
