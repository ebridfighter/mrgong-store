package uk.co.ribot.androidboilerplate.data.model;

/**
 * 库存列表item
 *
 * Created by Dong on 2017/11/1.
 */

public class StockItem {
    /**
     * code : 11012215
     * inventoryValue : 108.0
     * lifeEndDate : 2017-07-27 00:00:00
     * lotID : 287
     * lotNum : Z170827000010
     * qty : 18.0
     * uom : 包
     * productID : 20
     */

    private String code;
    private double inventoryValue;
    private String lifeEndDate;
    private int lotID;
    private String lotNum;
    private double qty;
    private String uom;
    private int productID;
    int  ImageId;

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getInventoryValue() {
        return inventoryValue;
    }

    public void setInventoryValue(double inventoryValue) {
        this.inventoryValue = inventoryValue;
    }

    public String getLifeEndDate() {
        return lifeEndDate;
    }

    public void setLifeEndDate(String lifeEndDate) {
        this.lifeEndDate = lifeEndDate;
    }

    public int getLotID() {
        return lotID;
    }

    public void setLotID(int lotID) {
        this.lotID = lotID;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
