package mobile.appartoo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandre on 16-07-06.
 */
public class ImageModel implements JsonModel {
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

    @Override
    public ImageModel createFromJSON(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("@id");
        this.caption = jsonObject.getString("caption");
        this.contentUrl = jsonObject.getString("contentUrl");

        try {
            this.thumbnail = new ImageModel().createFromJSON(jsonObject.getJSONObject("thumbnail"));
        } catch (JSONException e) {
            this.thumbnail = null;
        }
        return this;
    }
}
