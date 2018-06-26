package uk.co.ribot.androidboilerplate.data.model.database;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;


@AutoValue
public abstract class NameBean implements Parcelable {
    public abstract String first();
    public abstract String last();

    public static NameBean create(String first, String last) {
        return new AutoValue_NameBean(first, last);
    }

    public static TypeAdapter<NameBean> typeAdapter(Gson gson) {
        return new AutoValue_NameBean.GsonTypeAdapter(gson);
    }

}
