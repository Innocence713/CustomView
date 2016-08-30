package com.cheng.test.toggleview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.cheng.test.toggleview.R;
import com.cheng.test.toggleview.ui.ToggleView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleView toggleView = (ToggleView) findViewById(R.id.toggleView);

        //设置开关更新监听
        toggleView.setOnSwitchStateUpdateListener(new ToggleView.OnSwitchStateUpdateListener() {

            @Override
            public void stateUpdate(boolean state) {

                Toast.makeText(MainActivity.this, "state:" + state, Toast.LENGTH_SHORT).show();

            }

        });
    }

}

