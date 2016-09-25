package ru.android.vkapp.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.android.vkapp.model.Response;
import ru.android.vkapp.view.MainView;

/**
 * Created by yasina on 25.09.16.
 */
public class MainPresenter {

    private final String TAG = "MainPresenter";
    private final String[] mScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.GROUPS,
            VKScope.DIRECT,
            VKScope.PHOTOS
    };
    private final String NEWSFEED = "newsfeed.get";
    private final String POST = "post";
    private final String TEXT = "text";
    private final String START = "start_from";
    private final int COUNT = 20;

    private final Context mContext;
    private final LoaderManager mLm;
    private final MainView mView;

    private boolean isLoading = false;
    private String lastPostInfo;
    private boolean isResumed = false;
    private Gson mGson;
    private List<Response.Response.Item> mPosts;
    private HashMap<Long, Response.Response.Group> mGroups;
    private HashMap<Long, Response.Response.Profile> mProfiles;


    public MainPresenter(Context context, @NonNull LoaderManager lm,
                          @NonNull MainView view) {
        mContext = context;
        mLm = lm;
        mView = view;
        init();
    }

    private void init(){
        mGson = new GsonBuilder().create();
        mPosts = new ArrayList<>();
        mGroups = new HashMap<>();
        mProfiles = new HashMap<>();
    }

    public void initVKSdk(final Activity activity){
        VKSdk.wakeUpSession(activity, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                Log.d(TAG, res.name() + isResumed);

                switch (res) {
                    case LoggedOut:
                        VKSdk.login(activity, mScope);
                        break;
                    case LoggedIn:
                        break;
                    case Pending:
                        VKSdk.login(activity, mScope);
                        break;
                    case Unknown:
                        VKSdk.login(activity, mScope);
                        break;
                }
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(mContext, "Wake up error", Toast.LENGTH_LONG).show();
                Log.d(TAG, error.toString());
            }
        });
    }

    public void refreshNews(){
        VKRequest request =
                new VKRequest(NEWSFEED, VKParameters.from(
                        VKApiConst.FILTERS, POST,
                        VKApiConst.FIELDS, TEXT));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "refreshNews response - " + response.responseString);

                Response postsResponse = mGson
                        .fromJson(response.responseString, Response.class);
                Response.Response.Item[] posts = postsResponse.response.items;
                mGroups.clear();
                mProfiles.clear();

                takeAuthors(postsResponse);
                mPosts.clear();

                for(int i=0;i<posts.length;i++){
                    mPosts.add(posts[i]);
                }

                lastPostInfo = postsResponse.response.next_from;
                mView.showLoading(mPosts, mGroups, mProfiles);
            }
        });
    }


    private void takeAuthors(Response postsResponse){

        for(Response.Response.Group group : postsResponse.response.groups){
            mGroups.put(group.id, group);
        }

        for(Response.Response.Profile profile : postsResponse.response.profiles){
            mProfiles.put(profile.id, profile);
        }
    }

    public void loadMoreItems(){
        VKRequest request =
                new VKRequest(NEWSFEED, VKParameters.from(
                        VKApiConst.FILTERS, POST,
                        START, lastPostInfo,
                        VKApiConst.COUNT, COUNT,
                        VKApiConst.FIELDS, TEXT));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "loadMoreItems response- " + response.responseString);

                Response postsResponse = mGson
                        .fromJson(response.responseString, Response.class);
                Response.Response.Item[] posts = postsResponse.response.items;
                lastPostInfo = postsResponse.response.next_from;
                isLoading=false;
                for(int i=0;i<posts.length;i++){
                    mPosts.add(posts[i]);
                }
                takeAuthors(postsResponse);
                mView.updateRecyclerView();
            }
        });
    }

    public int getPostsListSize(){
        return mPosts.size();
    }

    public boolean isLoading(){
        return isLoading;
    }

    public void setLoading(boolean loading){
        isLoading = loading;
    }

}
