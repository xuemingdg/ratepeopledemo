package com.example.helloworld.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.utils.MySQLiteOpenHelper;
import com.example.helloworld.R;
import com.example.helloworld.utils.SpaceItemDecoration;
import com.example.helloworld.utils.User;
import com.example.helloworld.adapter.UserAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    private ArrayList<User> userArrayList = new ArrayList<>();
    private Unbinder unbinder;
    private FragmentManager fragmentManager;
    private SQLiteDatabase db;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.textview)
    TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        unbinder = ButterKnife.bind(this, view);
        initUsers();
        initView();
        return view;

    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        UserAdapter adapter = new UserAdapter(userArrayList);
        if(getArguments() != null && getArguments().getBoolean("landscape")){
            adapter.setOnItemClickListener((v, user) ->{
                EventBus.getDefault().post(user);
            });
        }
        else {
            adapter.setOnItemClickListener((v, user) -> {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                int id = user.getId();
                InfoFragment infoFragment = new InfoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Users", userArrayList);
                bundle.putInt(MySQLiteOpenHelper.USER_COLUMN_ID, id);
                infoFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainframe, infoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
    }

    private void initUsers() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this.getContext(), "UserPool.db", null, 1);
        db = mySQLiteOpenHelper.getReadableDatabase();
        userArrayList.clear();
        for(int i = 1; i < 21; i++) {
            Cursor cursor = mySQLiteOpenHelper.getUser(i, db);
            cursor.moveToFirst();
            String userName = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_NAME));
            String image = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_IMAGE));
            int rating = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_RATING));
            String info = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_INFO));
            User user = new User(i,userName, image, info, rating);
            userArrayList.add(user);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        db.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
