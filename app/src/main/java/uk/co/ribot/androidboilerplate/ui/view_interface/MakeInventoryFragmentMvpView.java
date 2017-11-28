package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/28.
 */
public interface MakeInventoryFragmentMvpView extends MvpView {
    void showInventoryList(InventoryResponse inventoryResponse);
    void showInventoryListError(String error);
    void showInventoryListEmpty();

    void cancelMakeInventory();
    void cancelMakeInventoryError(String error);

}
