package edu.scranton.lear.lunchilicious;

import android.app.Activity;
import android.content.Context;
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


public class MenuFragment extends ListFragment {

    public interface OnMenuListener {
        void onItemSelected(int position);
    }

    private OnMenuListener mListener;
    private Activity mActivity = null;



    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = getResources().getStringArray(R.array.foodItems);
        //String[] values = new String[] { "Pizza", "Sandwich", "Cheeseburger",
        //        "Pasta", "Bottled Coke", "Bottled Water"};
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

}
