package uk.co.ribot.androidboilerplate.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by mike on 2017/10/10.
 */
@AutoValue
public abstract class ProductProfile implements Parcelable{

    public abstract String name();
    public abstract boolean isTwoUnit();
    public abstract double settlePrice();

    public abstract String uom();
    public abstract String settleUomId();
    public abstract double price();

    public abstract String barcode();
    public abstract String defaultCode();
    public abstract String stockType();

    public abstract String category();
    public abstract String unit();
    public abstract String productUom();

    public abstract int productID();
    public abstract String tracking();

    public static Builder builder() {
        return new AutoValue_ProductProfile.Builder();
    }
    public static TypeAdapter<ProductProfile> typeAdapter(Gson gson) {
        return new AutoValue_ProductProfile.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);
        public abstract Builder setIsTwoUnit(boolean isTwoUnit);
        public abstract Builder setSettlePrice(double settlePrice);

        public abstract Builder setUom(String uom);
        public abstract Builder setSettleUomId(String settleUomId);
        public abstract Builder setPrice(double price);

        public abstract Builder setBarcode(String barcode);
        public abstract Builder setDefaultCode(String defaultCode);
        public abstract Builder setStockType(String stockType);

        public abstract Builder setCategory(String category);
        public abstract Builder setUnit(String unit);
        public abstract Builder setProductUom(String productUom);

        public abstract Builder setProductID(int productID);
        public abstract Builder setTracking(String tracking);
        public abstract ProductProfile build();
    }
}
