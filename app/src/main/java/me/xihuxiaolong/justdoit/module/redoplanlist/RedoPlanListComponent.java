package me.xihuxiaolong.justdoit.module.redoplanlist;

import dagger.Component;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;
import me.xihuxiaolong.justdoit.common.dagger.scope.ActivityScope;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {RedoPlanListModule.class})
public interface RedoPlanListComponent {

    RedoPlanListPresenter presenter();
}
