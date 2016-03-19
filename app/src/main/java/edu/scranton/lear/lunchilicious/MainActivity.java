package edu.scranton.lear.lunchilicious;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        MenuFragment.OnMenuListener, FoodFragment.OnAdditemListener
        /*,CartFragment.OnCartCancelListener*/{

    private RetainedFragment mDataFragment;
    private int[] mQuantityArray;
    Menu mMenu;
    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        mDataFragment = (RetainedFragment) fm.findFragmentByTag("retained_data");

        if (mDataFragment == null) {
            // add the fragment
            mDataFragment = new RetainedFragment();
            FragmentTransaction ft = fm.beginTransaction();
            // the add here does not have a view container as the first parameter
            // so the fragment is not attached to any view, and so its onCreateView
            // will NOT be called.
            ft.add(mDataFragment, "retained_data");
            ft.commit();
            // load the data from the web
            mPosition = 0;
            mQuantityArray = getResources().getIntArray(R.array.quantityOfItems);
            mDataFragment.setPosition(mPosition);
            mDataFragment.setQuantities(mQuantityArray);

        }
        mPosition = mDataFragment.getPosition();
        mQuantityArray = mDataFragment.getQuantities();



        if (findViewById(R.id.frame_container) != null) {
            // if savedInstanceState is not null, then this activity
            // is re-created after destroyed by Android, and then
            // the fragment was saved and restored by Android
            if (savedInstanceState != null) {
                return;

            }

                MenuFragment foodMenu = new MenuFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame_container, foodMenu);
                ft.commit();
            }

            //mQuantityArray = getResources().getIntArray(R.array.quantityOfItems);
            //mPosition=0;

        else {

            // large device is used and so ArticleFragment is inflated
            // you can use findViewById() to find UI widgets in the Fragment
            //TextView textView = (TextView) findViewById(R.id.food_display);
            //textView.setText("HELLLOOOOOOOOO");

                FoodFragment foodDetail = new FoodFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame_container_second, foodDetail);
                foodDetail.setPosition(mPosition);
                ft.commit();


        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onItemSelected(int position){
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FoodFragment foodDescription = (FoodFragment) fragmentManager.findFragmentById(R.id.frame_container_second);
        mPosition = position;

        if (foodDescription != null) {
            foodDescription.updateDisplay(position);
        }
        else {
            hideCart.setVisible(false);
            foodDescription = new FoodFragment();
            Bundle args = new Bundle();
            args.putInt(FoodFragment.ARG_POSITION, position);
            foodDescription.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, foodDescription);
            transaction.addToBackStack("DETAILS");
            transaction.commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.shopping_cart) {
            MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
            hideCart.setVisible(false);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FoodFragment foodDescription = (FoodFragment) fragmentManager.findFragmentById(R.id.frame_container_second);
            CartFragment foodCart;
            foodCart = new CartFragment();
            Bundle args = new Bundle();
            args.putIntArray(CartFragment.ARG_QUANTITIES, mQuantityArray);
            foodCart.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (foodDescription != null) {
                transaction.replace(R.id.frame_container_second, foodCart);
            }
            else{
                transaction.replace(R.id.frame_container, foodCart);
            }
            transaction.addToBackStack("CART");
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addQuantity(int food, int quantity) {
        mQuantityArray[food] += quantity;
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        getSupportFragmentManager().popBackStackImmediate();
    }

    /*public void onCancelCart(){
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        mQuantityArray = getResources().getIntArray(R.array.quantityOfItems);
        getSupportFragmentManager().popBackStackImmediate();
    }*/

    public void onBackPressed(){
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // save updated data in retainedFragment
        mDataFragment.setPosition(mPosition);
        mDataFragment.setQuantities(mQuantityArray);
    }


}
