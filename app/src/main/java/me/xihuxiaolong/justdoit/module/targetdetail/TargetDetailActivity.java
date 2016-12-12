package me.xihuxiaolong.justdoit.module.targetdetail;

import android.os.Bundle;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class TargetDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_day_plan_list);
            ButterKnife.bind(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setSharedElementEnterTransition(makeSharedElementEnterTransition(getWindow().getSharedElementEnterTransition()));
//            setEnterSharedElementCallback(new EnterSharedElementCallback(this));
//        }

        TargetDetailFragment targetDetailFragment =
                (TargetDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(targetDetailFragment == null) {
            targetDetailFragment = TargetDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    targetDetailFragment, R.id.contentFrame);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public static Transition makeSharedElementEnterTransition(Transition sharedElementEnterTransition) {
//        TransitionSet set = (TransitionSet) sharedElementEnterTransition;
//        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
//
////        Transition recolor = new Recolor();
////        recolor.addTarget("targetTitle");
////        set.addTransition(recolor);
//
//        Transition changeBounds = new ChangeBounds();
//        changeBounds.addTarget("targetTitle");
//        set.addTransition(changeBounds);
//
//        Transition textSize = new TextSizeTransition();
//        textSize.addTarget("targetTitle");
//        set.addTransition(textSize);
//
//        return set;
//    }
}
