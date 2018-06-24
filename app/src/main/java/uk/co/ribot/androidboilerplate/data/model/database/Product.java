package uk.co.ribot.androidboilerplate.data.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by mike on 2018/6/24.
 */

public class Product extends BaseModel implements Serializable {
    @Column
    private String name;
    @Column
    private boolean isTwoUnit;
    @Column
    private float settlePrice;
    @Column
    private String uom;
    @Column
    private String settleUomId;
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


}
