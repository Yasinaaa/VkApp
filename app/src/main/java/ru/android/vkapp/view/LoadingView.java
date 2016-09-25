package ru.android.vkapp.view;

import java.util.HashMap;
import java.util.List;

import ru.android.vkapp.model.Response;

/**
 * Created by yasina on 25.09.16.
 */
public interface LoadingView {

    void showLoading(List<Response.Response.Item> mPosts,
                     HashMap<Long, Response.Response.Group> mGroups,
                     HashMap<Long, Response.Response.Profile> mProfiles);

    void updateRecyclerView();
}
