package com.articles.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostInfo implements Parcelable {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("authorName")
    @Expose
    private String authorName;

    @SerializedName("authorEmail")
    @Expose
    private String authorEmail;

    @SerializedName("authorPhotoUrl")
    @Expose
    private String authorPhotoUrl;

    @SerializedName("contentImageURL")
    @Expose
    public String contentImageURL;

    public PostInfo(){}


    public PostInfo(String title, String content, String authorName, String authorEmail, String authorPhotoUrl, String contentImageURL) {
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.authorPhotoUrl = authorPhotoUrl;
        this.contentImageURL = contentImageURL;
    }

    protected PostInfo(android.os.Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.authorName = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.authorName = ((String) in.readValue((String.class.getClassLoader())));
        this.authorEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.authorPhotoUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.contentImageURL = ((String) in.readValue((String.class.getClassLoader())));
    }

    public static final Creator<PostInfo> CREATOR = new Creator<PostInfo>() {
        @Override
        public PostInfo createFromParcel(Parcel in) {
            return new PostInfo(in);
        }

        @Override
        public PostInfo[] newArray(int size) {
            return new PostInfo[size];
        }
    };

    public String getContentImageURL() {
        return contentImageURL;
    }

    public void setContentImageURL(String contentImageURL) {
        this.contentImageURL = contentImageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorPhotoUrl() {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String authorPhotoUrl) {
        this.authorPhotoUrl = authorPhotoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(authorName);
        dest.writeString(authorEmail);
        dest.writeString(authorPhotoUrl);
        dest.writeString(contentImageURL);
    }
}
