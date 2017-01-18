package me.xihuxiaolong.justdoit.module.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.util.DeviceUtil;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import mehdi.sakout.fancybuttons.FancyButton;

public class BacklogListAdapter extends BaseQuickAdapter<BacklogDO, BaseViewHolder> {

    private int minHeight;

    BacklogListOnClickListener backlogListOnClickListener;

    public BacklogListAdapter(Context context, int layoutResId, List<BacklogDO> datas, BacklogListOnClickListener backlogListOnClickListener) {
        super(layoutResId, datas);
        init(context, backlogListOnClickListener);
    }

    private void init(Context context, BacklogListOnClickListener backlogListOnClickListener){
        minHeight = DeviceUtil.getScreenHeight();

        this.backlogListOnClickListener = backlogListOnClickListener;
    }

    private View.OnClickListener backlogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BacklogDO backlogDO = (BacklogDO) v.getTag();
            if (backlogListOnClickListener != null)
                backlogListOnClickListener.backlogClick(backlogDO);
        }
    };

    @Override
    protected void convert(BaseViewHolder holder, BacklogDO backlogDO) {
        holder.setText(R.id.contentTV, backlogDO.getContent());
    }

    public interface BacklogListOnClickListener {
        void backlogClick(BacklogDO backlogDO);
    }
}