package com.articles.app.Models;

import java.util.List;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.parceler.Generated;

@Generated("jsonschema2pojo")
public class ArticlesResponse implements Parcelable
{

    @SerializedName("data")
    @Expose
    private List<com.articles.app.Models.Datum> data = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    public final static Creator<ArticlesResponse> CREATOR = new Creator<ArticlesResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ArticlesResponse createFromParcel(android.os.Parcel in) {
            return new ArticlesResponse(in);
        }

        public ArticlesResponse[] newArray(int size) {
            return (new ArticlesResponse[size]);
        }

    }
    ;

    protected ArticlesResponse(android.os.Parcel in) {
        in.readList(this.data, (com.articles.app.Models.Datum.class.getClassLoader()));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ArticlesResponse() {
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(data);
        dest.writeValue(page);
        dest.writeValue(pages);
    }

    public int describeContents() {
        return  0;
    }

}
