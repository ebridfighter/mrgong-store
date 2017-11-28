package uk.co.ribot.androidboilerplate.data.model.net.request;

/**
 * Created by mike on 2017/11/28.
 */

public class GetInventoryListRequest {
    private int pz;
    private int limit;
    private int date_type;
    public int getPz() {
        return pz;
    }

    public void setPz(int pz) {
        this.pz = pz;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getDate_type() {
        return date_type;
    }

    public void setDate_type(int date_type) {
        this.date_type = date_type;
    }
}
