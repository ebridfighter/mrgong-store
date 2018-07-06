package uk.co.ribot.androidboilerplate.data.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import uk.co.ribot.androidboilerplate.data.local.AppDatabase;

@Table(database = AppDatabase.class)
public class CategoryChildBean extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    String name;
    @Column
    String categoryParent;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
