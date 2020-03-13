package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.helloworld.ui.MainFragment;
import com.example.helloworld.ui.SingleInfoFragment;
import com.example.helloworld.utils.MySQLiteOpenHelper;



public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "UserPool.db", null, 1);
        db = mySQLiteOpenHelper.getWritableDatabase();
        Configuration configuration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = configuration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            Bundle bundle = new Bundle();
            bundle.putBoolean("landscape", true);
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.left_frame,mainFragment)
                    .commit();
            SingleInfoFragment singleInfoFragment = new SingleInfoFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.right_frame, singleInfoFragment)
                    .commit();
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainframe,new MainFragment())
                    .commit();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
