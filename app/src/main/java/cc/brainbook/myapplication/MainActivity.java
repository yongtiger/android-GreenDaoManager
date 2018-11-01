package cc.brainbook.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import cc.brainbook.myapplication.dao.GreenDaoManager;
import cc.brainbook.myapplication.entity.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 数据表操作：插入（insert）
        User user = new User(null, "new_user");
        GreenDaoManager.getInstance().getDaoSession().insert(user);

        // 数据表操作：查询（loadAll）
        List<User> users = GreenDaoManager.getInstance().getDaoSession().getUserDao().loadAll();
        Log.d("TAG", "onCreate: " + users.size());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 关闭所有的数据库操作
        GreenDaoManager.getInstance().closeConnection();
    }
}
