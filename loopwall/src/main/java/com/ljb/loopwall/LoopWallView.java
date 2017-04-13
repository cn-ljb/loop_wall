package com.ljb.loopwall;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
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

    public void pause() {
        mLoopWallTask.pause();
    }

    public void resume() {
        mLoopWallTask.resume();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    protected void onDetachedFromWindow() {
        //停止动画，释放View
        mLoopWallTask.destroy();
        super.onDetachedFromWindow();
    }

    private class LoopWallRunnable implements Runnable {

        //滚动的速度
        private float mScrollV;
        private ValueAnimator mTranslationAnim1;
        private ValueAnimator mTranslationAnim2;
        private int mOffsetH1;
        private int mOffsetH2;
        private int mPauseH1;
        private int mPauseH2;
        private boolean mPause;

        private AnimListener1 mAnimListener1;
        private AnimListener2 mAnimListener2;

        LoopWallRunnable() {
            mAnimListener1 = new AnimListener1();
            mAnimListener2 = new AnimListener2();
            mPauseH1 = mPauseH2 = Integer.MIN_VALUE;
            mPause = false;
            mScrollV = 0.2f;
        }


        @Override
        public void run() {
            mOffsetH1 = measureViewHeight(mLoopView1);
            mOffsetH2 = measureViewHeight(mLoopView2);
            startAnim1();
        }


        void pause() {
            if (mPause) {
                return;
            }
            mPause = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (mTranslationAnim1.isStarted() && mTranslationAnim1.isRunning()) {
                    mTranslationAnim1.pause();
                }
                if (mTranslationAnim2.isStarted() && mTranslationAnim2.isRunning()) {
                    mTranslationAnim2.pause();
                }
            } else {
                if (mTranslationAnim1 != null && mTranslationAnim1.isStarted() && mTranslationAnim1.isRunning()) {
                    mPauseH1 = mAnimListener1.getTempPauseH1();
                    mTranslationAnim1.cancel();
                } else {
                    //动画1已结束，初始化为原始值
                    mPauseH1 = Integer.MIN_VALUE;
                }


                if (mTranslationAnim2 != null && mTranslationAnim2.isStarted() && mTranslationAnim2.isRunning()) {
                    mPauseH2 = mAnimListener2.getTempPauseH2();
                    mTranslationAnim2.cancel();
                } else {
                    //动画2已结束，初始化为原始值
                    mPauseH2 = Integer.MIN_VALUE;
                }

                Log.d("58che", "anim1:" + mPauseH1);
                Log.d("58che", "anim2:" + mPauseH2);
            }
        }

        void resume() {
            if (!mPause) {
                return;
            }
            mPause = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (mTranslationAnim1.isPaused()) {
                    mTranslationAnim1.resume();
                }
                if (mTranslationAnim2.isPaused()) {
                    mTranslationAnim2.resume();
                }
            } else {
                if (mPauseH1 != Integer.MIN_VALUE) {
                    startPauseAnim1();
                }

                if (mPauseH2 != Integer.MIN_VALUE) {
                    startPauseAnim2();
                }

                //恢复时由于互相监听会出现bug，所以先启动动画，再加监听器
                if (mPauseH1 != Integer.MIN_VALUE) {
                    mTranslationAnim1.addUpdateListener(mAnimListener1);
                }

                if (mPauseH2 != Integer.MIN_VALUE) {
                    mTranslationAnim2.addUpdateListener(mAnimListener2);
                }
            }
        }

        private void startPauseAnim1() {
            //第一墙暂停后恢复动画
            Log.d("58che", "恢复anim1：" + mPauseH1);
            int pauseS = getHeight() + mOffsetH1 - (getHeight() - mPauseH1);
            mTranslationAnim1 = ObjectAnimator.ofFloat(mLoopView1, "translationY", mPauseH1, -mOffsetH1);
            mTranslationAnim1.setDuration((long) (pauseS / mScrollV));
            mTranslationAnim1.setInterpolator(new LinearInterpolator());
            mTranslationAnim1.start();
        }

        private void startPauseAnim2() {
            //第二墙暂停后恢复动画
            Log.d("58che", "恢复anim2：" + mPauseH2);
            int pauseS = getHeight() + mOffsetH2 - (getHeight() - mPauseH2);
            mTranslationAnim2 = ObjectAnimator.ofFloat(mLoopView2, "translationY", mPauseH2, -mOffsetH2);
            mTranslationAnim2.setDuration((long) (pauseS / mScrollV));
            mTranslationAnim2.setInterpolator(new LinearInterpolator());
            mTranslationAnim2.start();
        }


        private void startAnim1() {
            //第一墙动画
            Log.d("58che", "开始anim1");
            mTranslationAnim1 = ObjectAnimator.ofFloat(mLoopView1, "translationY", getHeight(), -mOffsetH1);
            mTranslationAnim1.setDuration((long) ((getHeight() + mOffsetH1) / mScrollV));
            mTranslationAnim1.setInterpolator(new LinearInterpolator());
            mTranslationAnim1.addUpdateListener(mAnimListener1);
            mTranslationAnim1.start();
        }

        private void startAnim2() {
            //第二墙动画
            Log.d("58che", "开始anim2");
            mTranslationAnim2 = ObjectAnimator.ofFloat(mLoopView2, "translationY", getHeight(), -mOffsetH2);
            mTranslationAnim2.setDuration((long) ((getHeight() + mOffsetH1) / mScrollV));
            mTranslationAnim2.setInterpolator(new LinearInterpolator());
            mTranslationAnim2.addUpdateListener(mAnimListener2);
            mTranslationAnim2.start();
        }


        void setScrollV(float v) {
            this.mScrollV = v;
        }


        void destroy() {
            mLoopView1.clearAnimation();
            mLoopView2.clearAnimation();

            if (mTranslationAnim1 != null) {
                mTranslationAnim1.removeAllUpdateListeners();
                mTranslationAnim1.cancel();
                mTranslationAnim1 = null;
            }

            if (mTranslationAnim2 != null) {
                mTranslationAnim2.removeAllUpdateListeners();
                mTranslationAnim2.cancel();
                mTranslationAnim2 = null;
            }
        }


        //第二墙动画监听器
        private class AnimListener2 implements ValueAnimator.AnimatorUpdateListener {

            private int tempPauseH2;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tempPauseH2 = (int) Double.parseDouble(animation.getAnimatedValue().toString());
                if (tempPauseH2 <= getHeight() - mOffsetH2 && !mPause) {
                    if (mTranslationAnim1 != null && mTranslationAnim1.isRunning()) {
                        return;
                    }

                    startAnim1();
                }
            }

            public int getTempPauseH2() {
                return tempPauseH2;
            }
        }

        //第一墙动画监听器
        private class AnimListener1 implements ValueAnimator.AnimatorUpdateListener {

            private int tempPauseH1;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tempPauseH1 = (int) Double.parseDouble(animation.getAnimatedValue().toString());
                if (tempPauseH1 <= getHeight() - mOffsetH1 && !mPause) {
                    if (mTranslationAnim2 != null && mTranslationAnim2.isRunning()) {
                        return;
                    }
                    if (mTranslationAnim2 == null) {
                        mLoopView2.setVisibility(View.VISIBLE);
                    }
                    startAnim2();
                }
            }

            public int getTempPauseH1() {
                return tempPauseH1;
            }
        }
    }

    public int measureViewHeight(View view) {
        int measuredWidth = view.getMeasuredWidth();
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight();
    }
}
