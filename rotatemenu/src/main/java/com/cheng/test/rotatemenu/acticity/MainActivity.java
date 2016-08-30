package com.cheng.test.rotatemenu.acticity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cheng.test.rotatemenu.R;
import com.cheng.test.rotatemenu.utils.AnimationUtils;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String Tag = "MainActivity";
    private RelativeLayout rl_level1;
    private RelativeLayout rl_level2;
    private RelativeLayout rl_level3;
    boolean isLevel1Display = true;
    boolean isLevel2Display = true;
    boolean isLevel3Display = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        // 找到控件，设置点击 事件
        findViewById(R.id.ib_home).setOnClickListener(this);
        findViewById(R.id.ib_menu).setOnClickListener(this);

        rl_level1 = (RelativeLayout) findViewById(R.id.rl_level1);
        rl_level2 = (RelativeLayout) findViewById(R.id.rl_level2);
        rl_level3 = (RelativeLayout) findViewById(R.id.rl_level3);

    }

    @Override
    public void onClick(View v) {
        if (AnimationUtils.runningAnimCount > 1) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.ib_home:
                    if (isLevel2Display) {
                        // 如果Level2已显示，则转出去
                        long delay = 0;
                        if (isLevel3Display) {
                            // 如果Level3已显示，则转出去
                            isLevel3Display = false;
                            AnimationUtils.rotateOutAnim(rl_level3, 0);
                            delay += 100;

                        }
                        isLevel2Display = false;
                        AnimationUtils.rotateOutAnim(rl_level2, delay);

                    } else {
                        // 如果Level2未显示，则转出来
                        isLevel2Display = true;
                        AnimationUtils.rotateInAnim(rl_level2, 0);
                    }

                    break;
                case R.id.ib_menu:
                    if (isLevel3Display) {
                        // 如果Level3已显示，则转出去
                        isLevel3Display = false;
                        AnimationUtils.rotateOutAnim(rl_level3, 0);

                    } else {
                        // 如果Level3未显示，则转出来
                        isLevel3Display = true;
                        AnimationUtils.rotateInAnim(rl_level3, 0);
                    }
                    break;
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        System.out.println("keyCode:" + keyCode);
        // 当按下菜单键时
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (AnimationUtils.runningAnimCount > 1) {
                return true;
            }
            if (isLevel1Display) {
                if (isLevel2Display) {
                    if (isLevel3Display) {
                        isLevel3Display = false;
                        AnimationUtils.rotateOutAnim(rl_level3, 0);
                    }

                    isLevel2Display = false;
                    AnimationUtils.rotateOutAnim(rl_level2, 100);
                }

                isLevel1Display = false;
                AnimationUtils.rotateOutAnim(rl_level1, 200);
            } else {

                isLevel1Display = true;
                AnimationUtils.rotateInAnim(rl_level1, 0);
                isLevel2Display = true;
                AnimationUtils.rotateInAnim(rl_level2, 100);
                isLevel3Display = true;
                AnimationUtils.rotateInAnim(rl_level3, 200);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
