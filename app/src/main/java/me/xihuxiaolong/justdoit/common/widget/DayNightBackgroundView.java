package me.xihuxiaolong.justdoit.common.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
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
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.library.widget.WaveHelper;
import me.xihuxiaolong.library.widget.WaveView;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/15.
 */

public class DayNightBackgroundView extends FrameLayout{

    private int animationDuration = 2000;

    private View rootView;
    private View mSunView;
    private View mSkyView;
    private WaveView waveView;
    private ObjectAnimator mSunAnim, mSunXAnim, mSunAlphaAnim;
    private ObjectAnimator mSkyAnim;

    private View mCloud1View;
    private View mCloud2View;
    private ObjectAnimator mCloud1Anim;
    private ObjectAnimator mCloud2Anim;

    private View mStar1View, mStar2View, mStar3View, mStar4View, mStar5View;
    private ObjectAnimator mStarX1Anim, mStarX2Anim, mStarX3Anim, mStarX4Anim, mStarX5Anim;
    private ObjectAnimator mStarY1Anim, mStarY2Anim, mStarY3Anim, mStarY4Anim, mStarY5Anim;
    private ObjectAnimator mStarR1Anim, mStarR2Anim, mStarR3Anim, mStarR4Anim, mStarR5Anim;

    private AnimatorSet mSunriseAnimSet;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
//    private int mWaveBehindColor;
//    private int mWaveFrontColor;

    private WaveHelper mWaveHelper;

    public DayNightBackgroundView(Context context) {
        super(context);
        init(context, null);
    }

    public DayNightBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DayNightBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayNightBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context,  AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DayNightBackgroundView);
            animationDuration = a.getInt(R.styleable.DayNightBackgroundView_animationDuration, 2000);
            a.recycle();
        }
        mBlueSkyColor = ContextCompat.getColor(context, R.color.sky);
        mSunsetSkyColor = ContextCompat.getColor(context, R.color.sunset_sky);
//        mWaveBehindColor = ContextCompat.getColor(context, R.color.wave_behind);
//        mWaveFrontColor = ContextCompat.getColor(context, R.color.wave_front);
        if(DayNightModeUtils.isCurrentNight()){
            initNight(context);
        }else{
            initDay(context);
        }


    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        mWaveHelper = new WaveHelper(waveView, animationDuration);
    }

    private void initDay(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.day_background_view, this);

        rootView = findViewById(R.id.bgRootView);
        mSunView = findViewById(R.id.sun);
        mSkyView = findViewById(R.id.sky);
        waveView = (WaveView) findViewById(R.id.wave);
        mCloud1View = findViewById(R.id.cloud_1);
        mCloud2View = findViewById(R.id.cloud_2);
        mWaveHelper = new WaveHelper(waveView, animationDuration);

        post(new Runnable() {
            @Override
            public void run() {
                sunrise();
            }
        });
    }

    private void initNight(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.night_background_view, this);

        rootView = findViewById(R.id.bgRootView);
        mSunView = findViewById(R.id.sun);
        mSkyView = findViewById(R.id.sky);
        waveView = (WaveView) findViewById(R.id.wave);
        mStar1View = findViewById(R.id.star1);
        mStar2View = findViewById(R.id.star2);
        mStar3View = findViewById(R.id.star3);
        mStar4View = findViewById(R.id.star4);
        mStar5View = findViewById(R.id.star5);
        mWaveHelper = new WaveHelper(waveView, animationDuration);

        post(new Runnable() {
            @Override
            public void run() {
                moonrise();
            }
        });
    }

    /**
     * 太阳升起
     */
    private void sunrise() {
//        waveView.setWaveColor(mWaveBehindColor, mWaveFrontColor);
        mWaveHelper.start();
        mSunAnim = getSunYAnimator(mSkyView.getHeight(), getContext().getResources().getDimensionPixelSize(R.dimen.sun_top));
        mSunXAnim = getSunXAnimator(0, mSunView.getLeft());
        mSunAlphaAnim = getSunAlphaAnimator(0.0f, 1.0f);
        mSkyAnim = getSkyAnimator(mSunsetSkyColor, mBlueSkyColor);
        mCloud1Anim = getCloud1Animator(0, mCloud1View.getLeft());
        mCloud2Anim = getCloud2Animator(0, mCloud2View.getRight());
        mSunriseAnimSet = new AnimatorSet();
        mSunriseAnimSet.play(mSunAnim).with(mSunXAnim).with(mSunAlphaAnim)
                .with(mSkyAnim)
                .with(mCloud1Anim).with(mCloud2Anim);
        mSunriseAnimSet.start();
    }

    /**
     * 月亮升起
     */
    private void moonrise() {
//        waveView.setWaveColor(mWaveBehindColor, mWaveFrontColor);
        mWaveHelper.start();
        mSunAnim = getSunYAnimator(mSkyView.getHeight(), getContext().getResources().getDimensionPixelSize(R.dimen.sun_top));
        mSunXAnim = getSunXAnimator(0, mSunView.getLeft());
        mSunAlphaAnim = getSunAlphaAnimator(0.0f, 1.0f);
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
        mSunriseAnimSet = new AnimatorSet();
        mSunriseAnimSet.play(mSunAnim).with(mSunXAnim).with(mSunAlphaAnim)
                .with(mSkyAnim)
                .with(mStarX1Anim).with(mStarX2Anim).with(mStarX3Anim).with(mStarX4Anim).with(mStarX5Anim)
                .with(mStarY1Anim).with(mStarY2Anim).with(mStarY3Anim).with(mStarY4Anim).with(mStarY5Anim)
                .with(mStarR1Anim).with(mStarR2Anim).with(mStarR3Anim).with(mStarR4Anim).with(mStarR5Anim);
        mSunriseAnimSet.start();
    }

    private ObjectAnimator getSunYAnimator(float sunYStart, float sunYEnd) {
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart+100, sunYEnd)
                .setDuration(animationDuration);
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        return heightAnimator;
    }

    private ObjectAnimator getSunXAnimator(float sunYStart, float sunYEnd) {
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "x", sunYStart, sunYEnd)
                .setDuration(animationDuration);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return heightAnimator;
    }

    private ObjectAnimator getSunAlphaAnimator(float start, float end) {
        ObjectAnimator skyAnimator = ObjectAnimator
                .ofFloat(mSunView, "alpha", start, end)
                .setDuration(animationDuration);
        skyAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return skyAnimator;
    }

    private ObjectAnimator getSkyAnimator(int startColor, int endColor) {
        ObjectAnimator skyAnimator = ObjectAnimator
                .ofInt(rootView, "backgroundColor", startColor, endColor)
                .setDuration(animationDuration);
        skyAnimator.setEvaluator(new ArgbEvaluator());
        return skyAnimator;
    }

    private ObjectAnimator getCloud1Animator(float cloudXStart, float cloudXEnd) {
        ObjectAnimator cloudAnimator = ObjectAnimator
                .ofFloat(mCloud1View, "x", cloudXStart, cloudXEnd)
                .setDuration(animationDuration);
        cloudAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return cloudAnimator;
    }

    private ObjectAnimator getCloud2Animator(float cloudXStart, float cloudXEnd) {
        ObjectAnimator cloudAnimator = ObjectAnimator
                .ofFloat(mCloud2View, "x", cloudXStart, cloudXEnd)
                .setDuration(animationDuration);
        cloudAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return cloudAnimator;
    }

    private ObjectAnimator getStarXAnimator(View starView, float scaleXStart, float scaleXEnd) {
        ObjectAnimator starAnimator = ObjectAnimator
                .ofFloat(starView, "scaleX", scaleXStart, scaleXEnd)
                .setDuration(animationDuration);
        starAnimator.setInterpolator(new BounceInterpolator());
        return starAnimator;
    }

    private ObjectAnimator getStarYAnimator(View starView, float scaleYStart, float scaleYEnd) {
        ObjectAnimator starAnimator = ObjectAnimator
                .ofFloat(starView, "scaleY", scaleYStart, scaleYEnd)
                .setDuration(animationDuration);
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

}
