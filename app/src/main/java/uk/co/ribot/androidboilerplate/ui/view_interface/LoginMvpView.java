package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/9/28.
 */

public interface LoginMvpView extends MvpView{

    void showProgressDialog();
    void hideProgressDialog();

    void onSuccess();
    void showError(String error);
    void loginConflict();

}
