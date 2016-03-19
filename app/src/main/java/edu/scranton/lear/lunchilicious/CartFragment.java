package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class CartFragment extends Fragment {

    /*public interface OnCartCancelListener {
        void onCancelCart();
    }*/

    //private OnCartCancelListener mCancelListener;
    public static final String ARG_QUANTITIES ="edu.scranton.lear.lunchilious.ARG_QUANTITIES";
    public int[] mFoodQuantites;
    private Button mCancelButton;
    private TextView mQuanTextView;
    private TextView mTotalTextView;
    private String[] mFoods;
    private String[] mCost;
    LinearLayout mLinearContainer;
    ArrayList<TextView> mTextViews;
    ArrayList<Integer> mViewIds;



    public CartFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell the host activity that this fragment adds items to the appbar
        //setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if (bundle != null) mFoodQuantites = bundle.getIntArray(CartFragment.ARG_QUANTITIES);
        else mFoodQuantites= getResources().getIntArray(R.array.quantityOfItems);

        mFoods= getResources().getStringArray(R.array.foodItems);
        mCost = getResources().getStringArray(R.array.Cost);

        mTextViews = new ArrayList<>();
        mViewIds = new ArrayList<>();

        // get the linearLayout container
    }

    private void inflateLinearContainer() {

        TextView cartDisplay = new TextView(getActivity());
        TextView cartTotal = new TextView(getActivity());

        int id = View.generateViewId();
        cartDisplay.setId(id);
        // keep a reference to the TextView
        mTextViews.add(cartDisplay);
        // keep the id  of the TextView
        mViewIds.add(id);

        id = View.generateViewId();
        cartTotal.setId(id);
        mTextViews.add(cartTotal);
        mViewIds.add(id);

        // you can further manipulate the attributes of the TextView
        cartDisplay.setFreezesText(true);
        cartDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cartTotal.setFreezesText(true);
        cartTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        //\cartTotal.align;

        String output = "Welcome to the Cart: \n";
        for (int i = 0;i<6;i++)
        {
            if (mFoodQuantites[i]>0) {
                if (i != 5) output += mFoods[i] + ": " + mFoodQuantites[i] + "\n";
                else output += mFoods[i] + ": " + mFoodQuantites[i];
            }
        }
        cartDisplay.setText(output);

        double total = updateTotal(mFoodQuantites);
        DecimalFormat money;
        money = new DecimalFormat("$0.00");
        cartTotal.setText("Total: " + money.format(total));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cartDisplay.setLayoutParams(params);
        cartTotal.setLayoutParams(params);


        // add the new row to the linearlayout container
        mLinearContainer.addView(cartDisplay);
        mLinearContainer.addView(cartTotal);



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mCancelListener = (OnCartCancelListener) context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,  container, false);
        mLinearContainer = (LinearLayout) view.findViewById(R.id.my_linearlayout_cart);
        //mQuanTextView = (TextView) view.findViewById(R.id.food_display);
        //mQuanTextView.setTextSize(20);
        //mTotalTextView = (TextView) view.findViewById(R.id.total_display);
        //updateDisplay(mFoodQuantites, view);

        /*mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelListener.onCancelCart();
            }
        });*/
        inflateLinearContainer();
        return view;

    }

    public void updateDisplay(int[] quantities, View v){
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
