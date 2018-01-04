package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.ProcurementResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2018/1/4.
 */
public interface ProcurementFragmentMvpView extends MvpView {
    void showList(ProcurementResponse procurementResponse);

    void showError(String message);
}
