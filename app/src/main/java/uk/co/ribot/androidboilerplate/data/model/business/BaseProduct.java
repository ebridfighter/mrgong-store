package uk.co.ribot.androidboilerplate.data.model.business;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 基本的商品数据类型
 *
 * Created by Dong on 2017/11/10.
 */

public class BaseProduct implements Parcelable {

    public static final String TRACKING_LOT = "lot";
    public static final String TRACKING_NONE = "none";

    private int productID;

    public BaseProduct() {
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productID);
    }

    protected BaseProduct(Parcel in) {
        this.productID = in.readInt();
    }

    public static final Creator<BaseProduct> CREATOR = new Creator<BaseProduct>() {
        @Override
        public BaseProduct createFromParcel(Parcel source) {
            return new BaseProduct(source);
        }

        @Override
        public BaseProduct[] newArray(int size) {
            return new BaseProduct[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseProduct that = (BaseProduct) o;

        return productID == that.productID;

    }

    @Override
    public int hashCode() {
        return productID;
    }
}
