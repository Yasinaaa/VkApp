package ru.android.vkapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yasina on 25.09.2016.
 */
public class ResponseVk {
    @SerializedName("response")
    public Response response;

    public static class Response{

        @SerializedName("items")
        public Item[] items;

        @SerializedName("profiles")
        public Profile[] profiles;

        @SerializedName("groups")
        public Group[] groups;

        @SerializedName("next_from")
        public String next_from;

        public static class Item{

            @SerializedName("type")
            public String type;

            @SerializedName("source_id")
            public long source_id;

            @SerializedName("date")
            public long date;

            @SerializedName("post_id")
            public long post_id;

            @SerializedName("post_type")
            public String post_type;

            @SerializedName("text")
            public String text;

            @SerializedName("likes")
            public Likes likes;

            @SerializedName("reposts")
            public Reposts reposts;

            @SerializedName("attachments")
            public Attachment[] attachments;

            public static class Likes{
                @SerializedName("count")
                public int count;
            }

            public static class Reposts{
                @SerializedName("count")
                public int count;
            }

            public static class Attachment{
                @SerializedName("type")
                public String type;

                @SerializedName("photo")
                public Photo photo;

                @SerializedName("doc")
                public Doc doc;

                public static class Photo{
                    @SerializedName("photo_75")
                    public String photo_75;

                    @SerializedName("photo_130")
                    public String photo_130;

                    @SerializedName("photo_604")
                    public String photo_604;

                    @SerializedName("photo_1280")
                    public String photo_1280;

                    @SerializedName("photo_2560")
                    public String photo_2560;
                }

                public class Doc{

                    @SerializedName("ext")
                    public String ext;

                    @SerializedName("url")
                    public String url;

                    @SerializedName("preview")
                    public Preview preview;

                    public class Preview{

                        @SerializedName("photo")
                        public Photo photo;

                        public class Photo{

                            @SerializedName("size")
                            public Size[] sizes;

                            public class Size{

                                @SerializedName("src")
                                public String src;
                            }
                        }
                    }
                }
            }

            @SerializedName("copy_history")
            public Item[] copy_history;
        }

        public static class Profile{
            @SerializedName("id")
            public long id;

            @SerializedName("first_name")
            public String first_name;

            @SerializedName("last_name")
            public String last_name;

            @SerializedName("photo_50")
            public String photo_50;
        }

        public static class Group{

            @SerializedName("id")
            public long id;

            @SerializedName("name")
            public String name;

            @SerializedName("photo_50")
            public String photo_50;
        }
    }
}
