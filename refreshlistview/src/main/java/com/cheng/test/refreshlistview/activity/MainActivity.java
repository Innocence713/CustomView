package com.cheng.test.refreshlistview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheng.test.refreshlistview.R;
import com.cheng.test.refreshlistview.view.RefreshListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private RefreshListView rf_listview;
    private ArrayList<String> mDataList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        rf_listview = (RefreshListView) findViewById(R.id.rf_listview);

        rf_listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println("下拉刷新出来的数据！");
                        mDataList.add(0, "下拉刷新出来的数据！");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                myAdapter.notifyDataSetChanged();
                                rf_listview.refreshComplete();
                            }
                        });
                    };
                }.start();

            }

            @Override
            public void loadMore() {
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println("加载更多的数据！");
                        mDataList.add(mDataList.size(), "加载更多的数据！");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                myAdapter.notifyDataSetChanged();
                                rf_listview.refreshComplete();
                            }
                        });
                    };
                }.start();

            }
        });

        mDataList = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            mDataList.add("ListView数据：" + i);
        }

        myAdapter = new MyAdapter();
        rf_listview.setAdapter(myAdapter);

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mDataList.size();
        }

        @Override
        public String getItem(int position) {

            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.list_text_item, null);
            }
            TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
            tv_item.setText(mDataList.get(position));

            return convertView;
        }

    }
}

