package me.xihuxiaolong.justdoit.module.easybackloglist;

import dagger.Component;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;
import me.xihuxiaolong.justdoit.common.dagger.scope.ActivityScope;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/9.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {EasyBacklogListModule.class})
public interface EasyBacklogListComponent {

    EasyBacklogListPresenter presenter();
}
