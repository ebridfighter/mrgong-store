package uk.co.ribot.androidboilerplate.data.model.business;

import android.os.Parcel;
import android.os.Parcelable;

import uk.co.ribot.androidboilerplate.data.model.Image;

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
    private Product product;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static class Product extends BaseProduct implements Parcelable{
//             "category":"",
//            "stockType":"",
//            "name":"老班长-辣椒油",
//            "barcode":"",
//            "tracking":"lot",
//            "image":{
//                 "imageMedium":"",
//                "image":"",
//                "imageSmall":""
//    },
//            "defaultCode":"2.02.0068",
//            "unit":"1kg/袋",
//            "productID":737
        private String category;
        private String stockType;
        private String name;
        private String barcode;
        private String tracking;
        private String defaultCode;
        private String unit;
        private Image image;

        public String getCategory() {
            return category;
        }

        public String getStockType() {
            return stockType;
        }

        public String getName() {
            return name;
        }

        public String getBarcode() {
            return barcode;
        }

        public String getTracking() {
            return tracking;
        }

        public String getDefaultCode() {
            return defaultCode;
        }

        public String getUnit() {
            return unit;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setStockType(String stockType) {
            this.stockType = stockType;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public void setTracking(String tracking) {
            this.tracking = tracking;
        }

        public void setDefaultCode(String defaultCode) {
            this.defaultCode = defaultCode;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest,flags);
            dest.writeString(this.category);
            dest.writeString(this.stockType);
            dest.writeString(this.name);
            dest.writeString(this.barcode);
            dest.writeString(this.tracking);
            dest.writeString(this.defaultCode);
            dest.writeString(this.unit);
            dest.writeParcelable(this.image, flags);
        }

        public Product() {
        }

        protected Product(Parcel in) {
            super(in);
            this.category = in.readString();
            this.stockType = in.readString();
            this.name = in.readString();
            this.barcode = in.readString();
            this.tracking = in.readString();
            this.defaultCode = in.readString();
            this.unit = in.readString();
            this.image = in.readParcelable(Image.class.getClassLoader());
        }

        public static final Creator<Product> CREATOR = new Creator<Product>() {
            @Override
            public Product createFromParcel(Parcel source) {
                return new Product(source);
            }

            @Override
            public Product[] newArray(int size) {
                return new Product[size];
            }
        };
    }

}
