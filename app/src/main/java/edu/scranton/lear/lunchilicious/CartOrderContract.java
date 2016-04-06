package edu.scranton.lear.lunchilicious;

import android.provider.BaseColumns;

/**
 * Created by teddylear on 3/30/2016.
 */
public final class CartOrderContract {
    public CartOrderContract() {}

    //Product(_ID, name, description, calories, price)
    public static abstract class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CALORIES = "calories";
        public static final String COLUMN_NAME_PRICE = "price";

    }
    //OrderDetails(_ID, purchaseOrderId, lineNo, productName, quantity)
    public static abstract class OrderDetails implements BaseColumns {
        public static final String TABLE_NAME = "orderDetails";
        public static final String COLUMN_NAME_PURCHASEORDERID = "purchaseOrderId";
        public static final String COLUMN_NAME_LINENO = "lineNo";
        public static final String COLUMN_NAME_PRODUCTNAME = "productName";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
    }
    //PurchaseOrder(_ID, orderDate, totalCost)
    public static abstract class PurchaseOrder implements BaseColumns {
        public static final String TABLE_NAME = "purchaseOrder";
        public static final String COLUMN_NAME_ORDERDATE = "orderDate";
        public static final String COLUMN_NAME_TOTALCOST = "totalCost";
    }
}



