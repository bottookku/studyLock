package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
public class Lock extends Activity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);
        final EditText pass = findViewById(R.id.editTextNumberPassword);
        Button btn = findViewById(R.id.button);
        final String pass_ok = Preferences.getString("password");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pas = pass.getText().toString();
                Log.e("AAA",pas+ " = "+ pass_ok);
                if(pas.equals(pass_ok)){
                    MainActivity.currentUnlocked = true;
                    finish();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
