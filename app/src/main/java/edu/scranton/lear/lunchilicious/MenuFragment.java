package edu.scranton.lear.lunchilicious;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MenuFragment extends ListFragment {

    public interface OnMenuListener {
        void onItemSelected(int position);
    }

    public interface DbProvider {
        SQLiteDatabase getmReadOnlyDb();
    }

    private OnMenuListener mListener;
    private Activity mActivity = null;
    private SQLiteDatabase mReadOnlyDb;
    private DbProvider provider;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
        provider = (DbProvider) getActivity();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReadOnlyDb = provider.getmReadOnlyDb();

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Context context = mActivity.getApplicationContext();
        try {
            mListener = (OnMenuListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        mListener.onItemSelected(position);
    }

    public MenuFragment() {
        // Required empty public constructor
    }

    public void onDestroy() {
        mReadOnlyDb = null;
        super.onDestroy();
    }

}
