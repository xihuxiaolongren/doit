package me.xihuxiaolong.justdoit.module.redoplanlist;

import android.os.Bundle;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.module.targetdetail.TargetPunchDetailFragment;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class RedoPlanListActivity extends BaseActivity {

    public static final String ARG_TARGET = "target";

    TargetDO targetDO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        targetDO = (TargetDO) getIntent().getSerializableExtra(ARG_TARGET);

        RedoPlanListFragment redoPlanListFragment =
                (RedoPlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (redoPlanListFragment == null) {
            redoPlanListFragment = RedoPlanListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    redoPlanListFragment, R.id.contentFrame);
        }
    }

}
