package edu.scranton.lear.lunchilicious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CartFragment extends Fragment implements View.OnClickListener{

    public interface OnConfirmationListener {
        void onConfirmation(long purchaseOrderId);
    }

    public interface DbProvider {
        SQLiteDatabase getmReadOnlyDb();
        SQLiteDatabase getmWritableDb();
    }

    //private OnCartCancelListener mCancelListener;
    public static final String ARG_QUANTITIES ="edu.scranton.lear.lunchilious.ARG_QUANTITIES";
    public ArrayList<Integer> mFoodQuantities;
    private ArrayList<String> mFoods;
    private ArrayList<Double> mCost;
    private LinearLayout mLinearContainer;//changed to private
    private ArrayList<TextView> mTextViews;
    private ArrayList<Integer> mViewIds;
    private ArrayList<Button> mButtons;
    private ArrayList<Integer> mButtonIds;
    private SQLiteDatabase mReadOnlyDb;
    private SQLiteDatabase mWritableDb;
    private DbProvider provider;
    private OnConfirmationListener mConfirmationListener;
    private double mTotal;


    public CartFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell the host activity that this fragment adds items to the appbar
        //setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if (bundle != null) mFoodQuantities = bundle.getIntegerArrayList(CartFragment.ARG_QUANTITIES);
        else mFoodQuantities = new ArrayList<>();

        mFoods= new ArrayList<>();
        mCost = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mViewIds = new ArrayList<>();
        mButtons = new ArrayList<>();
        mButtonIds = new ArrayList<>();

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
        for (int i = 0;i<mFoodQuantities.size();i++)
        {
            if (mFoodQuantities.get(i)>0) {
                cartDisplay = new TextView(getActivity());
                id = View.generateViewId();
                cartDisplay.setId(id);
                mTextViews.add(cartDisplay);
                mViewIds.add(id);
                cartDisplay.setText(mFoods.get(i) + ":" + mFoodQuantities.get(i));
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
        if (mFoodQuantities.size() > 0){
            mTotal = updateTotal(mFoodQuantities);
        }
        else
            mTotal = 0;
        DecimalFormat money;
        money = new DecimalFormat("$0.00");
        cartTotal.setText("Total: " + money.format(mTotal));
        cartTotal.setLayoutParams(params);
        mLinearContainer.addView(cartTotal);


        // -- add a button to the linearyLayout container
        Button button  = new Button(getActivity());
        button.setText("Check Out");
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


    /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu);
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mCancelListener = (OnCartCancelListener) context;
        provider = (DbProvider) getActivity();
        mConfirmationListener = (OnConfirmationListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,  container, false);
        mLinearContainer = (LinearLayout) view.findViewById(R.id.my_linearlayout_cart);
        mReadOnlyDb = provider.getmReadOnlyDb();
        mWritableDb = provider.getmWritableDb();

        String[] projection = {
                CartOrderContract.Product.COLUMN_NAME_NAME,
                CartOrderContract.Product.COLUMN_NAME_PRICE
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
                double foodPrice = c.getDouble(1);
                mFoods.add(foodName);
                mCost.add(foodPrice);
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

        inflateLinearContainer();
        return view;

    }

    public double updateTotal(ArrayList<Integer> quantities){
        double total = 0;
        for (int i = 0;i<6;i++)
        {
            if (quantities.get(i)>0)
                total += mCost.get(i) *  quantities.get(i);
        }
        return total;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mButtonIds.get(0)){
            //purchase order
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            ContentValues purchaseOrder = new ContentValues();
            purchaseOrder.put(CartOrderContract.PurchaseOrder.COLUMN_NAME_ORDERDATE, timeStamp);
            purchaseOrder.put(CartOrderContract.PurchaseOrder.COLUMN_NAME_TOTALCOST, mTotal);
            long purchaseOrderId = mWritableDb.insert(CartOrderContract.PurchaseOrder.TABLE_NAME,null,purchaseOrder);

            //orderDetails
            int lineNo = 1;
            int amountOfItems = mFoodQuantities.size();
            for (int i = 0;i<amountOfItems;i++){
                if (mFoodQuantities.get(i) > 0){
                    ContentValues orderDetails = new ContentValues();
                    orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_PURCHASEORDERID,purchaseOrderId);
                    orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_LINENO,lineNo);
                    lineNo++;
                    orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_PRODUCTNAME, mFoods.get(i));
                    orderDetails.put(CartOrderContract.OrderDetails.COLUMN_NAME_QUANTITY, mFoodQuantities.get(i));
                    mWritableDb.insert(CartOrderContract.OrderDetails.TABLE_NAME,null,orderDetails);
                }
            }

            mConfirmationListener.onConfirmation(purchaseOrderId);
        }
    }




}
