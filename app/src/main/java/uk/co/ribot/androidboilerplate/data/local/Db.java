package uk.co.ribot.androidboilerplate.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import uk.co.ribot.androidboilerplate.data.model.database.Name;
import uk.co.ribot.androidboilerplate.data.model.database.Profile;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;

public class Db {

    public Db() { }

    public abstract static class RibotProfileTable {
        public static final String TABLE_NAME = "ribot_profile";

        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_HEX_COLOR = "hex_color";
        public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_BIO = "bio";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                        COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                        COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                        COLUMN_HEX_COLOR + " TEXT NOT NULL, " +
                        COLUMN_DATE_OF_BIRTH + " INTEGER NOT NULL, " +
                        COLUMN_AVATAR + " TEXT, " +
                        COLUMN_BIO + " TEXT" +
                " ); ";

        public static ContentValues toContentValues(Profile profile) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, profile.email());
            values.put(COLUMN_FIRST_NAME, profile.name().first());
            values.put(COLUMN_LAST_NAME, profile.name().last());
            values.put(COLUMN_HEX_COLOR, profile.hexColor());
            values.put(COLUMN_DATE_OF_BIRTH, profile.dateOfBirth().getTime());
            values.put(COLUMN_AVATAR, profile.avatar());
            if (profile.bio() != null) values.put(COLUMN_BIO, profile.bio());
            return values;
        }

        public static Profile parseCursor(Cursor cursor) {
            Name name = Name.create(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
            long dobTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH));

            return Profile.builder()
                    .setName(name)
                    .setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)))
                    .setHexColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HEX_COLOR)))
                    .setDateOfBirth(new Date(dobTime))
                    .setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVATAR)))
                    .setBio(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO)))
                    .build();
        }
    }

    public abstract static class ProductProfileTable{
        public static final String TABLE_NAME = "product_profile";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IS_TWOUNIT = "isTwoUnit";
        public static final String COLUMN_SETTLE_PRICE = "settlePrice";

        public static final String COLUMN_UOM = "uom";
        public static final String COLUMN_SETTLEUOMID = "settleUomId";
        public static final String COLUMN_PRICE = "price";

        public static final String COLUMN_BARCODE = "barcode";
        public static final String COLUMN_DEFAULTCODE = "defaultCode";
        public static final String COLUMN_STOCKTYPE = "stockType";

        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_PRODUCTUOM = "productUom";

        public static final String COLUMN_PRODUCTID= "productID";
        public static final String COLUMN_TRACKING = "tracking";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_PRODUCTID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_IS_TWOUNIT + " INTEGER NOT NULL, " +
                        COLUMN_SETTLE_PRICE + " FLOAT NOT NULL, " +
                        COLUMN_UOM + " TEXT NOT NULL, " +
                        COLUMN_SETTLEUOMID + " TEXT NOT NULL, " +
                        COLUMN_PRICE + " DOUBLE, " +
                        COLUMN_BARCODE + " TEXT, " +
                        COLUMN_DEFAULTCODE + " TEXT, " +
                        COLUMN_STOCKTYPE + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_UNIT + " TEXT, " +
                        COLUMN_PRODUCTUOM + " TEXT, " +
                        COLUMN_TRACKING + " TEXT" +
                        " ); ";
        public static ContentValues toContentValues(ProductListResponse.Product product) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_PRODUCTID, product.getProductID());
            values.put(COLUMN_NAME, product.getName());
            if (product.isTwoUnit()){
                values.put(COLUMN_IS_TWOUNIT, 1);
            }else{
                values.put(COLUMN_IS_TWOUNIT, 0);
            }

            values.put(COLUMN_SETTLE_PRICE, product.getSettlePrice());
            values.put(COLUMN_UOM, product.getUom());
            values.put(COLUMN_SETTLEUOMID, product.getSettleUomId());

            values.put(COLUMN_PRICE, product.getPrice());
            values.put(COLUMN_BARCODE, product.getBarcode());
            values.put(COLUMN_DEFAULTCODE, product.getDefaultCode());

            values.put(COLUMN_STOCKTYPE, product.getStockType());
            values.put(COLUMN_CATEGORY, product.getCategory());
            values.put(COLUMN_UNIT, product.getUnit());

            values.put(COLUMN_PRODUCTUOM, product.getProductUom());
            values.put(COLUMN_TRACKING, product.getTracking());

            return values;
        }
        public static ProductListResponse.Product parseCursor(Cursor cursor) {
            ProductListResponse.Product product = new ProductListResponse.Product();
            product.setProductID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCTID)));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            product.setTwoUnit(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_TWOUNIT))==1);

            product.setSettlePrice(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_SETTLE_PRICE)));
            product.setUom(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UOM)));
            product.setSettleUomId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SETTLEUOMID)));

            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            product.setBarcode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARCODE)));
            product.setDefaultCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEFAULTCODE)));

            product.setStockType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCKTYPE)));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            product.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)));

            product.setProductUom(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCTUOM)));
            product.setTracking(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACKING)));

            return product;
        }
    }

}
