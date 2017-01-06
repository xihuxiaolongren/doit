package me.xihuxiaolong.justdoit.common.database.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import me.xihuxiaolong.justdoit.BuildConfig;
import me.xihuxiaolong.justdoit.common.MyApplication;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public abstract class BaseDataService {

    protected DaoMaster.OpenHelper helper;

    private static final String MAIN_DB_NAME_SUFFIX = "_main_db";

    public BaseDataService(){

        if(BuildConfig.DEBUG)
            helper = new OnlineOpenHelper(MyApplication.getInstance(), -1L + MAIN_DB_NAME_SUFFIX, null);
        else
            helper = new OnlineOpenHelper(MyApplication.getInstance(), -1L + MAIN_DB_NAME_SUFFIX, null);
    }

    public void clear(DaoSession daoSession, SQLiteDatabase database){
        daoSession.clear();
        if(database != null && database.isOpen())
            database.close();
    }

    protected class OnlineOpenHelper  extends DaoMaster.OpenHelper {

        public OnlineOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion){
                default:
            }
        }

    }
}
