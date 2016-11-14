package me.xihuxiaolong.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import me.xihuxiaolong.library.R;

/**
 * Created by yangxiaolong on 15/10/26.
 */
public class BottomTabView extends FrameLayout {

    private SingleTabView[] singleTabViews;

    private OnItemClickCallback onItemClickCallback;

    private ViewPager viewPager;

    private boolean smoothScroll;

    public BottomTabView(Context context) {
        super(context);
    }

    public BottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(List<TabData> tabDatas){
        singleTabViews = new SingleTabView[tabDatas.size()];
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.widget_bottom_tab_view, this);
        LinearLayout linearLayout = (LinearLayout) viewGroup.findViewById(R.id.rootView);
        for(int i = 0; i < tabDatas.size(); ++i){
            SingleTabView singleTabView = new SingleTabView(getContext());
            TabData tabData = tabDatas.get(i);
            singleTabView.setData(tabData.text, tabData.textColorListRes, tabData.imageBgRes);
            singleTabViews[i] = singleTabView;
            linearLayout.addView(singleTabView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            singleTabView.setTag(i);
            singleTabView.setOnClickListener(tabClickListener);
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setViewPager(ViewPager viewPager, boolean smoothScroll) {
        this.viewPager = viewPager;
        this.smoothScroll = smoothScroll;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private OnClickListener tabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            setCurrentItem(position);
            if(viewPager != null){
                viewPager.setCurrentItem(position, smoothScroll);
            }
            if(onItemClickCallback != null)
                onItemClickCallback.onItemClick(position);
        }
    };

    public void setCurrentItem(int position){
        if(position < singleTabViews.length)
            for(int i = 0; i < singleTabViews.length; ++i)
                singleTabViews[i].setSelected(position == i);
    }

    public void setImgUnreadVisible(int pos, boolean visible) {
        if(singleTabViews == null || singleTabViews.length-1 < pos){
            throw new IllegalStateException("singleTabViews null or out of index.");
        }
        singleTabViews[pos].setImgUnreadVisible(visible);
    }

    public interface OnItemClickCallback{
        void onItemClick(int position);
    }

    public static class TabData{
        String text;
        int textColorListRes;
        int imageBgRes;

        public TabData(String text, int textColorListRes, int imageBgRes) {
            this.text = text;
            this.textColorListRes = textColorListRes;
            this.imageBgRes = imageBgRes;
        }
    }

}
