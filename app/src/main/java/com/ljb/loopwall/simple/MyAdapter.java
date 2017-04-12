package com.ljb.loopwall.simple;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljb.loopwall.BaseLoopWallAdapter;

import java.util.List;

/**
 * Created by L on 2017/4/12.
 */

class MyAdapter extends BaseLoopWallAdapter<ModeItem> {

    public MyAdapter(Context c, List<ModeItem> data) {
        super(c, data);
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = View.inflate(getContext(), R.layout.item, null);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_prize = (TextView) view.findViewById(R.id.tv_thing);

        ModeItem item = getItem(position);
        tv_phone.setText(item.getPhone());
        tv_time.setText(item.getTime());
        tv_prize.setText(item.getPrize());
        return view;
    }
}
