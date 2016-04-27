package edu.scranton.lear.lunchilicious;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ConfirmationAsyncTask extends AsyncTask<Void, Void, ConfirmationTaskHelper> {
    private WeakReference<ConfirmationFragment> mWeakRefActivity;
    private SQLiteDatabase mReadOnlyDb;
    private long mPurchaseOrderId;

    public ConfirmationAsyncTask(ConfirmationFragment confirmFrag, SQLiteDatabase readOnlyDb, long purchaseOrderId) {
        this.mWeakRefActivity = new WeakReference<> (confirmFrag);
        this.mReadOnlyDb = readOnlyDb;
        this.mPurchaseOrderId = purchaseOrderId;
    }


    @Override
    protected ConfirmationTaskHelper doInBackground(Void...params) {

        ArrayList<String> mFoods = new ArrayList<>();
        ArrayList<Integer> mQuantity = new ArrayList<>();
        ArrayList<Double> mTotals = new ArrayList<>();
        ConfirmationTaskHelper confirmationTaskHelper = new ConfirmationTaskHelper();

        String[] projection = {
                CartOrderContract.OrderDetails.COLUMN_NAME_PRODUCTNAME,
                CartOrderContract.OrderDetails.COLUMN_NAME_QUANTITY
        };
        String selection = CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID + " =?";
        String[] selectionArgs = { mPurchaseOrderId + "" };

        Cursor c = null;
        try {
            c = mReadOnlyDb.query(
                    CartOrderContract.OrderDetails.TABLE_NAME,          // table name
                    projection,                   // The columns to return
                    selection,                    // The columns for the WHERE clause
                    selectionArgs,                // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String foodName = c.getString(0);
                int foodQuantity = c.getInt(1);
                mFoods.add(foodName);
                //int foodQuantity = c.getInt(0);
                mQuantity.add(foodQuantity);
            }
            // get data out of cursor, i.e.,
            // firstName = cursor.getString(1);
        } catch (Exception e) {

            // handle all exceptions as needed
        } finally {     // this guarantees the cursor is closed.
            if(c != null) {
                c.close();
            }
        }

        double total;
        //ArrayList<Integer> mTest = new ArrayList<>();


        String[] projectionTwo = {
                //CartOrderContract.PurchaseOrder._ID
                CartOrderContract.PurchaseOrder.COLUMN_NAME_TOTALCOST
        };
        String selectionTwo = CartOrderContract.PurchaseOrder._ID + "=?";
        String[] selectionArgsTwo = { mPurchaseOrderId + "" };

        c = null;
        try {
            c = mReadOnlyDb.query(
                    CartOrderContract.PurchaseOrder.TABLE_NAME,          // table name
                    projectionTwo,                   // The columns to return
                    selectionTwo,                    // The columns for the WHERE clause
                    selectionArgsTwo,                    // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );
            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                total = c.getDouble(0);
                mTotals.add(total);
                //int test = c.getInt(0);
                //mTest.add(test);

            }

            // get data out of cursor, i.e.,
            // firstName = cursor.getString(1);
        } catch (Exception e) {

            // handle all exceptions as needed
        } finally {     // this guarantees the cursor is closed.
            if(c != null) {
                c.close();
            }
        }

        confirmationTaskHelper.setFoods(mFoods);
        confirmationTaskHelper.setQuantities(mQuantity);
        confirmationTaskHelper.setTotals(mTotals);


        return confirmationTaskHelper;
    }

    @Override
    protected void onPostExecute(ConfirmationTaskHelper confirmationTaskHelper) {
        super.onPostExecute(confirmationTaskHelper);
        ConfirmationFragment confirmFrag = mWeakRefActivity.get();
        if (confirmFrag == null)return;
        confirmFrag.setConfirmItems(confirmationTaskHelper);
    }

}

