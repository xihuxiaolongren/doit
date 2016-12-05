package me.xihuxiaolong.justdoit.common.base;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.xihuxiaolong.justdoit.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    protected int menuColor;

    abstract protected void injectDependencies();

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    protected void setAllMenuColor(Menu menu, Toolbar toolbar) {
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

    private boolean isFirstSet = true;

    public void setToolbar(Toolbar toolbar, boolean showHomeAsUp) {
        setToolbar(toolbar, showHomeAsUp, true);
    }

    public void setToolbar(Toolbar toolbar, boolean showHomeAsUp, boolean showTitle) {
        if (toolbar != null) {
            if (isFirstSet && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
                layoutParams.height = layoutParams.height + getStatusBarHeight();
                toolbar.setLayoutParams(layoutParams);
                toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
                isFirstSet = false;
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        }
    }
}
