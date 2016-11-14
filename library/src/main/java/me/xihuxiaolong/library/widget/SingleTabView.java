package me.xihuxiaolong.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.xihuxiaolong.library.R;

/**
 * Created by yangxiaolong on 15/10/26.
 */
public class SingleTabView extends LinearLayout{

    private TintableImageView ivTab;
    private ImageView imgUnread;
    private TextView tvTab;

    public SingleTabView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_single_tab, this);
        ivTab = (TintableImageView) findViewById(R.id.img_tab);
        imgUnread = (ImageView) findViewById(R.id.img_unread);
        tvTab = (TextView) findViewById(R.id.text_tab);
    }

    public void setData(Pair<String, Integer> data) {
        tvTab.setText(data.first);
        ivTab.setBackgroundResource(data.second);
    }

    public void setData(String text, int textColorListRes, int imageRes) {
        tvTab.setText(text);
        tvTab.setTextColor(ContextCompat.getColorStateList(getContext(), textColorListRes));
//        ivTab.setBackgroundResource(imageRes);
        ivTab.setImageResource(imageRes);
//        ColorStateList list = ContextCompat.getColorStateList(getContext(), textColorListRes);
//        ivTab.setColorFilter(list);
    }

    public void setImgUnreadVisible(boolean visible) {
        if (visible) {
            imgUnread.setVisibility(View.VISIBLE);
        } else {
            imgUnread.setVisibility(View.GONE);
        }
    }

}
