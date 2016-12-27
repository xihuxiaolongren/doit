package me.xihuxiaolong.justdoit.module.targetdetail;

import android.os.Bundle;
import android.support.v7.graphics.Target;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class TargetDetailActivity extends BaseActivity {

    public static final String ARG_TARGET = "target";

    TargetDO targetDO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        targetDO = (TargetDO) getIntent().getSerializableExtra(ARG_TARGET);

        if(targetDO.getType() == TargetDO.TYPE_NORMAL) {
            TargetDetailFragment targetDetailFragment =
                    (TargetDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
            if (targetDetailFragment == null) {
                targetDetailFragment = TargetDetailFragment.newInstance();
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        targetDetailFragment, R.id.contentFrame);
            }
        }else if(targetDO.getType() == TargetDO.TYPE_PUNCH){
            TargetPunchDetailFragment targetDetailFragment =
                    (TargetPunchDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
            if (targetDetailFragment == null) {
                targetDetailFragment = TargetPunchDetailFragment.newInstance();
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        targetDetailFragment, R.id.contentFrame);
            }
        } else {
            finish();
        }
    }

}
