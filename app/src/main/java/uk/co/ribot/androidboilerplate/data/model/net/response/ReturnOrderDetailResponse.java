package uk.co.ribot.androidboilerplate.data.model.net.response;

/**
 * Created by libin on 2017/8/28.
 */

public class ReturnOrderDetailResponse {

    /**
     * returnOrder : {"amountTotal":2.22,"stateTracker":["2017-08-21 10:23 退货单已退货","2017-08-21 10:13 退货单退货中"],"doneDate":"2017-08-21 10:23:05","createDate":"2017-08-21 10:13:04","state":"done","vehicle":null,"isTwoUnit":false,"deliveryType":"vendor_delivery","hasAttachment":1,"driver":null,"isThirdPartLog":false,"doneDtate":"2017-08-21 10:23:05","loadingDate":null,"createUser":"周大福","isDispatch":false,"returnOrderID":147,"driveMobile":null,"orderID":577,"isPaid":false,"name":"RO17082100001","returnThirdPartLog":false,"lines":[{"productUom":"件","priceUnit":0.95,"tax":17,"discount":0,"deliveredQty":2,"priceSubtotal":1.9000000000000001,"productID":21,"saleOrderProductID":158,"lotIDs":[],"pickupWeight":0,"pickupNum":0,"stockType":"other","lotList":[],"productUomQty":2}],"amount":2}
     */

    private ReturnOrderListResponse.ListBean returnOrder;

    public ReturnOrderListResponse.ListBean getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(ReturnOrderListResponse.ListBean returnOrder) {
        this.returnOrder = returnOrder;
    }
}
