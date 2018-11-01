package cc.brainbook.myapplication.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import cc.brainbook.myapplication.entity.DaoMaster;
import cc.brainbook.myapplication.entity.DaoSession;

// https://blog.csdn.net/scorpio_gao/article/details/53048699
// https://blog.csdn.net/wzgbgz/article/details/79140056
// https://www.jianshu.com/p/dac3bd9bad72
// https://blog.csdn.net/geanwen/article/details/69945127#commentBox
public class GreenDaoManager {
    /**
     * 数据库名称
     */
    private static final String DB_NAME = "user.db";
    private static final String DB_ENCRYPTED_NAME = "user-encrypted.db";   // 加密数据库名称
    private static final String DB_ENCRYPTED_PASSWORD = "123456";   // 加密数据库密码

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    /**
     * 多线程安全访问、单例模式
     */
    private volatile static GreenDaoManager INSTANCE;

//    /**
//     * 静态内部类实现单例（注意：无法传入参数！）
//     *
//     * https://www.jianshu.com/p/dac3bd9bad72
//     */
//    private static class SingleInstanceHolder
//    {
//        private static final GreenDaoManager INSTANCE = new GreenDaoManager();
//    }
//    public static GreenDaoManager getInstance()
//    {
//        return SingleInstanceHolder.INSTANCE;
//    }
//
//    /**
//     * 私有构造实现单例（注意：可以通过带参数的构造方法传入参数）
//     */
//    private GreenDaoManager(){
//        if (INSTANCE == null) {
//            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, ENCRYPTED ? DB_ENCRYPTED_NAME : DB_NAME, null);
//        }
//    }

    /**
     * 使用单例模式获得操作数据库的对象
     */
    public static GreenDaoManager getInstance() {
        if (INSTANCE == null) {
            // synchronized保证了异步处理的安全
            synchronized (GreenDaoManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GreenDaoManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 注意：以下都可为static，但要用synchronized关键字处理多线程安全！https://blog.csdn.net/wzgbgz/article/details/79140056
     */
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     *  注意：如果这样写：“GreenDaoManager.getInstance(mContext)”，以后的CRUD操作代码比较多，
     *  如：“GreenDaoManager.getInstance(MyApplication.getContext()).getDaoSession()”，
     *  而用GreenDaoManager的init方式后代码简单了，如“GreenDaoManager.getInstance().getDaoSession()”
     */
    private Context mContext;
    public void init(Context context){
        this.mContext = context;
        mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, ENCRYPTED ? DB_ENCRYPTED_NAME : DB_NAME, null);
    }

    /**
     * 获取DaoMaster
     */
    public DaoMaster getDaoMaster() {
        return getWritableDaoMaster();
    }
    public DaoMaster getReadableDaoMaster() {
        if (null == mDaoMaster) {
            if (ENCRYPTED) {
                mDaoMaster = new DaoMaster(getEncryptedReadableDb());
            } else {
                mDaoMaster = new DaoMaster(getReadableDatabase());
            }

        }
        return mDaoMaster;
    }
    public DaoMaster getWritableDaoMaster() {
        if (null == mDaoMaster) {
            if (ENCRYPTED) {
                mDaoMaster = new DaoMaster(getEncryptedWritableDb());
            } else {
                mDaoMaster = new DaoMaster(getWritableDatabase());
            }

        }
        return mDaoMaster;
    }

    /**
     * 获取可读数据库
     */
    public SQLiteDatabase getReadableDatabase() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    public SQLiteDatabase getWritableDatabase() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * 获取可读加密数据库
     */
    public Database getEncryptedReadableDb() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getEncryptedReadableDb(DB_ENCRYPTED_PASSWORD);
    }

    /**
     * 获取可写加密数据库
     */
    public Database getEncryptedWritableDb() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getEncryptedWritableDb(DB_ENCRYPTED_PASSWORD);
    }

    /**
     * 获取DaoSession
     *
     * 完成对数据库的添加删除修改查询等的操作
     * 注意：仅仅是一个接口！
     */
    public DaoSession getDaoSession(){
        if (mDaoSession == null){
            if (mDaoMaster == null){
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 打开数据库输出日志的操作
     * 注意：默认是关闭的
     */
    public void setDebug(){
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的数据库操作
     * 注意：数据库开启之后，使用完毕必须要关闭！
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }
    public void closeHelper() {
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
            mDevOpenHelper = null;
        }
    }
    public void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}

