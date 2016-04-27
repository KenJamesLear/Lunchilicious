package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by teddylear on 4/26/2016.
 */
public class MenuAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>{
    private WeakReference<MenuFragment> mWeakRefActivity;
    private SQLiteDatabase mReadOnlyDb;

    public MenuAsyncTask(MenuFragment menuFrag, SQLiteDatabase readOnlyDb) {
        this.mWeakRefActivity = new WeakReference<MenuFragment>(menuFrag);
        this.mReadOnlyDb = readOnlyDb;
    }


    @Override
    protected ArrayList<String> doInBackground(Void...params) {
        String[] projection = {
                CartOrderContract.Product.COLUMN_NAME_NAME
        };
        ArrayList<String> values = new ArrayList<>();
        Cursor c = null;
        try {
            c = mReadOnlyDb.query(
                    CartOrderContract.Product.TABLE_NAME,          // table name
                    projection,                   // The columns to return
                    null,                    // The columns for the WHERE clause
                    null,                    // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );
            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String foodName = c.getString(0);
                values.add(foodName);

            }
            // get data out of cursor, i.e.,
            // firstName = cursor.getString(1);
        } catch (Exception e) {
            values.add("Error Reading DB");
            // handle all exceptions as needed
        } finally {     // this guarantees the cursor is closed.
            if(c != null) {
                c.close();
            }
        }

        return values;
    }

    @Override
    protected void onPostExecute(ArrayList<String> foodNames) {
        super.onPostExecute(foodNames);
        MenuFragment menuFrag = mWeakRefActivity.get();
        if (menuFrag == null)return;
        menuFrag.setMenuItems(foodNames);
        menuFrag = null;
    }

}
