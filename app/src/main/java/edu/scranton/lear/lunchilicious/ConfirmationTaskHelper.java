package edu.scranton.lear.lunchilicious;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by teddylear on 4/26/2016.
 */
public class ConfirmationTaskHelper {
    private ArrayList<String> mFoods;
    private ArrayList<Double> mTotals;
    private ArrayList<Integer> mQuantity;

    public ConfirmationTaskHelper() {
        mFoods = new ArrayList<>();
        mTotals = new ArrayList<>();
        mQuantity = new ArrayList<>();
    }


    public void setFoods(ArrayList<String> foods) {this.mFoods = foods;}
    public ArrayList<String> getFoods() {return mFoods;}
    public void setQuantities(ArrayList<Integer> quantities) {this.mQuantity = quantities;}
    public ArrayList<Integer> getQuantities() {return mQuantity;}
    public void setTotals(ArrayList<Double> totals){this.mTotals = totals;}
    public ArrayList<Double> getTotals(){return mTotals;}

}
