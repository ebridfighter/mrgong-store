package uk.co.ribot.androidboilerplate.data.model.event;

/**
 * Created by mike on 2017/11/2.
 */

public class LogOutEvent {
    boolean mSessionExpired;

    public boolean isSessionExpired() {
        return mSessionExpired;
    }

    public void setSessionExpired(boolean sessionExpired) {
        mSessionExpired = sessionExpired;
    }
}
