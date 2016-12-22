package me.xihuxiaolong.justdoit.common.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import me.xihuxiaolong.justdoit.common.MyApplication;

public class DeviceUtil extends Activity {

	static int d = 0;

	static DisplayMetrics displayMetric;
	
	static Context context = MyApplication.getInstance();

	public static DisplayMetrics getDisplay() {
		if (displayMetric == null)
			displayMetric = context.getResources()
					.getDisplayMetrics();
		return displayMetric;
	}

	public static int getDensity() {
		if (d <= 0)
			d = (int) context.getResources().getDisplayMetrics().density;
		return d;
	}

}
