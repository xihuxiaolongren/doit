package me.xihuxiaolong.justdoit.common.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.orhanobut.logger.Logger;

import me.xihuxiaolong.justdoit.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    private int menuColor;

    abstract protected void injectDependencies();

    public int getStatusBarHeight() {
        if(getActivity() instanceof BaseActivity)
            return ((BaseActivity)getActivity()).getStatusBarHeight();
        else if(getActivity() instanceof BaseMvpActivity)
            return ((BaseMvpActivity)getActivity()).getStatusBarHeight();
        return 0;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setMenuColor(menu);
    }

    protected void setMenuColor(Menu menu) {
        if (menuColor == 0)
            menuColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(menuColor, PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }

    protected void setAllMenuColor(Menu menu, Toolbar toolbar, int color) {
        if (color == 0)
            menuColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);
        else
            menuColor = color;
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(menuColor, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        toolbar.setTitleTextColor(menuColor);
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), menuColor);
            toolbar.setOverflowIcon(drawable);
        }
        final Drawable upArrow = ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_ab_back_material_1);
        upArrow.setColorFilter(menuColor, PorterDuff.Mode.SRC_IN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void initToolbar(Toolbar toolbar, boolean showHomeAsUp) {
        initToolbar(toolbar, showHomeAsUp, true);
    }

    public void initToolbar(Toolbar toolbar, boolean showHomeAsUp, boolean showTitle) {
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        layoutParams.height = layoutParams.height + getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setToolbar(toolbar, showHomeAsUp, showTitle);
    }

    public void setToolbar(Toolbar toolbar, boolean showHomeAsUp) {
        setToolbar(toolbar, showHomeAsUp, true);
    }

    public void setToolbar(Toolbar toolbar, boolean showHomeAsUp, boolean showTitle) {
        if (toolbar != null) {
            //解决当一个activity中多个fragment使用不同toolbar时，padding会在特定情况下被置0，具体原因未知
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && toolbar.getPaddingTop() == 0) {
                toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
            Logger.d("%d, %d, %d, %d, %d, %d", toolbar.getMeasuredHeight(), toolbar.getHeight(), toolbar.getPaddingTop(), toolbar.getPaddingBottom(), toolbar.getPaddingLeft(), toolbar.getPaddingRight());
        }
    }

}
