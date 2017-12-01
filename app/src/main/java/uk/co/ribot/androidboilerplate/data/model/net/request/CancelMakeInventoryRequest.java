package uk.co.ribot.androidboilerplate.data.model.net.request;

/**
 * Created by mike on 2017/11/28.
 */

public class CancelMakeInventoryRequest {
    /**
     * id : 245
     * state : draft
     */

    private int id;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
