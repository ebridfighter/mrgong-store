package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.List;

/**
 * Created by libin on 2017/7/8.
 */

public class IntelligentProductDataResponse {

    private List<DefaultProductResponse> list;

    public List<DefaultProductResponse> getList() {
        return list;
    }

    public void setList(List<DefaultProductResponse> list) {
        this.list = list;
    }

}
