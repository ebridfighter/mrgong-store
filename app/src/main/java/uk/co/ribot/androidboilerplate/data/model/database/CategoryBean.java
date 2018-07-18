package uk.co.ribot.androidboilerplate.data.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

import uk.co.ribot.androidboilerplate.data.local.AppDatabase;

@Table(database = AppDatabase.class)
public class CategoryBean extends BaseModel implements Serializable {
    @Column
    int orderBy;
    @PrimaryKey
    private String categoryParent;

    private List<String> categoryChild;

    List<CategoryChildBean> categoryChildBeans;

    public String getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
    }
    public List<String> getCategoryChild() {
        return categoryChild;
    }

    public void setCategoryChild(List<String> categoryChild) {
        this.categoryChild = categoryChild;
    }
    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "categoryChildBeans")
    public List<CategoryChildBean> getCategoryChildBeans() {
        if (categoryChildBeans == null || categoryChildBeans.isEmpty()) {
            categoryChildBeans = SQLite.select()
                    .from(CategoryChildBean.class)
                    .where(CategoryChildBean_Table.categoryParent.eq(categoryParent))
            .orderBy(CategoryChildBean_Table.orderBy,true)
                    .queryList();
        }
        return categoryChildBeans;
    }
}
