package uk.co.ribot.androidboilerplate.tools;

import android.content.Context;
import android.widget.ImageView;

import com.facebook.drawee.view.DraweeView;

import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;

/**
 * Created by mike on 2017/11/22.
 */

public class ImageManager {
    public static void load(Context context, ImageView imageView) {
        FrecoFactory.getInstance(context).disPlay((DraweeView) imageView, "");
    }

    public static void load(Context context, ImageView imageView,String url) {
        FrecoFactory.getInstance(context).disPlay((DraweeView) imageView, RunwiseService.ENDPOINT+url);
    }
}
