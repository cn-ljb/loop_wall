package com.ljb.loopwall;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by L on 2017/4/12.
 */
public class LoopWallView extends ScrollView implements View.OnTouchListener {

    private LinearLayout mLoopView1;
    private LinearLayout mLoopView2;
    private LoopWallRunnable mLoopWallTask;

    public LoopWallView(Context context) {
        super(context);
        init();
    }

    public LoopWallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoopWallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initView();
        mLoopWallTask = new LoopWallRunnable();
    }

    private void initView() {
        setOnTouchListener(this);
        FrameLayout wallContent = new FrameLayout(getContext());
        mLoopView1 = new LinearLayout(getContext());
        mLoopView2 = new LinearLayout(getContext());
        mLoopView1.setOrientation(LinearLayout.VERTICAL);
        mLoopView2.setOrientation(LinearLayout.VERTICAL);
        wallContent.addView(mLoopView1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        wallContent.addView(mLoopView2, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(wallContent, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLoopView2.setVisibility(View.INVISIBLE);
    }

    public void setScrollV(float v) {
        mLoopWallTask.setScrollV(v);
    }


    public void setAdapter(LoopWallAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            mLoopView1.addView(adapter.getView(i, mLoopView1));
            mLoopView2.addView(adapter.getView(i, mLoopView2));
        }

        post(mLoopWallTask);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private class LoopWallRunnable implements Runnable {

        //滚动的速度
        private float mScrollV = 0.2f;

        private ValueAnimator mTranslationAnim1;
        private ValueAnimator mTranslationAnim2;

        @Override
        public void run() {
            initAnim();
            mTranslationAnim1.start();
        }

        private void initAnim() {
            //第一墙动画
            final int offset1 = measureViewHeight(mLoopView1);
            Log.d("58che", offset1 + "");
            mTranslationAnim1 = ObjectAnimator.ofFloat(mLoopView1, "translationY", getHeight(), -offset1);
            mTranslationAnim1.setDuration((long) ((getHeight() + offset1) / mScrollV));
            mTranslationAnim1.setInterpolator(new LinearInterpolator());
            mTranslationAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    double value = Double.parseDouble(animation.getAnimatedValue().toString());
                    if (value <= getHeight() - offset1) {
                        if (mTranslationAnim2 == null || mTranslationAnim2.isRunning()) {
                            return;
                        }
                        mTranslationAnim2.start();

                        mLoopView2.setVisibility(View.VISIBLE);
                    }
                }
            });

            //第二墙动画
            final int offset2 = measureViewHeight(mLoopView2);
            Log.d("58che", offset2 + "");
            mTranslationAnim2 = ObjectAnimator.ofFloat(mLoopView2, "translationY", getHeight(), -offset2);
            mTranslationAnim2.setDuration((long) ((getHeight() + offset1) / mScrollV));
            mTranslationAnim2.setInterpolator(new LinearInterpolator());
            mTranslationAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    double value = Double.parseDouble(animation.getAnimatedValue().toString());
                    if (value <= getHeight() - offset2) {
                        if (mTranslationAnim1 == null || mTranslationAnim1.isRunning()) {
                            return;
                        }
                        mTranslationAnim1.start();
                    }
                }
            });
        }

        public void setScrollV(float v) {
            this.mScrollV = v;
        }
    }

    public int measureViewHeight(View view) {
        int measuredWidth = view.getMeasuredWidth();
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY); //宽度不变，精确值
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); //自动计算，最高上限
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight();
    }
}
