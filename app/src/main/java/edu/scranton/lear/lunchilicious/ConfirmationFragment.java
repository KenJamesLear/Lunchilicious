package edu.scranton.lear.lunchilicious;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationFragment extends Fragment implements View.OnClickListener {

    public interface DbProvider {
        SQLiteDatabase getmReadOnlyDb();
        SQLiteDatabase getmWritableDb();
    }

    public interface OnOkListener {
        void onOkClicked();
    }

    private SQLiteDatabase mReadOnlyDb;
    private DbProvider provider;
    public static final String ARG_PURCHASEORDERID ="edu.scranton.lear.lunchilious.ARG_PURCHASEORDERID";
    private long mPurchaseOrderId;
    private ArrayList<String> mFoods;
    private ArrayList<Double> mTotals;
    private ArrayList<Integer> mQuantity;
    private LinearLayout mLinearContainer;
    private ArrayList<TextView> mTextViews;
    private ArrayList<Integer> mViewIds;
    private ArrayList<Button> mButtons;
    private ArrayList<Integer> mButtonIds;
    private OnOkListener mOnOkListener;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = (DbProvider) getActivity();
        mOnOkListener = (OnOkListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) mPurchaseOrderId = bundle.getLong(ConfirmationFragment.ARG_PURCHASEORDERID, 0);
        else mPurchaseOrderId = -1;
        mFoods= new ArrayList<>();
        mTotals = new ArrayList<>();
        mQuantity = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mViewIds = new ArrayList<>();
        mButtons = new ArrayList<>();
        mButtonIds = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReadOnlyDb = provider.getmReadOnlyDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        mLinearContainer = (LinearLayout) view.findViewById(R.id.my_linearlayout_confirmation);

       mReadOnlyDb = provider.getmReadOnlyDb();
        //TextView confirmationItemDisplay = (TextView) view.findViewById(R.id.item_display);
        //TextView confirmationTotalDisplay = (TextView) view.findViewById(R.id.cost_display);


        //TODO CHANGE THIS
        String[] projection = {
                //CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID
                CartOrderContract.OrderDetails.COLUMN_NAME_PRODUCTNAME,
                CartOrderContract.OrderDetails.COLUMN_NAME_QUANTITY
        };
        String selection = CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID + " =?";
        String[] selectionArgs = { mPurchaseOrderId + "" };

        Cursor c = null;
        try {
            c = mReadOnlyDb.query(
                    CartOrderContract.OrderDetails.TABLE_NAME,          // table name
                    projection,                   // The columns to return
                    selection,                    // The columns for the WHERE clause
                    selectionArgs,                // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String foodName = c.getString(0);
                int foodQuantity = c.getInt(1);
                mFoods.add(foodName);
                //int foodQuantity = c.getInt(0);
                mQuantity.add(foodQuantity);
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


        /*String output = "Welcome to the Cart: \n";
        for (int i = 0;i<mQuantity.size();i++)
        {
            //if (mQuantity.get(i)>0) {
                output += mFoods.get(i) + ": " + mQuantity.get(i) + "\n";
            //}
        }
        confirmationItemDisplay.setText( output );*/

        double total;
        //ArrayList<Integer> mTest = new ArrayList<>();

        //TODO CHANGE THIS
        String[] projectionTwo = {
                //CartOrderContract.PurchaseOrder._ID
                CartOrderContract.PurchaseOrder.COLUMN_NAME_TOTALCOST
        };
        String selectionTwo = CartOrderContract.PurchaseOrder._ID + "=?";
        String[] selectionArgsTwo = { mPurchaseOrderId + "" };

        c = null;
        try {
            c = mReadOnlyDb.query(
                    CartOrderContract.PurchaseOrder.TABLE_NAME,          // table name
                    projectionTwo,                   // The columns to return
                    selectionTwo,                    // The columns for the WHERE clause
                    selectionArgsTwo,                    // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );
            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                total = c.getDouble(0);
                mTotals.add(total);
                //int test = c.getInt(0);
                //mTest.add(test);

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

        /*DecimalFormat money;
        money = new DecimalFormat("$0.00");
        String confirmTotal = "";
        /*for (int i=0;i<mTest.size();i++){
            confirmTotal += mTest.get(i) + "\n";
        }

        for (int i = 0;i<mTotals.size();i++)
        {
            confirmTotal += money.format(mTotals.get(i));
        }
        confirmationTotalDisplay.setText("Total: " + confirmTotal);*/
        inflateLinearContainer();
        return view;

    }

    private void inflateLinearContainer() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView cartIntro = new TextView(getActivity());
        TextView cartTotal = new TextView(getActivity());

        int id = View.generateViewId();
        cartIntro.setId(id);
        // keep a reference to the TextView
        mTextViews.add(cartIntro);
        // keep the id  of the TextView
        mViewIds.add(id);
        String output = "Welcome to the Cart:";
        cartIntro.setText(output);
        cartIntro.setFreezesText(true);
        cartIntro.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cartIntro.setLayoutParams(params);
        mLinearContainer.addView(cartIntro);



        TextView cartDisplay;
        for (int i = 0;i<mQuantity.size();i++)
        {
            if (mQuantity.get(i)>0) {
                cartDisplay = new TextView(getActivity());
                id = View.generateViewId();
                cartDisplay.setId(id);
                mTextViews.add(cartDisplay);
                mViewIds.add(id);
                cartDisplay.setText(mFoods.get(i) + ":" + mQuantity.get(i));
                cartDisplay.setFreezesText(true);
                cartDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                cartDisplay.setLayoutParams(params);
                mLinearContainer.addView(cartDisplay);
                /*if (i != 5) output += mFoods.get(i) + ": " + mFoodQuantities[i] + "\n";
                else output += mFoods.get(i) + ": " + mFoodQuantities[i];*/
            }
        }
        //cartDisplay.setText(output);

        //total display
        id = View.generateViewId();
        cartTotal.setId(id);
        mTextViews.add(cartTotal);
        mViewIds.add(id);
        cartTotal.setFreezesText(true);
        cartTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        DecimalFormat money;
        money = new DecimalFormat("$0.00");
        String confirmTotal = "";
        for (int i = 0;i<mTotals.size();i++)
        {
            confirmTotal += money.format(mTotals.get(i));
        }
        cartTotal.setText("Total: " + confirmTotal);
        cartTotal.setLayoutParams(params);
        mLinearContainer.addView(cartTotal);


        // -- add a button to the linearyLayout container
        Button button  = new Button(getActivity());
        button.setText("OK");
        button.setOnClickListener(this);

        id = View.generateViewId();
        button.setId(id);
        mButtons.add(button);
        mButtonIds.add(id);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(buttonParams);

        mLinearContainer.addView(button);

    }

    public void onClick(View v) {
        mOnOkListener.onOkClicked();
    }
}
