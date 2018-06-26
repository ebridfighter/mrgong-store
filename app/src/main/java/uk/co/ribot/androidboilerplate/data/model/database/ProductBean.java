package uk.co.ribot.androidboilerplate.data.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import uk.co.ribot.androidboilerplate.data.local.AppDatabase;

/**
 * Created by mike on 2018/6/24.
 */
@Table(database = AppDatabase.class)
public class ProductBean extends BaseModel implements Serializable {
    @Column
    private String name;
    @Column
    private boolean isTwoUnit;
    @Column
    private String uom;
    @Column
    private double price;
    @Column
    private String barcode;
    @Column
    private String defaultCode;
    @Column
    private String stockType;
    @Column
    private String category;
    @Column
    private String categoryParent;
    @Column
    private String categoryChild;
    @Column
    private String unit;
    @Column
    private String productUom;
    @Column
    private int productID;
    @PrimaryKey(autoincrement = true)
    private long id;
    @ForeignKey
    private ImageBean image;
    @Column
    private String tracking;
    @Column
    private String stockUom;//库存单位
    @Column
    private String saleUom;//销售单位
    @Column
    private String productTag;
    @Column
    private int orderBy;
    @Column
    private boolean subValid;//True就是用来下单的 False就不用来下单的
    //本地数据
    private boolean isInvalid;
    private boolean cacheSelected;
    private long cartAddedTime;//加入购物车时间,用于排序
    private String remark;//备注
    private double count;
    private double actualQty;
    private double presetQty;
    public String imageLocal;

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


    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
    }

    public String getCategoryChild() {
        return categoryChild;
    }

    public void setCategoryChild(String categoryChild) {
        this.categoryChild = categoryChild;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProductUom() {
        return productUom;
    }

    public void setProductUom(String productUom) {
        this.productUom = productUom;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getStockUom() {
        return stockUom;
    }

    public void setStockUom(String stockUom) {
        this.stockUom = stockUom;
    }

    public String getSaleUom() {
        return saleUom;
    }

    public void setSaleUom(String saleUom) {
        this.saleUom = saleUom;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isSubValid() {
        return subValid;
    }

    public void setSubValid(boolean subValid) {
        this.subValid = subValid;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public boolean isCacheSelected() {
        return cacheSelected;
    }

    public void setCacheSelected(boolean cacheSelected) {
        this.cacheSelected = cacheSelected;
    }

    public long getCartAddedTime() {
        return cartAddedTime;
    }

    public void setCartAddedTime(long cartAddedTime) {
        this.cartAddedTime = cartAddedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getActualQty() {
        return actualQty;
    }

    public void setActualQty(double actualQty) {
        this.actualQty = actualQty;
    }

    public double getPresetQty() {
        return presetQty;
    }

    public void setPresetQty(double presetQty) {
        this.presetQty = presetQty;
    }

    public String getImageLocal() {
        return imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }


}
