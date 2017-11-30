package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.HostResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/9/28.
 */

public interface LoginMvpView extends MvpView {


    void onSuccess();

    void showError(String error);

    void loginConflict();

    void getHostSuccess(HostResponse hostResponse);

    void getHostError(String error);
}
