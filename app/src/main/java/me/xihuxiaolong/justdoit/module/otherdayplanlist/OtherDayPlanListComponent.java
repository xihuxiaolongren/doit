package me.xihuxiaolong.justdoit.module.otherdayplanlist;

import dagger.Component;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;
import me.xihuxiaolong.justdoit.common.dagger.scope.ActivityScope;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {OtherDayPlanListModule.class})
public interface OtherDayPlanListComponent {

    OtherDayPlanListPresenter presenter();
}
