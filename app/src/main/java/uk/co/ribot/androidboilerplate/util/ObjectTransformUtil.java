package uk.co.ribot.androidboilerplate.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mike on 2017/11/2.
 */

public class ObjectTransformUtil {
    public static String toString(Object object) {
        String[] names = ClassReflexUtil.getFiledName(object);
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < names.length; i++) {
            try {
                jsonObject.put(names[i], ClassReflexUtil.getFieldValueByName(names[i], object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public static Object toObject(String sourceStr, Class classOfT) {
        Object object = new Gson().fromJson(sourceStr, classOfT);
        return object;
    }
}
