package com.cheng.test.quickindexbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    private QuickIndexBar quickIndexBar;
    private ListView listView;
    private ArrayList<Friend> friendsList = new ArrayList<Friend>();
    private TextView textBubble;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
        listView = (ListView) findViewById(R.id.listView);
        textBubble = (TextView) findViewById(R.id.textBubble);

        // 1.初始化数据
        initData();

        // 2.将数据排序
        Collections.sort(friendsList);

        // 3.设置适配器
        listView.setAdapter(new MyAdapter(this, friendsList));

        //通过缩小气泡来影藏
        ViewHelper.setScaleX(textBubble, 0);
        ViewHelper.setScaleY(textBubble, 0);

        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {

            @Override
            public void onTouchLetter(String letter) {

                for (int i = 0; i < friendsList.size(); i++) {
                    if (letter.equals(friendsList.get(i).getPinyin().charAt(0) + "")) {
                        listView.setSelection(i);
                        break;
                    }
                }

                showCurrentWord(letter);
            }
        });


    }

    private boolean isScale = false;

    protected void showCurrentWord(String letter) {
//		textBubble.setVisibility(View.VISIBLE);
        textBubble.setText(letter);

        if (!isScale) {
            isScale = true;
            ViewPropertyAnimator.animate(textBubble)
                    .scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(400)
                    .start();
            ViewPropertyAnimator.animate(textBubble)
                    .scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(400)
                    .start();

        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                ViewPropertyAnimator.animate(textBubble)
                        .scaleX(0)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(400)
                        .start();
                ViewPropertyAnimator.animate(textBubble)
                        .scaleY(0)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(400)
                        .start();
                isScale = false;
            }
        }, 1000);
    }

    public void initData() {
        // 虚拟数据
        friendsList.add(new Friend("李伟"));
        friendsList.add(new Friend("张三"));
        friendsList.add(new Friend("阿三"));
        friendsList.add(new Friend("阿四"));
        friendsList.add(new Friend("段誉"));
        friendsList.add(new Friend("段正淳"));
        friendsList.add(new Friend("张三丰"));
        friendsList.add(new Friend("陈坤"));
        friendsList.add(new Friend("林俊杰1"));
        friendsList.add(new Friend("陈坤2"));
        friendsList.add(new Friend("王二a"));
        friendsList.add(new Friend("林俊杰a"));
        friendsList.add(new Friend("张四"));
        friendsList.add(new Friend("林俊杰"));
        friendsList.add(new Friend("王二"));
        friendsList.add(new Friend("王二b"));
        friendsList.add(new Friend("赵四"));
        friendsList.add(new Friend("杨坤"));
        friendsList.add(new Friend("赵子龙"));
        friendsList.add(new Friend("杨坤1"));
        friendsList.add(new Friend("李伟1"));
        friendsList.add(new Friend("宋江"));
        friendsList.add(new Friend("宋江1"));
        friendsList.add(new Friend("李伟3"));
    }
}
