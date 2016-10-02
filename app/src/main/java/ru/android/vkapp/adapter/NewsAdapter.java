package ru.android.vkapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.android.vkapp.activities.PostActivity;
import ru.android.vkapp.R;
import ru.android.vkapp.model.ResponseVk;

/**
 * Created by yasina on 25.09.2016.
 */
public class NewsAdapter extends
        RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private List<ResponseVk.Response.Item> mRecords;
    private HashMap<Long, ResponseVk.Response.Group> mGroups = new HashMap<>();
    private HashMap<Long, ResponseVk.Response.Profile> mProfiles = new HashMap<>();
    Context mContext;

    public NewsAdapter(Context context,
                       List<ResponseVk.Response.Item> records,
                       HashMap<Long, ResponseVk.Response.Group> groups,
                       HashMap<Long, ResponseVk.Response.Profile> profiles){

        mContext = context;
        mRecords = records;
        mGroups = groups;
        mProfiles = profiles;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.news_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {
        final ResponseVk.Response.Item post = mRecords.get(position);
        CardView postContainer = holder.mPostContainer;
        ImageView postAuthorPhotoView = holder.mAuthorPhotoView;
        TextView postTextView = holder.mPostTextView;
        TextView postDateView = holder.mPostDateView;
        TextView postAuthorNameView = holder.mAuthorNameView;
        ImageView postImageView = holder.mPostImageView;

        setAuthorInfo(post.source_id, postAuthorNameView, postAuthorPhotoView);
        postDateView.setText(takeFormattedDate(post.date));
        postTextView.setText(takeCutText(post.text));

        final String imageUrlExtra = setPostImageAndGetUrl(post, postImageView);;
        postContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra(PostActivity.POST_TEXT_EXTRA, post.text);
                intent.putExtra(PostActivity.POST_IMAGE_URL_EXTRA, imageUrlExtra);
                intent.putExtra(PostActivity.POST_LIKES_COUNT_EXTRA, post.likes.count);
                intent.putExtra(PostActivity.POST_REPOSTS_COUNT_EXTRA, post.reposts.count);
                mContext.startActivity(intent);
            }
        });
    }

    public void setAuthorInfo(@NonNull long source_id,
                              @NonNull TextView authorNameView,
                              @NonNull ImageView authorPhotoView){
        if (source_id < 0) {
            //if author is group
            ResponseVk.Response.Group group = mGroups.get(source_id * (-1));
            authorNameView.setText(group.name);
            drawAuthorImage(group.photo_50, authorPhotoView);
        } else {
            //if author is user
            ResponseVk.Response.Profile profile = mProfiles.get(source_id);
            authorNameView.setText(profile.first_name + " " + profile.last_name);
            drawAuthorImage(profile.photo_50, authorPhotoView);
        }
    }

    private void drawAuthorImage(@NonNull String url,@NonNull ImageView imageView){
        Glide.with(mContext)
                .load(url)
                .into(imageView);
    }

    @Nullable
    private String setPostImageAndGetUrl(@NonNull ResponseVk.Response.Item post,
                                         @NonNull ImageView imageView){
        String imageUrl = null;

        //try to get post image url
        try {
            imageUrl = post.attachments[0].photo.photo_604;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            imageUrl = post.attachments[0].doc.preview.photo.sizes[0].src;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        Glide.clear(imageView);
        //draw post image
        if(imageUrl!=null) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        return imageUrl;
    }

    @NonNull
    public String takeFormattedDate(@NonNull long date){
        return new SimpleDateFormat("HH:mm   dd MMM yyyy")
                .format(new Date(date*1000L));
    }

    @NonNull
    public String takeCutText(@NonNull String text){
        //cut text if it's so long
        if(text.length()>500){
            text = text.substring(0, 400) + "..." + "\n\n" + "read more...";
        }
        return text;
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mPostContainer;
        private ImageView mAuthorPhotoView;
        private TextView mAuthorNameView;
        private TextView mPostDateView;
        private TextView mPostTextView;
        private ImageView mPostImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPostContainer = (CardView) itemView.findViewById(R.id.news_cv);
            mAuthorPhotoView = (ImageView) itemView.findViewById(R.id.item_post_author_photo);
            mAuthorNameView = (TextView) itemView.findViewById(R.id.item_post_author_name);
            mPostDateView = (TextView) itemView.findViewById(R.id.item_post_date);
            mPostTextView = (TextView) itemView.findViewById(R.id.item_post_text);
            mPostImageView = (ImageView) itemView.findViewById(R.id.item_post_image);
        }
    }
}
