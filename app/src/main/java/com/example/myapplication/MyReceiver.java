package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    static MyReceiver myReceiver;
    public static MyReceiver getReciever(){
        if(myReceiver!=null) {
            return myReceiver;
        }
        myReceiver = new MyReceiver();
        return myReceiver;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Toast.makeText(context,"SUKA",Toast.LENGTH_LONG).show();
        }
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            }else {
                context.startService(new Intent(context, MyService.class));
            }
        }
    }
}
