package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by leo on 17-11-2.
 */

public interface MessageMvpView extends MvpView {
    void showMessage(List<MessageResponse.OrderBean> message);
    void showEmpty();
    void showMessagesEmpty();
    void showMessagesError();
}
