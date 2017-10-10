package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mike on 2017/9/29.
 */

public class ProductListResponse implements Serializable {

    private List<Product> list;

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public static class Product implements Serializable {
        /**
         * name : 黑鱼（大型）
         * image : {"imageMedium":"/gongfu/image/product/8/image_medium/","image":"/gongfu/image/product/8/image/","imageSmall":"/gongfu/image/product/8/image_small/","imageID":8}
         * barcode :
         * defaultCode : 11012201
         * stockType : lengcanghuo
         * unit : 大于3斤/条
         * productID : 7
         */
        private String name;
        private boolean isTwoUnit;
        private float settlePrice;
        private String uom;
        private String settleUomId;
        private double price;
        private ImageResponse image;
        private String barcode;
        private String defaultCode;
        private String stockType;
        private String category;
        private String unit;
        private String productUom;
        private int productID;
        private String tracking;
        public static final String TRACKING_TYPE_LOT = "lot";
        public static final String TRACKING_TYPE_SERIAL = "serial";
        public static final String TRACKING_TYPE_NONE = "none";


        public String getTracking() {
            return tracking;
        }

        public void setTracking(String tracking) {
            this.tracking = tracking;
        }

        public String getProductUom() {
            return productUom;
        }

        public void setProductUom(String productUom) {
            this.productUom = productUom;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isTwoUnit() {
            return isTwoUnit;
        }

        public void setTwoUnit(boolean twoUnit) {
            isTwoUnit = twoUnit;
        }

        public float getSettlePrice() {
            return settlePrice;
        }

        public void setSettlePrice(float settlePrice) {
            this.settlePrice = settlePrice;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public String getSettleUomId() {
            return settleUomId;
        }

        public void setSettleUomId(String settleUomId) {
            this.settleUomId = settleUomId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public ImageResponse getImage() {
            return image;
        }

        public void setImage(ImageResponse image) {
            this.image = image;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getDefaultCode() {
            return defaultCode;
        }

        public void setDefaultCode(String defaultCode) {
            this.defaultCode = defaultCode;
        }

        public String getStockType() {
            return stockType;
        }

        public void setStockType(String stockType) {
            this.stockType = stockType;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getProductID() {
            return productID;
        }

        public void setProductID(int productID) {
            this.productID = productID;
        }


    }
}
