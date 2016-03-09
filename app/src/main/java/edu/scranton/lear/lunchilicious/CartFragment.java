package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;


public class CartFragment extends Fragment {

    private OnCartCancelListener mCancelListener;
    public static final String ARG_QUANTITIES ="edu.scranton.lear.lunchilious.ARG_QUANTITIES";
    public int[] mFoodQuantites;
    private Button mCancelButton;
    private TextView mQuanTextView;
    private TextView mTotalTextView;
    private String[] mFoods;
    private String[] mCost;

    public CartFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell the host activity that this fragment adds items to the appbar
        setHasOptionsMenu(true);

        // get the position of the selected article. if not set, use 0

        Bundle bundle = getArguments();
        if (bundle != null) mFoodQuantites = bundle.getIntArray(CartFragment.ARG_QUANTITIES);
        else mFoodQuantites= getResources().getIntArray(R.array.quantityOfItems);

        mFoods= getResources().getStringArray(R.array.foodItems);
        mCost = getResources().getStringArray(R.array.Cost);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCancelListener = (OnCartCancelListener) context;

    }

    public interface OnCartCancelListener {
        void onCancelCart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,  container, false);
        mQuanTextView = (TextView) view.findViewById(R.id.food_display);
        mQuanTextView.setTextSize(20);
        mTotalTextView = (TextView) view.findViewById(R.id.total_display);
        updateDisplay(mFoodQuantites);

        mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelListener.onCancelCart();
            }
        });
        return view;
    }

    public void updateDisplay(int[] quantities){
        String output = "Welcome to the Cart: \n";
        for (int i = 0;i<6;i++)
        {
            if (quantities[i]>0) {
                if (i != 5) output += mFoods[i] + ": " + quantities[i] + "\n";
                else output += mFoods[i] + ": " + quantities[i];
            }
        }
        mQuanTextView.setText(output);
        mQuanTextView.refreshDrawableState();
        double total = updateTotal(quantities);
        DecimalFormat money;
        money = new DecimalFormat("$0.00");
        mTotalTextView.setText("Total: " + money.format(total));
    }

    public double updateTotal(int[] quantities){
        double total = 0;
        for (int i = 0;i<6;i++)
        {
            if (quantities[i]>0)
                total += Double.parseDouble(mCost[i]) *  quantities[i];
        }
        return total;
    }

}
