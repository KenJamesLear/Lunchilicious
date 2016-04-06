package edu.scranton.lear.lunchilicious;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by teddylear on 3/30/2016.
 */
public class CartOrderDbOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "cartorder.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA = ", ";

    // compose the CREATE statement for purchase order table
    //Product(_ID, name, description, calories, price)
    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + CartOrderContract.Product.TABLE_NAME + "(" +
                CartOrderContract.Product._ID + " INTEGER PRIMARY KEY, " +
                CartOrderContract.Product.COLUMN_NAME_NAME + TEXT_TYPE + COMMA +
                CartOrderContract.Product.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA +
                CartOrderContract.Product.COLUMN_NAME_CALORIES + INTEGER_TYPE + COMMA +
                CartOrderContract.Product.COLUMN_NAME_PRICE + REAL_TYPE +
                ")";

    // compose the CREATE statement for product table
    //OrderDetails(_ID, purchaseOrderId, lineNo, productName, quantity)
    private static final String SQL_CREATE_ORDERDETAILS =
            "CREATE TABLE " + CartOrderContract.OrderDetails.TABLE_NAME + "(" +
                    CartOrderContract.OrderDetails._ID + " INTEGER PRIMARY KEY , " +
                    CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID + INTEGER_TYPE + COMMA +
                    CartOrderContract.OrderDetails.COLUMN_NAME_LINENO + INTEGER_TYPE + COMMA +
                    CartOrderContract.OrderDetails.COLUMN_NAME_PRODUCTNAME + TEXT_TYPE + COMMA +
                    CartOrderContract.OrderDetails.COLUMN_NAME_QUANTITY + INTEGER_TYPE +
                    ")";

    // compose the CREATE statement for product table
    //PurchaseOrder(_ID, orderDate, totalCost)
    private static final String SQL_CREATE_PURCHASEORDER =
            "CREATE TABLE " + CartOrderContract.PurchaseOrder.TABLE_NAME + "(" +
                    CartOrderContract.PurchaseOrder._ID + " INTEGER PRIMARY KEY , " +
                    CartOrderContract.PurchaseOrder.COLUMN_NAME_ORDERDATE + TEXT_TYPE + COMMA +
                    CartOrderContract.PurchaseOrder.COLUMN_NAME_TOTALCOST + REAL_TYPE +
                    ")";

    // compose the drop statements for tables
    private static final String SQL_DROP_PRODUCT =
            "DROP TABLE IF EXISTS " + CartOrderContract.Product.TABLE_NAME;
    private static final String SQL_DROP_ORDERDETAILS =
            "DROP TABLE IF EXISTS " + CartOrderContract.OrderDetails.TABLE_NAME;
    private static final String SQL_DROP_PURCHASEORDER =
            "DROP TABLE IF EXISTS " + CartOrderContract.PurchaseOrder.TABLE_NAME;

    public CartOrderDbOpenHelper(Context context) {
        // call the super constructor which will call the callbacks
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // create all tables
        db.execSQL(SQL_CREATE_PURCHASEORDER);
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_ORDERDETAILS);


        // initialize the database
        initialize(db);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // we simply drop all the tables and create them

        db.execSQL(SQL_DROP_PURCHASEORDER);
        db.execSQL(SQL_DROP_PRODUCT);
        db.execSQL(SQL_DROP_ORDERDETAILS);

        onCreate(db);
    }

    private void initialize(SQLiteDatabase db) {
        // you can initialize the database
        // e.g., insert rows into tables
        //Product(_ID, name, description, calories, price)
        //TODO CHECK IF THIS WORKS
        ContentValues product = new ContentValues();

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "Pizza");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "Includes Pepperoni!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 7);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 5.00);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "Sandwich");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "Extra Bacon!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 8);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 10.00);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "Pasta");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "Topped with Sauce!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 9);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 9.45);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "CheeseBurger");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "Fresh Bread!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 10);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 8.50);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "Bottled Coke");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "New Flavor of Coke!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 11);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 2.15);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

        product.put(CartOrderContract.Product.COLUMN_NAME_NAME, "Bottled Water");
        product.put(CartOrderContract.Product.COLUMN_NAME_DESCRIPTION, "Wet!");
        product.put(CartOrderContract.Product.COLUMN_NAME_CALORIES, 12);
        product.put(CartOrderContract.Product.COLUMN_NAME_PRICE, 1.75);
        db.insert(CartOrderContract.Product.TABLE_NAME, null, product);

    }
}
