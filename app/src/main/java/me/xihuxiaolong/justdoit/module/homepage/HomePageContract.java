package me.xihuxiaolong.justdoit.module.homepage;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.joda.time.DateTime;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class HomePageContract {

    interface IView extends MvpView {

        void showDayInfo(String avatarUrl, DateTime dateTime);

        void showSignature(String signature, String preSignature);

        void savePunchSuccess();

        void showPunchDialog(List<TargetDO> targetList);

        void updateBacklogCount(long count);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadUserSettings();

        void loadBacklogCount();

        void startAddPunch();

        void savePunch(String content, String pictures, String targetName);

        void saveBacklog(String content);

    }
}
