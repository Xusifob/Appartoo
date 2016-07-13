package mobile.appartoo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by alexandre on 16-07-06.
 */
public interface JsonModel extends Serializable {
    public JsonModel createFromJSON(JSONObject jsonObject) throws JSONException;
}
