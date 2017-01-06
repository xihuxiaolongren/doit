package me.xihuxiaolong.justdoit.module.editphoto;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/28.
 */

public class EditPhotoPresenter extends MvpBasePresenter<EditPhotoContract.IView> implements EditPhotoContract.IPresenter {

    @Inject
    PlanDataService planDataSource;

    @Inject @Nullable
    String targetName;

    @Inject
    public EditPhotoPresenter() {
    }

    @Override
    public void savePhoto(String content, String pictures) {
        DateTime dateTime = DateTime.now();
        PlanDO photo = new PlanDO();
        photo.setType(PlanDO.TYPE_PHOTO);
        photo.setContent(content);
        photo.setStartHour(dateTime.getHourOfDay());
        photo.setStartMinute(dateTime.getMinuteOfHour());
        photo.setStartTime(dateTime.getMillisOfDay());
        photo.setPicUrls(pictures);
        photo.setTargetName(targetName);

        photo.setDayTime(dateTime.withTimeAtStartOfDay().getMillis());
        long photoId = planDataSource.insertOrReplacePlanDO(photo);
        photo.setId(photoId);
        EventBus.getDefault().post(new Event.AddPlan(photo));
        if (isViewAttached())
            getView().saveSuccess();
    }

}
