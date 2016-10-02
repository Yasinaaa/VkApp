package ru.android.vkapp.view;

import java.util.HashMap;
import java.util.List;

import ru.android.vkapp.model.ResponseVk;

/**
 * Created by yasina on 25.09.16.
 */
public interface LoadingView {

    void showLoading(List<ResponseVk.Response.Item> mPosts,
                     HashMap<Long, ResponseVk.Response.Group> mGroups,
                     HashMap<Long, ResponseVk.Response.Profile> mProfiles);

    void updateRecyclerView();
}
