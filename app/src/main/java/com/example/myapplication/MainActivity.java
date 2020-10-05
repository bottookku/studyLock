package com.example.myapplication;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import static com.example.myapplication.Preferences.loadAllListApps;



public class MainActivity extends Activity {
    public static Map<String,Integer> listApp;
    public static String nowUnlockPackageName = "sexuality";
    public static boolean currentUnlocked = false;

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Preferences(getApplicationContext());
        loadAllListApps();
        Preferences.saveString("password","123");


        setContentView(R.layout.list_app);
        LinearLayout ln = findViewById(R.id.list_app_lin);
        goSetting();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(new Intent(getApplicationContext(), MyService.class));
        }else {
            getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(getApplicationContext())) {
            RequestPermission();
        }



        final PackageManager pm = getApplication().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);

        for(final ResolveInfo app : apps ){

            PackageManager packageManager = getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(app.activityInfo.packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {}
            final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");

            ViewGroup child = (ViewGroup) getLayoutInflater().inflate(R.layout.item_app,null);
            ((TextView)child.findViewById(R.id.textView)).setText(title);
            ImageView img = child.findViewById(R.id.imageView);
            img.setImageDrawable(app.activityInfo.loadIcon(getApplication().getPackageManager()));
            final ImageButton btn = child.findViewById(R.id.button2);
            final int i[] = new int[1];
            i[0] = Preferences.getInt(app.activityInfo.packageName);
            setDrawble(i[0],btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(i[0] ==2){
                        i[0] = -1;
                    }
                    i[0]++;
                    setDrawble(i[0],btn);
                    Preferences.saveInt(app.activityInfo.packageName,i[0]);
                    if(listApp.containsKey(app.activityInfo.packageName)) {
                        listApp.remove(app.activityInfo.packageName);
                    }
                    listApp.put(app.activityInfo.packageName,i[0]);
                }
            });
            ln.addView(child);
        }
    }

    void setDrawble(int i, ImageButton btn){
        switch (i){
            case 0:{
                btn.setBackground(getResources().getDrawable(R.mipmap.unlock));
                break;
            }
            case 1:{
                btn.setBackground(getResources().getDrawable(R.mipmap.lock));
                break;
            }
            case 2:{
                btn.setBackground(getResources().getDrawable(R.mipmap.study));
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        goSetting();
    }


    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(getApplicationContext().APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void RequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplication().getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }
    private void goSetting(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}
