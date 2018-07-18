package uk.co.ribot.androidboilerplate.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Condition;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean_Table;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryChildBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean_Table;
import uk.co.ribot.androidboilerplate.data.model.database.Ribot;
import uk.co.ribot.androidboilerplate.data.model.database.UserBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Ribot> setRibots(final Collection<Ribot> newRibots) {
        return Observable.create(new ObservableOnSubscribe<Ribot>() {
            @Override
            public void subscribe(ObservableEmitter<Ribot> e) throws Exception {
                if (e.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.RibotProfileTable.TABLE_NAME, null);
                    for (Ribot ribot : newRibots) {
                        long result = mDb.insert(Db.RibotProfileTable.TABLE_NAME,
                                Db.RibotProfileTable.toContentValues(ribot.profile()),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) e.onNext(ribot);
                    }
                    transaction.markSuccessful();
                    e.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public rx.Observable<List<Ribot>> getRibots() {
        return mDb.createQuery(Db.RibotProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.RibotProfileTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Ribot>() {
                    @Override
                    public Ribot call(Cursor cursor) {
                        return Ribot.create(Db.RibotProfileTable.parseCursor(cursor));
                    }
                });
    }

    public Observable<ProductListResponse> setProducts(final ProductListResponse productListResponse) {
        return Observable.create(new ObservableOnSubscribe<ProductListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<ProductListResponse> e) throws Exception {
                if (e.isDisposed()) return;
                if (productListResponse.getProducts().size() > 0) {
                    Delete.table(ProductBean.class);
                    Delete.table(CategoryBean.class);
                    Delete.table(CategoryChildBean.class);
                }
//                同步事务
//                FlowManager.getDatabase(AppDatabase.class)
//                        .executeTransaction(new ProcessModelTransaction.Builder<ProductBean>(
//                                BaseModel::insert
//                        ).addAll(productListResponse.getProducts()).build());
                //异步事务 存商品数据
                FlowManager.getDatabase(AppDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<ProductBean>(
                                BaseModel::save
                        ).addAll(productListResponse.getProducts()).build())
                        .error(new Transaction.Error() {
                            @Override
                            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                                Timber.e(error, "There was an error save the products.");
                            }
                        })
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(@NonNull Transaction transaction) {
                                e.onNext(productListResponse);
                            }
                        })
                        .build()
                        .execute();

                setCategoryChild(productListResponse.getCategory());
                setCategoryParent(productListResponse.getCategory());
            }
        });
    }

    private void setCategoryParent(List<CategoryBean> categoryBeanList){
        //异步事务 存商品父分类数据
        FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<CategoryBean>(
                        BaseModel::save
                ).addAll(categoryBeanList).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Timber.e(error, "There was an error save the CategoryParents.");
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(@NonNull Transaction transaction) {

                    }
                })
                .build()
                .execute();
    }


    private void setCategoryChild(List<CategoryBean> categoryBeanList) {
        List<CategoryChildBean> categoryChildBeanList = new ArrayList<>();
        for (CategoryBean categoryBean : categoryBeanList) {
            List<String> categoryChildList = categoryBean.getCategoryChild();
            for (int i = 0;i<categoryChildList.size();i++){
                String categoryChildName = categoryChildList.get(i);
                CategoryChildBean categoryChildBean = new CategoryChildBean();
                categoryChildBean.setOrderBy(i);
                categoryChildBean.setCategoryParent(categoryBean.getCategoryParent());
                categoryChildBean.setName(categoryChildName);
                categoryChildBeanList.add(categoryChildBean);
            }
        }
        //异步事务 存商品子分类数据
        FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<CategoryChildBean>(
                        BaseModel::save
                ).addAll(categoryChildBeanList).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Timber.e(error, "There was an error save the categoryChildBeans.");
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(@NonNull Transaction transaction) {
                    }
                })
                .build()
                .execute();
    }


    public Observable<OrderListResponse> setOrders(final OrderListResponse orderListResponse) {
        return Observable.create(new ObservableOnSubscribe<OrderListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<OrderListResponse> e) throws Exception {
                if (e.isDisposed()) return;
//               做保存订单操作
            }
        });
    }

    public void saveUser(String companyName, String userName, String password) {
        UserBean userBean = new UserBean();
        userBean.setId(companyName + userName);
        userBean.setCompanyName(companyName);
        userBean.setPassword(password);
        userBean.setUserName(userName);
//        注意这里是同步操作,但只是操作一个实体，对整体的性能影响不大，批量操作使用异步事务
        userBean.save();
    }

    public Single<List<UserBean>> loadUserList() {
        return RXSQLite.rx(SQLite.select().from(UserBean.class)).queryList();
    }

    public Single<List<ProductBean>> loadProducts() {
        return RXSQLite.rx(SQLite.select().from(ProductBean.class)).queryList();
    }


    public Maybe<ProductBean> loadProduct(int productId) {
        return RXSQLite.rx(SQLite.select().from(ProductBean.class).where(ProductBean_Table.productID.eq(productId)).orderBy(ProductBean_Table.orderBy,true)).querySingle();
    }
    public Single<List<ProductBean>> loadProductsByCategoryParent(String categoryParent) {
        return RXSQLite.rx(SQLite.select().from(ProductBean.class).where(ProductBean_Table.categoryParent.eq(categoryParent)).orderBy(ProductBean_Table.orderBy,true)).queryList();
    }

    public Single<List<CategoryBean>> loadCategorys() {
        return RXSQLite.rx(SQLite.select().from(CategoryBean.class).orderBy(CategoryBean_Table.orderBy,true)).queryList();
    }


    public void deleteUser(UserBean userBean) {
        userBean.delete();
    }


}
