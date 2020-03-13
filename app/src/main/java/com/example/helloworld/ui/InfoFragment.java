package com.example.helloworld.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.helloworld.adapter.CardViewAdapter;
import com.example.helloworld.adapter.CustomPagerHelper;
import com.example.helloworld.adapter.HorizontalLayoutManager;
import com.example.helloworld.utils.MySQLiteOpenHelper;
import com.example.helloworld.R;
import com.example.helloworld.utils.RecyclerViewPageChangeListenerHelper;
import com.example.helloworld.utils.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InfoFragment extends Fragment {

    private Unbinder unbinder;
    private int id;
    private ArrayList<User> userList;
    MySQLiteOpenHelper mySQLiteOpenHelper;
    CardViewAdapter adapter;

    @BindView(R.id.card_list)
    RecyclerView cardListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container,false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        initPortraitMode(bundle);
        return view;
    }


    private void initPortraitMode(Bundle bundle) {
        userList = bundle.getParcelableArrayList("Users");
        id = bundle.getInt(MySQLiteOpenHelper.USER_COLUMN_ID);
        adapter = new CardViewAdapter(userList);
        HorizontalLayoutManager linearLayoutManager = new HorizontalLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.scrollToPositionWithOffset(id - 1, 0);
        linearLayoutManager.setStackFromEnd(true);
        cardListView.setLayoutManager(linearLayoutManager);
        adapter.setListener((rating, id) -> mySQLiteOpenHelper.updateUser(id, rating));
        cardListView.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(cardListView);
        ((SimpleItemAnimator) cardListView.getItemAnimator()).setSupportsChangeAnimations(false);
        cardListView.addOnScrollListener(new RecyclerViewPageChangeListenerHelper(snapHelper,
                new RecyclerViewPageChangeListenerHelper.OnPageChangeListener() {


                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    }

                    @Override
                    public void onPageSelected(int position) {


                        int pos = id - 1;
                        adapter.notifyDataSetChanged();
                        if (pos == 0 && position == userList.size() - 1) {
                            mySQLiteOpenHelper.updateUser(id, 0);
                            adapter.getUserList().get(pos).setRating(0);
                            id = userList.size();
                            return;
                        }
                        if (pos == userList.size() - 1 && position == 0) {
                            mySQLiteOpenHelper.updateUser(id, 5);
                            adapter.getUserList().get(pos).setRating(5);
                            id = 1;
                            return;
                        }
                        if ((pos) < position) {
                            //左滑
                            mySQLiteOpenHelper.updateUser(id, 5);
                            adapter.getUserList().get(pos).setRating(5);
                            id++;
                        } else {
                            //右滑
                            mySQLiteOpenHelper.updateUser(id, 0);
                            adapter.getUserList().get(pos).setRating(0);
                            id--;
                        }

                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this.getContext(), "UserPool.db", null, 1);

    }

}
