package uk.co.ribot.androidboilerplate.data.model.event;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

public class ProductCountUpdateEvent {
    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    Object exception;

    public ProductCountUpdateEvent(){}
    public ProductCountUpdateEvent(ProductBean listBean, double count){
        this.bean = listBean;
        this.count = count;
    }

    public ProductCountUpdateEvent(List<ProductBean> beanList){
        this.beanList = beanList;
    }


    public ProductBean bean;
    public List<ProductBean> beanList;
    public double count;
}
