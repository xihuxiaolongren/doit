package me.xihuxiaolong.justdoit.common.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import me.xihuxiaolong.justdoit.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/15.
 */

public class NightBackgroundView extends FrameLayout{

    private static final int ANIMATION_DURATION = 2000;

    private View mSunView;
    private View mSkyView;
    private View mStar1View, mStar2View, mStar3View, mStar4View, mStar5View;

    private ObjectAnimator mSunAnim, mSunXAnim;
    private ObjectAnimator mSkyAnim;
    private ObjectAnimator mStarX1Anim, mStarX2Anim, mStarX3Anim, mStarX4Anim, mStarX5Anim;
    private ObjectAnimator mStarY1Anim, mStarY2Anim, mStarY3Anim, mStarY4Anim, mStarY5Anim;
    private ObjectAnimator mStarR1Anim, mStarR2Anim, mStarR3Anim, mStarR4Anim, mStarR5Anim;

    private AnimatorSet mSunsetAnimSet;
    private AnimatorSet mSunriseAnimSet;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;

    public NightBackgroundView(Context context) {
        super(context);
        init(context);
    }

    public NightBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NightBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NightBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.night_background_view, this);

        mSunView = findViewById(R.id.sun);
        mSkyView = findViewById(R.id.sky);
        mStar1View = findViewById(R.id.star1);
        mStar2View = findViewById(R.id.star2);
        mStar3View = findViewById(R.id.star3);
        mStar4View = findViewById(R.id.star4);
        mStar5View = findViewById(R.id.star5);

        mBlueSkyColor = ContextCompat.getColor(context, R.color.sky);
        mSunsetSkyColor = ContextCompat.getColor(context, R.color.sunset_sky);

        post(new Runnable() {
            @Override
            public void run() {
                sunrise();
            }
        });

//        mSkyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ((mSunView.getY() >= mSkyView.getHeight()) ||
//                        mSunsetAnimSet != null && mSunsetAnimSet.isRunning()) {
//                    /*
//                     * 太阳上升的情况只有两种
//                     * 1) 太阳刚刚落到底
//                     * 2) 太阳正在落下
//                     */
//                    sunrise();
//                } else {
//                    // 其他情况，太阳上升
//                    sunset();
//                }
//            }
//
//        });
    }

    /**
     * 太阳升起
     */
    private void sunrise() {
        // 如果太阳正在落下
        if (mSunsetAnimSet != null && mSunsetAnimSet.isRunning()) {
            long playTime = ANIMATION_DURATION - mSunAnim.getCurrentPlayTime();

            mSunAnim = getSunAnimator((float) mSunAnim.getAnimatedValue(), mSunView.getTop());
            mSunAnim.setCurrentPlayTime(playTime);

            mSkyAnim = getSkyAnimator((int) mSkyAnim.getAnimatedValue(), mBlueSkyColor);
            mSkyAnim.setCurrentPlayTime(playTime);

            // 停止太阳下落的动画
            mSunsetAnimSet.end();
        } else {
            mSunAnim = getSunAnimator(mSkyView.getHeight(), getContext().getResources().getDimensionPixelSize(R.dimen.sun_top));
            mSunXAnim = getSunXAnimator(0, mSunView.getLeft());
            mSkyAnim = getSkyAnimator(mSunsetSkyColor, mBlueSkyColor);
            mStarX1Anim = getStarXAnimator(mStar1View, 0.0f, 1.0f);
            mStarX2Anim = getStarXAnimator(mStar2View, 0.0f, 1.0f);
            mStarX3Anim = getStarXAnimator(mStar3View, 0.0f, 1.0f);
            mStarX4Anim = getStarXAnimator(mStar4View, 0.0f, 1.0f);
            mStarX5Anim = getStarXAnimator(mStar5View, 0.0f, 1.0f);
            mStarY1Anim = getStarYAnimator(mStar1View, 0.0f, 1.0f);
            mStarY2Anim = getStarYAnimator(mStar2View, 0.0f, 1.0f);
            mStarY3Anim = getStarYAnimator(mStar3View, 0.0f, 1.0f);
            mStarY4Anim = getStarYAnimator(mStar4View, 0.0f, 1.0f);
            mStarY5Anim = getStarYAnimator(mStar5View, 0.0f, 1.0f);
            mStarR1Anim = getStarRotationAnimator(mStar1View, 0.0f, 10f);
            mStarR2Anim = getStarRotationAnimator(mStar2View, 0.0f, 0.0f);
            mStarR3Anim = getStarRotationAnimator(mStar3View, 0.0f, 270f);
            mStarR4Anim = getStarRotationAnimator(mStar4View, 0.0f, 10f);
            mStarR5Anim = getStarRotationAnimator(mStar5View, 0.0f, 270f);
//            mCloud2Anim = getCloud2Animator(0, mCloud2View.getRight());
        }
        mSunriseAnimSet = new AnimatorSet();
        mSunriseAnimSet.play(mSunAnim).with(mSunXAnim)
                .with(mSkyAnim)
                .with(mStarX1Anim).with(mStarX2Anim).with(mStarX3Anim).with(mStarX4Anim).with(mStarX5Anim)
                .with(mStarY1Anim).with(mStarY2Anim).with(mStarY3Anim).with(mStarY4Anim).with(mStarY5Anim)
                .with(mStarR1Anim).with(mStarR2Anim).with(mStarR3Anim).with(mStarR4Anim).with(mStarR5Anim);
        mSunriseAnimSet.start();
    }

    /**
     * 太阳落下
     */
    private void sunset() {
        // 如果太阳正在升起
        if (mSunriseAnimSet != null && mSunriseAnimSet.isRunning()) {
            long playTime = ANIMATION_DURATION - mSunAnim.getCurrentPlayTime();

            mSunAnim = getSunAnimator((float) mSunAnim.getAnimatedValue(), mSkyView.getHeight());
            mSunAnim.setCurrentPlayTime(playTime);

            mSkyAnim = getSkyAnimator((int) mSkyAnim.getAnimatedValue(), mSunsetSkyColor);
            mSkyAnim.setCurrentPlayTime(playTime);

            mSunriseAnimSet.end();
        } else {
            mSunAnim = getSunAnimator(mSunView.getTop(), mSkyView.getHeight());
            mSkyAnim = getSkyAnimator(mBlueSkyColor, mSunsetSkyColor);
        }

        mSunsetAnimSet = new AnimatorSet();
        mSunsetAnimSet.play(mSunAnim)
                .with(mSkyAnim);

        mSunsetAnimSet.start();
    }

    private ObjectAnimator getSunAnimator(float sunYStart, float sunYEnd) {
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(ANIMATION_DURATION);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return heightAnimator;
    }

    private ObjectAnimator getSunXAnimator(float sunYStart, float sunYEnd) {
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "x", sunYStart, sunYEnd)
                .setDuration(ANIMATION_DURATION);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return heightAnimator;
    }

    private ObjectAnimator getSkyAnimator(int startColor, int endColor) {
        ObjectAnimator skyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", startColor, endColor)
                .setDuration(ANIMATION_DURATION);
        skyAnimator.setEvaluator(new ArgbEvaluator());
        return skyAnimator;
    }

    private ObjectAnimator getStarXAnimator(View starView, float scaleXStart, float scaleXEnd) {
        ObjectAnimator starAnimator = ObjectAnimator
                .ofFloat(starView, "scaleX", scaleXStart, scaleXEnd)
                .setDuration(ANIMATION_DURATION);
        starAnimator.setInterpolator(new BounceInterpolator());
        return starAnimator;
    }

    private ObjectAnimator getStarYAnimator(View starView, float scaleYStart, float scaleYEnd) {
        ObjectAnimator starAnimator = ObjectAnimator
                .ofFloat(starView, "scaleY", scaleYStart, scaleYEnd)
                .setDuration(ANIMATION_DURATION);
        starAnimator.setInterpolator(new BounceInterpolator());
        return starAnimator;
    }

    private ObjectAnimator getStarRotationAnimator(View starView, float rotateStart, float rotateEnd) {
        ObjectAnimator starAnimator = ObjectAnimator
                .ofFloat(starView, "rotation", rotateStart, rotateEnd)
                .setDuration(0);
        starAnimator.setInterpolator(new BounceInterpolator());
        return starAnimator;
    }
//
//    private ObjectAnimator getCloud2Animator(float cloudXStart, float cloudXEnd) {
//        ObjectAnimator cloudAnimator = ObjectAnimator
//                .ofFloat(mCloud2View, "x", cloudXStart, cloudXEnd)
//                .setDuration(ANIMATION_DURATION);
//        cloudAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        return cloudAnimator;
//    }

}
