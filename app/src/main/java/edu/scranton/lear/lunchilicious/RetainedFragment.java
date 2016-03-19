package edu.scranton.lear.lunchilicious;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetainedFragment extends Fragment {

    private int mPosition;
    private int[] mQuantityArray;

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
    public void setQuantities(int[] quantities) {
        this.mQuantityArray = quantities;
    }
    public int[] getQuantities() {
        return mQuantityArray;
    }



}
