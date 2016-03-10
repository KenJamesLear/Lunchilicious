package edu.scranton.lear.lunchilicious;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        MenuFragment.OnMenuListener, FoodFragment.OnAdditemListener,
        CartFragment.OnCartCancelListener{

    private int[] mQuantityArray;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MenuFragment foodMenu  = new MenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_container, foodMenu);
        ft.commit();

        mQuantityArray = getResources().getIntArray(R.array.quantityOfItems);

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
        FoodFragment foodDescription;
        foodDescription = new FoodFragment();
        Bundle args = new Bundle();
        args.putInt(FoodFragment.ARG_POSITION, position);
        foodDescription.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, foodDescription);
        transaction.addToBackStack("DETAILS");
        transaction.commit();
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
            CartFragment foodCart;
            foodCart = new CartFragment();
            Bundle args = new Bundle();
            args.putIntArray(CartFragment.ARG_QUANTITIES, mQuantityArray);
            foodCart.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, foodCart);
            transaction.addToBackStack("CART");
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addQuantity(int food, int quantity) {
        mQuantityArray[food] += quantity;
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        if (getSupportFragmentManager().popBackStackImmediate("DETAILS", FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
            Toast.makeText(this, "Returned from Detail", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pop from Backstack failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancelCart(){
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        mQuantityArray = getResources().getIntArray(R.array.quantityOfItems);
        if (getSupportFragmentManager().popBackStackImmediate("CART", FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
            Toast.makeText(this, "Returned from Cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pop from Backstack failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        MenuItem hideCart = mMenu.findItem(R.id.shopping_cart);
        hideCart.setVisible(true);
        getSupportFragmentManager().popBackStackImmediate();
    }


}
