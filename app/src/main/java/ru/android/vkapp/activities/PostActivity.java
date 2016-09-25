package ru.android.vkapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ru.android.vkapp.R;

public class PostActivity extends AppCompatActivity {

    public static final String POST_TEXT_EXTRA = "POST_TEXT_EXTRA";
    public static final String POST_IMAGE_URL_EXTRA = "POST_IMAGE_URL_EXTRA";
    public static final String POST_LIKES_COUNT_EXTRA = "POST_LIKES_COUNT_EXTRA";
    public static final String POST_REPOSTS_COUNT_EXTRA = "POST_REPOSTS_COUNT_EXTRA";

    private String mPostText;
    private String mPostImageUrl;
    private int mPostLikesCount;
    private int mPostRepostsCount;

    private ImageView mPostImage;
    private TextView mPostView;
    private TextView mLikesView;
    private TextView mRepostsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mPostImage = (ImageView) findViewById(R.id.post_image);
        mPostView = (TextView) findViewById(R.id.post_text);
        mLikesView = (TextView) findViewById(R.id.post_likes_count);
        mRepostsView = (TextView) findViewById(R.id.post_reposts_count);

        takeIntentExtraParams();
        updateViews();
    }

    private void takeIntentExtraParams(){
        mPostImageUrl = getIntent().getStringExtra(POST_IMAGE_URL_EXTRA);
        mPostText = getIntent().getStringExtra(POST_TEXT_EXTRA);
        mPostLikesCount = getIntent().getIntExtra(POST_LIKES_COUNT_EXTRA, 0);
        mPostRepostsCount = getIntent().getIntExtra(POST_REPOSTS_COUNT_EXTRA, 0);
    }

    private void updateViews(){
        mPostView.setText(mPostText);
        Glide.with(this)
                .load(mPostImageUrl)
                .into(mPostImage);
        mLikesView.setText("Likes: " + mPostLikesCount);
        mRepostsView.setText("Reposts: " + mPostRepostsCount);
    }
}
