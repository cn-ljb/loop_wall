package com.ljb.loopwall;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by L on 2017/4/12.
 */

public abstract class BaseLoopWallAdapter<T> implements LoopWallAdapter {

    private Context mContext;
    private List<T> mData;

    public BaseLoopWallAdapter(Context c, List<T> data) {
        mContext = c;
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public abstract View getView(int position, ViewGroup parent);
}
