package com.ljb.loopwall;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by L on 2017/4/12.
 */

public interface LoopWallAdapter {

    int getCount();

    Object getItem(int position);

    View getView(int position, ViewGroup parent);
}
