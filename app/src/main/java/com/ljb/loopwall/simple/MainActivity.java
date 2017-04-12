package com.ljb.loopwall.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ljb.loopwall.LoopWallView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LoopWallView mLoopWallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mLoopWallView = (LoopWallView) findViewById(R.id.loop_view);
        mLoopWallView.setScrollV(0.2f); // 设置滚动速度，值越大，速度越快（默认0.2f）
    }

    private void initData() {
        getDataFromXX();
    }

    private void getDataFromXX() {
        //模拟数据
        List<ModeItem> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            String index = i < 10 ? "0" + i : "" + i;
            String phone = "188****88" + index;
            String time = "2017-04-12 12：" + index;
            String prize = "礼品" + index;
            data.add(new ModeItem(phone, time, prize));
        }

        mLoopWallView.setAdapter(new MyAdapter(this, data));
    }


}
