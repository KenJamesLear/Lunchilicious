package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuInflater;
import android.view.inputmethod.InputMethodManager;






public class FoodFragment extends Fragment {

    public interface OnAdditemListener {
        void addQuantity(int food,int quantity);
    }
    
    private OnAdditemListener mAddListener;
    private String[] mFoods;
    private String[] mCost;
    private int[] mCalories;
    private String[] mDescription;
    private TextView mTextView;
    private EditText mEditText;
    private Button mReturnButton;
    private int mPosition = 0;
    private int mQuantity;
    public static final String ARG_POSITION ="edu.scranton.lear.lunchilious.ARG_POSITION";

    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell the host activity that this fragment adds items to the appbar
        //setHasOptionsMenu(true);

        // get the position of the selected article. if not set, use 0
        Bundle bundle = getArguments();
        if (bundle != null) mPosition = bundle.getInt(FoodFragment.ARG_POSITION, 0);

        mFoods= getResources().getStringArray(R.array.foodItems);
        mCost = getResources().getStringArray(R.array.Cost);
        mCalories = getResources().getIntArray(R.array.Calories);
        mDescription = getResources().getStringArray(R.array.Description);
        mQuantity = 0;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddListener = (OnAdditemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,  container, false);
        mTextView = (TextView) view.findViewById(R.id.food_display);
        mEditText = (EditText) view.findViewById(R.id.item_quantity);
        mTextView.setTextSize(20);

        updateDisplay(mPosition);


        mReturnButton = (Button) view.findViewById(R.id.return_to_menu);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().compareTo("")!=0)
                    mQuantity = Integer.parseInt(mEditText.getText().toString());
               else mQuantity = 0;
                if (mQuantity < 0 || mQuantity > 100) {
                    mQuantity = 0;
                }
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                mAddListener.addQuantity(mPosition, mQuantity);
            }
        });


        return view;
    }
    public void updateDisplay(int position) {

        mTextView.setText(mFoods[position] + "\n" + "Calories: " + mCalories[position] + "\n" +
                "Cost: $" + mCost[position] + "\n" + "Description:\n" + mDescription[position]);
        mTextView.refreshDrawableState();
    }

    public void setPosition(int position) {this.mPosition = position;}

}
