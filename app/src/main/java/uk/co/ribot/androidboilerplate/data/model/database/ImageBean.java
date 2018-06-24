package uk.co.ribot.androidboilerplate.data.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by mike on 2018/6/24.
 */

public class ImageBean extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String imageMedium;
    @Column
    private String image;
    @Column
    private String imageSmall;
    @Column
    private int imageID;
}
