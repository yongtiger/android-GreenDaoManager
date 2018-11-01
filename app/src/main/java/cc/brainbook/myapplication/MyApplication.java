package cc.brainbook.myapplication;

import android.app.Application;
import android.content.Context;

import cc.brainbook.myapplication.dao.GreenDaoManager;

public class MyApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        // GreenDao全局配置，只希望有一个数据库操作对象
        //
        // 注意：如果这样写：“GreenDaoManager.getInstance(mContext)”，
        // 以后的CRUD操作代码比较多，如：“GreenDaoManager.getInstance(MyApplication.getContext()).getDaoSession()”
        // 用GreenDaoManager的init方式后代码简介简单了，如“GreenDaoManager.getInstance().getDaoSession()”
        GreenDaoManager.getInstance().init(mContext);
    }
}
