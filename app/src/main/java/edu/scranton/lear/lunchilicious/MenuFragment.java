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

import java.security.KeyStore;
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
    private MenuAsyncTask mMenuAsyncTask;

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
        MenuAsyncTask ma = new MenuAsyncTask(this,mReadOnlyDb);
        mMenuAsyncTask = ma;
        ma.execute();
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
        if (mMenuAsyncTask != null) mMenuAsyncTask.cancel(true);
        super.onDestroy();
    }

    public void setMenuItems(ArrayList<String> values){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

}
