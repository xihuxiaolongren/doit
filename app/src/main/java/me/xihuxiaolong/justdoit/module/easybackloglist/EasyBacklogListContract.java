package me.xihuxiaolong.justdoit.module.easybackloglist;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class EasyBacklogListContract {

    interface IView extends MvpView {

        void removeBacklogItem(long backlogId);

        void addBacklogItem(BacklogDO backlogDO);

        void updateBacklogItem(BacklogDO backlogDO);

        void showBacklogs(List<BacklogDO> backlogs);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadBacklogs();

        void deleteBacklog(BacklogDO backlogDO);

        void shareBacklog(BacklogDO backlogDO);

        void saveBacklog(String content);

        void modifyBacklog(Long id, String content);

    }
}
