package me.xihuxiaolong.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.xihuxiaolong.library.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/6/24.
 */
public class CustomToolbar extends FrameLayout{

    private String leftFirstText = null;
    private String leftSecondText = null;

    private Drawable leftFirstIcon = null;
    private Drawable leftSecondIcon = null;

    private String rightFirstText = null;
    private String rightSecondText = null;

    private Drawable rightFirstIcon = null;
    private Drawable rightSecondIcon = null;

    private String title = null;

    RelativeLayout leftRL;
    RelativeLayout leftSecRL;
    TextView leftTV;
    TextView leftSecTV;
    ImageView leftIV;
    ImageView leftSecIV;
    RelativeLayout rightRL;
    RelativeLayout rightSecRL;
    TextView rightTV;
    TextView rightSecTV;
    ImageView rightIV;
    ImageView rightSecIV;
    TextView titleToolbarTV;

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray 	= context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();

        initializeCustomToolbar(context);

    }

    private void initAttributesArray(TypedArray attrsArray) {
        leftFirstText = attrsArray.getString(R.styleable.CustomToolbar_left_firstText);
        rightFirstText = attrsArray.getString(R.styleable.CustomToolbar_right_firstText);
        leftFirstIcon = attrsArray.getDrawable(R.styleable.CustomToolbar_left_firstIcon);
        rightFirstIcon = attrsArray.getDrawable(R.styleable.CustomToolbar_right_firstIcon);
        title = attrsArray.getString(R.styleable.CustomToolbar_toolbar_title);
        leftSecondText = attrsArray.getString(R.styleable.CustomToolbar_left_secondText);
        rightSecondText = attrsArray.getString(R.styleable.CustomToolbar_right_secondText);
        leftSecondIcon = attrsArray.getDrawable(R.styleable.CustomToolbar_left_secondIcon);
        rightSecondIcon = attrsArray.getDrawable(R.styleable.CustomToolbar_right_secondIcon);
    }

    private void initializeCustomToolbar(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this);
        leftRL = (RelativeLayout) view.findViewById(R.id.left_rl);
        leftSecRL = (RelativeLayout) view.findViewById(R.id.left_sec_rl);
        leftTV = (TextView) view.findViewById(R.id.btn_left_tv);
        leftSecTV = (TextView) view.findViewById(R.id.btn_left_sec_tv);
        leftIV = (ImageView) view.findViewById(R.id.btn_left_iv);
        leftSecIV = (ImageView) view.findViewById(R.id.btn_left_sec_iv);
        rightRL = (RelativeLayout) view.findViewById(R.id.right_rl);
        rightSecRL = (RelativeLayout) view.findViewById(R.id.right_sec_rl);
        rightTV = (TextView) view.findViewById(R.id.btn_right_tv);
        rightSecTV = (TextView) view.findViewById(R.id.btn_right_sec_tv);
        rightIV = (ImageView) view.findViewById(R.id.btn_right_iv);
        rightSecIV = (ImageView) view.findViewById(R.id.btn_right_sec_iv);
        titleToolbarTV = (TextView) view.findViewById(R.id.title_toolbar);

        leftTV.setText(leftFirstText);
        leftIV.setImageDrawable(leftFirstIcon);
        leftSecTV.setText(leftSecondText);
        leftSecIV.setImageDrawable(leftSecondIcon);
        rightTV.setText(rightFirstText);
        rightIV.setImageDrawable(rightFirstIcon);
        rightSecTV.setText(rightSecondText);
        rightSecIV.setImageDrawable(rightSecondIcon);
        titleToolbarTV.setText(title);
    }

    public void setBackVisble(Boolean backVisible, final Activity activity){
        if(backVisible){
            leftIV.setImageResource(R.drawable.common_btn_back);
            leftRL.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    public void setLeftFirstAreaClickListener(OnClickListener onClickListener){
        leftRL.setOnClickListener(onClickListener);
    }

    public void setLeftSecondAreaClickListener(OnClickListener onClickListener){
        leftSecRL.setOnClickListener(onClickListener);
    }

    public void setRightFirstAreaClickListener(OnClickListener onClickListener){
        rightRL.setOnClickListener(onClickListener);
    }

    public void setRightSecondAreaClickListener(OnClickListener onClickListener){
        rightSecRL.setOnClickListener(onClickListener);
    }

}
