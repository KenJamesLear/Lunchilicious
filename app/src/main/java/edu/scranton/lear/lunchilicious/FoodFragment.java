package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.DecimalFormat;
import java.util.ArrayList;


public class FoodFragment extends Fragment {

    public interface OnAdditemListener {
        void addQuantity(int food,int quantity);
    }

    public interface DbProvider {
        SQLiteDatabase getmReadOnlyDb();
        SQLiteDatabase getmWritableDb();
    }
    
    private OnAdditemListener mAddListener;
    private ArrayList<String> mFoods;
    private  ArrayList<Double> mCost;
    private  ArrayList<Integer> mCalories;
    private  ArrayList<String> mDescription;
    private TextView mTextView;
    private EditText mEditText;
    private Button mReturnButton;
    private int mPosition = 0;
    private int mQuantity;
    public static final String ARG_POSITION ="edu.scranton.lear.lunchilious.ARG_POSITION";
    private SQLiteDatabase mReadOnlyDb;
    private DbProvider provider;

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

        mQuantity = 0;
        mFoods = new ArrayList<>();
        mDescription = new ArrayList<>();
        mCost = new ArrayList<>();
        mCalories = new ArrayList<>();


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
        provider = (DbProvider) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,  container, false);
        mTextView = (TextView) view.findViewById(R.id.food_display);
        mEditText = (EditText) view.findViewById(R.id.item_quantity);
        mTextView.setTextSize(20);

        mReadOnlyDb = provider.getmReadOnlyDb();


        String[] projection = {
                CartOrderContract.Product.COLUMN_NAME_NAME,
                CartOrderContract.Product.COLUMN_NAME_DESCRIPTION,
                CartOrderContract.Product.COLUMN_NAME_PRICE,
                CartOrderContract.Product.COLUMN_NAME_CALORIES
        };
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
                String foodDescription = c.getString(1);
                double foodPrice = c.getDouble(2);
                int foodCalories = c.getInt(3);
                mFoods.add(foodName);
                mDescription.add(foodDescription);
                mCost.add(foodPrice);
                mCalories.add(foodCalories);
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
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                mAddListener.addQuantity(mPosition, mQuantity);
            }
        });

        return view;
    }

    public void updateDisplay(int position) {
        mPosition = position;
        DecimalFormat money = new DecimalFormat("$0.00");
        mTextView.setText(mFoods.get(position) + "\n" + "Calories: " + mCalories.get(position) +
                "\n" + "Cost: " + money.format(mCost.get(position)) + "\n" + "Description:\n"
                + mDescription.get(position));
        mTextView.refreshDrawableState();

    }

    public void setPosition(int position) {this.mPosition = position;}

}
