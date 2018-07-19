package uk.co.ribot.androidboilerplate.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.fragment.ProductListFragment;

public final class ViewUtil {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }


    public static void addTvAnim(View fromView, int[] carLoc, Context context, final ViewGroup rootview,ProductListFragment.AnimationFinishCallBack animationFinishCallBack) {
        int[] addLoc = new int[2];
        fromView.getLocationInWindow(addLoc);

        Path path = new Path();
        path.moveTo(addLoc[0], addLoc[1] - fromView.getHeight());
        path.quadTo(carLoc[0], addLoc[1] - 200, carLoc[0], carLoc[1]);

        final TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.circle_green);
        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(fromView.getWidth(), fromView.getHeight());
        rootview.addView(textView, lp);
        ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                rootview.removeView(textView);
                if (animationFinishCallBack != null){
                    animationFinishCallBack.onFinish();
                }
            }
        }).start();
    }

}
