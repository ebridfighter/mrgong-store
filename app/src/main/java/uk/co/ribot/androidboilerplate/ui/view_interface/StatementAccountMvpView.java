package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.StatementAccountListResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/28.
 */
public interface StatementAccountMvpView extends MvpView {
    void showStatementAccountList(StatementAccountListResponse statementAccountListResponse);
    void showStatementAccountListError(String error);
    void showStatementAccountListEmpty();
}
