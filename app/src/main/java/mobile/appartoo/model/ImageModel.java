package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by alexandre on 16-07-06.
 */
public class ImageModel implements Serializable {

    @SerializedName("@id")
    private String id;
    private String caption;
    private String contentUrl;
    private ImageModel thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public ImageModel getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageModel thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String toString(){
        String str;

        if(thumbnail != null) {
            str = "ImageModel: {id: " + id + ", " +
                    "caption: " + caption + ", " +
                    "contentUrl: " + contentUrl + ", " +
                    "thumbnail: " + thumbnail.toString() + "}";
        } else {
            str = "ImageModel: {id: " + id + ", " +
                    "caption: " + caption + ", " +
                    "contentUrl: " + contentUrl + ", " +
                    "thumbnail: null}";
        }

        return str;
    }
}
