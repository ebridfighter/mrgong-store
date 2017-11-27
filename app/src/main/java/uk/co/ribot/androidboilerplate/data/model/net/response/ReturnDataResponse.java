package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.List;

/**
 * Created by mike on 2017/11/27.
 */

public class ReturnDataResponse {
    private List<ReturnOrderListResponse.ListBean> lastWeekList;
    private List<ReturnOrderListResponse.ListBean> thisWeekList;
    private List<ReturnOrderListResponse.ListBean> allList;
    private List<ReturnOrderListResponse.ListBean> earlierList;

    public List<ReturnOrderListResponse.ListBean> getLastWeekList() {
        return lastWeekList;
    }

    public void setLastWeekList(List<ReturnOrderListResponse.ListBean> lastWeekList) {
        this.lastWeekList = lastWeekList;
    }

    public List<ReturnOrderListResponse.ListBean> getThisWeekList() {
        return thisWeekList;
    }

    public void setThisWeekList(List<ReturnOrderListResponse.ListBean> thisWeekList) {
        this.thisWeekList = thisWeekList;
    }

    public List<ReturnOrderListResponse.ListBean> getAllList() {
        return allList;
    }

    public void setAllList(List<ReturnOrderListResponse.ListBean> allList) {
        this.allList = allList;
    }

    public List<ReturnOrderListResponse.ListBean> getEarlierList() {
        return earlierList;
    }

    public void setEarlierList(List<ReturnOrderListResponse.ListBean> earlierList) {
        this.earlierList = earlierList;
    }
}
