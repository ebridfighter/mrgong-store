package uk.co.ribot.androidboilerplate.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.model.database.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;

import static uk.co.ribot.androidboilerplate.data.local.Db.ProductProfileTable.COLUMN_PRODUCTID;

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
        return Observable.create(new Observable.OnSubscribe<Ribot>() {
            @Override
            public void call(Subscriber<? super Ribot> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.RibotProfileTable.TABLE_NAME, null);
                    for (Ribot ribot : newRibots) {
                        long result = mDb.insert(Db.RibotProfileTable.TABLE_NAME,
                                Db.RibotProfileTable.toContentValues(ribot.profile()),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(ribot);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Ribot>> getRibots() {
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
        return Observable.create(new Observable.OnSubscribe<ProductListResponse>() {
            @Override
            public void call(Subscriber<? super ProductListResponse> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.ProductProfileTable.TABLE_NAME, null);
                    for (ProductListResponse.Product product : productListResponse.getList()) {
                        long result = mDb.insert(Db.ProductProfileTable.TABLE_NAME,
                                Db.ProductProfileTable.toContentValues(product),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(productListResponse);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<OrderListResponse> setOrders(final OrderListResponse orderListResponse) {
        return Observable.create(new Observable.OnSubscribe<OrderListResponse>() {
            @Override
            public void call(Subscriber<? super OrderListResponse> subscriber) {
                if (subscriber.isUnsubscribed()) return;
//               做保存订单操作
            }
        });
    }

    public Observable<List<ProductListResponse.Product>> getProducts() {
        return mDb.createQuery(Db.ProductProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.ProductProfileTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, ProductListResponse.Product>() {
                    @Override
                    public ProductListResponse.Product call(Cursor cursor) {
                        return Db.ProductProfileTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<ProductListResponse.Product> getProduct(int productId) {
        return mDb.createQuery(Db.ProductProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.ProductProfileTable.TABLE_NAME,COLUMN_PRODUCTID+"=?",String.valueOf(productId))
                .mapToList(new Func1<Cursor, ProductListResponse.Product>() {
                    @Override
                    public ProductListResponse.Product call(Cursor cursor) {
                        return Db.ProductProfileTable.parseCursor(cursor);
                    }
                }).map(new Func1<List<ProductListResponse.Product>, ProductListResponse.Product>() {
                    @Override
                    public ProductListResponse.Product call(List<ProductListResponse.Product> products) {
                        if (products.size() > 0){
                            return products.get(0);
                        }
                        return null;
                    }
                });
    }

}
