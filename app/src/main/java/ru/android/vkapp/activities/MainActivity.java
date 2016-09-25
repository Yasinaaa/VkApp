package ru.android.vkapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.HashMap;
import java.util.List;

import ru.android.vkapp.R;
import ru.android.vkapp.adapter.NewsAdapter;
import ru.android.vkapp.model.Response;
import ru.android.vkapp.presenter.MainPresenter;
import ru.android.vkapp.view.MainView;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, MainView {

    private final String TAG = "MainActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsRcView;

    private MainPresenter mPresenter;
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mPresenter = new MainPresenter(this, getLoaderManager(), this);
        mPresenter.initVKSdk(this);
    }

    private void init(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_main);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.vk_share_blue_color);

        mNewsRcView = (RecyclerView) findViewById(R.id.rcv_news);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                mPresenter.refreshNews();
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, error.toString());
                finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //pagination
    private RecyclerView.OnScrollListener takeScrollListener(){
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && !mPresenter.isLoading()
                        && mLayoutManager.findFirstVisibleItemPosition() + 15 > mPresenter.getPostsListSize()){
                    mPresenter.setLoading(true);
                    mPresenter.loadMoreItems();
                }
            }
        };
    }

    //pull to refresh
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        Log.d(TAG, "on refresh");
        mPresenter.refreshNews();
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "on refresh run");
                mPresenter.refreshNews();
            }
        }, 3000);
    }


    public void onClickFabLogout(View view){
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            mPresenter.initVKSdk(this);
        }
    }

    @Override
    public void openNewsScreen() {

    }


    @Override
    public void showLoading(List<Response.Response.Item> mPosts,
                            HashMap<Long, Response.Response.Group> mGroups,
                            HashMap<Long, Response.Response.Profile> mProfiles) {
            if(mPosts!=null) {
                mNewsAdapter = new NewsAdapter(getApplicationContext(),
                        mPosts, mGroups, mProfiles);
                mLayoutManager = new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false);
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                mNewsRcView.setAdapter(mNewsAdapter);
                mNewsRcView.setLayoutManager(mLayoutManager);
                mNewsRcView.setItemAnimator(itemAnimator);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        mNewsRcView.addOnScrollListener(takeScrollListener());
    }

    @Override
    public void updateRecyclerView() {
        mNewsAdapter.notifyDataSetChanged();
    }

}
