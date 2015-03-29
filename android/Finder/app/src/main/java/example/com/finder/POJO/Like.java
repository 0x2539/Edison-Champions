package example.com.finder.POJO;

import android.graphics.Bitmap;

/**
 * Created by stefyvolt on 29.03.15.
 */
public class Like {
    private String name;

    private String pictureUrl;
    private Bitmap picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
