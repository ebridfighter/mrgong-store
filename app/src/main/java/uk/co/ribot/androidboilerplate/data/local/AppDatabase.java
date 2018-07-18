package uk.co.ribot.androidboilerplate.data.local;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import uk.co.ribot.androidboilerplate.data.model.database.CategoryChildBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

/**
 * Created by mike on 2018/6/24.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "androidRunwise";
    //数据库版本号
    public static final int VERSION = 2;

    /**
     * 数据库的修改：
     * 1、PatientSession 表结构的变化
     * 2、增加表字段，考虑到版本兼容性，老版本不建议删除字段
     */
    @Migration(version = VERSION, database = AppDatabase.class)
    public static class Migration2Product extends AlterTableMigration<ProductBean> {

        public Migration2Product(Class<ProductBean> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
//            addColumn(SQLiteType.TEXT, "content");

        }
    }
    @Migration(version = VERSION, database = AppDatabase.class)
    public static class Migration2CategoryChild extends AlterTableMigration<CategoryChildBean> {

        public Migration2CategoryChild(Class<CategoryChildBean> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "orderBy");

        }
    }
}
