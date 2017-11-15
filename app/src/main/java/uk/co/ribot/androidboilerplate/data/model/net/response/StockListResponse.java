package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.business.StockItem;

/**
 * 库存列表接口返回
 *
 * Created by Dong on 2017/11/1.
 */
public class StockListResponse {
    private List<StockItem> list;

    public List<StockItem> getList() {
        return list;
    }

    public void setList(List<StockItem> list) {
        this.list = list;
    }

}
