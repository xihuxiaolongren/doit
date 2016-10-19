package me.xihuxiaolong.justdoit.module.planhistory;

import dagger.Component;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;
import me.xihuxiaolong.justdoit.common.dagger.scope.ActivityScope;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules={PlanHistoryActivityModule.class})
public interface PlanHistoryActivityComponent {

    void inject(PlanHistoryActivity planHistoryActivity);

    PlanHistoryActivityPresenter presenter();
}
