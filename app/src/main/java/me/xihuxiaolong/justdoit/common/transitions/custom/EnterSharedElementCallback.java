package me.xihuxiaolong.justdoit.common.transitions.custom;


import android.app.SharedElementCallback;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.xihuxiaolong.justdoit.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class EnterSharedElementCallback extends SharedElementCallback {
    private static final String TAG = "EnterSharedElementCallback";

    private final float mStartTextSize;
    private final float mEndTextSize;
//    private final int mStartColor;
//    private final int mEndColor;

    public EnterSharedElementCallback(Context context) {
        Resources res = context.getResources();
        mStartTextSize = res.getDimensionPixelSize(R.dimen.contentSmallTextSize);
        mEndTextSize = res.getDimensionPixelSize(R.dimen.title_size);
//        mStartColor = res.getColor(R.color.blue);
//        mEndColor = res.getColor(R.color.light_blue);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        for(int i = 0; i < sharedElementNames.size(); ++i){
            if("targetTitle".equals(sharedElementNames.get(i))){
                TextView targetTitleTV = (TextView) sharedElements.get(i);
                targetTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
            }
        }

        // Setup the TextView's start values.
//        textView.setTextColor(mStartColor);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        for(int i = 0; i < sharedElementNames.size(); ++i){
            if("targetTitle".equals(sharedElementNames.get(i))){
                TextView textView = (TextView) sharedElements.get(i);

                // Record the TextView's old width/height.
                int oldWidth = textView.getMeasuredWidth();
                int oldHeight = textView.getMeasuredHeight();

                // Setup the TextView's end values.
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEndTextSize);
//        textView.setTextColor(mEndColor);

                // Re-measure the TextView (since the text size has changed).
                int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                textView.measure(widthSpec, heightSpec);

                // Record the TextView's new width/height.
                int newWidth = textView.getMeasuredWidth();
                int newHeight = textView.getMeasuredHeight();

                // Layout the TextView in the center of its container, accounting for its new width/height.
                int widthDiff = newWidth - oldWidth;
                int heightDiff = newHeight - oldHeight;
                textView.layout(textView.getLeft() - widthDiff / 2, textView.getTop() - heightDiff / 2,
                        textView.getRight() + widthDiff / 2, textView.getBottom() + heightDiff / 2);            }
        }
    }
}
