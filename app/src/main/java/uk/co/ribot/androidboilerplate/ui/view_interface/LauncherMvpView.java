package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/29.
 */
public interface LauncherMvpView extends MvpView {
    void countDownFinish();
    void onReceiveServerTime(long serverTime);
}
