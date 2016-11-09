package me.xihuxiaolong.justdoit.common.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.dagger.module.AppModule;
import me.xihuxiaolong.library.utils.ToastUtil;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    ICacheService getCacheService();

}
