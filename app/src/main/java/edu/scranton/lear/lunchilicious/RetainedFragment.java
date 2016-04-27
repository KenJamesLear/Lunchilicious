package edu.scranton.lear.lunchilicious;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetainedFragment extends Fragment {

    private int mPosition;
    private ArrayList<Integer> mQuantityArray;
    private SQLiteDatabase mReadOnlyDb;
    private SQLiteDatabase mWritableDb;

    public RetainedFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the instance retained
        setRetainInstance(true);
    }

    public void setPosition(int position) {this.mPosition = position;}
    public int getPosition() {return mPosition;}
    public void setQuantities(ArrayList<Integer> quantities) {
        this.mQuantityArray = quantities;
    }
    public ArrayList<Integer> getQuantities() {
        return mQuantityArray;
    }
    public void setReadOnlyDb(SQLiteDatabase ReadOnlyDb){this.mReadOnlyDb = ReadOnlyDb;}
    public SQLiteDatabase getReadOnlyDb() {return mReadOnlyDb;}
    public void setWritableDb(SQLiteDatabase WritableDb){this.mWritableDb = WritableDb;}
    public SQLiteDatabase getWritableDb() {return mWritableDb;}





}
