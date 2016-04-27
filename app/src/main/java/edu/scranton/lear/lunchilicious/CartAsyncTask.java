package edu.scranton.lear.lunchilicious;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CartAsyncTask extends AsyncTask<Void, Void, CartTaskHelper> {
    private WeakReference<MainActivity> mWeakRefActivity;
    private SQLiteDatabase mWritableDb;
    private CartTaskHelper mCartTaskHelper;

    public CartAsyncTask(MainActivity mainActivity, SQLiteDatabase writableDb, CartTaskHelper cartTaskHelper) {
        this.mWeakRefActivity = new WeakReference<>(mainActivity);
        this.mWritableDb = writableDb;
        this.mCartTaskHelper = cartTaskHelper;
    }


    @Override
    protected CartTaskHelper doInBackground(Void...params) {
        CartTaskHelper cartTaskHelper = mCartTaskHelper;
        double mTotal = cartTaskHelper.getTotal();
        ArrayList<Integer> mFoodQuantities = cartTaskHelper.getQuantities();
        ArrayList<String> mFoods = cartTaskHelper.getFoods();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        ContentValues purchaseOrder = new ContentValues();
        purchaseOrder.put(CartOrderContract.PurchaseOrder.COLUMN_NAME_ORDERDATE, timeStamp);
        purchaseOrder.put(CartOrderContract.PurchaseOrder.COLUMN_NAME_TOTALCOST, mTotal);
        long purchaseOrderId = mWritableDb.insert(CartOrderContract.PurchaseOrder.TABLE_NAME,null,purchaseOrder);
        cartTaskHelper.setPurchaseOrderId(purchaseOrderId);

        //orderDetails
        int lineNo = 1;
        int amountOfItems = mFoodQuantities.size();
        for (int i = 0;i<amountOfItems;i++){
            if (mFoodQuantities.get(i) > 0){
                ContentValues orderDetails = new ContentValues();
                orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID,purchaseOrderId);
                orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_LINENO,lineNo);
                lineNo++;
                orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_PRODUCTNAME, mFoods.get(i));
                orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_QUANTITY, mFoodQuantities.get(i));
                mWritableDb.insert(CartOrderContract.OrderDetails.TABLE_NAME,null,orderDetails);
            }
        }

        return cartTaskHelper;
    }

    @Override
    protected void onPostExecute(CartTaskHelper cartTaskHelper) {
        super.onPostExecute(cartTaskHelper);
        MainActivity mainActivity = mWeakRefActivity.get();
        if (mainActivity == null)return;
        mainActivity.createConfirmationFrag(cartTaskHelper.getPurchaseOrderId());
    }

}
