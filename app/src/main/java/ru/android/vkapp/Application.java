package ru.android.vkapp;

import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import ru.android.vkapp.activities.MainActivity;

/**
 * Created by yasina on 25.09.2016.
 */
public class Application extends android.app.Application {

    public static final String VK_ACCESS_TOKEN = "VK_ACCESS_TOKEN";
    VKAccessToken accessToken;
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                Intent intent = new Intent(Application.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        accessToken= VKAccessToken.tokenFromSharedPreferences(this, VK_ACCESS_TOKEN);
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}
