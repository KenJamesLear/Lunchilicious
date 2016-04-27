package edu.scranton.lear.lunchilicious;

import java.util.ArrayList;

/**
 * Created by teddylear on 4/27/2016.
 */
public class CartTaskHelper {
    private ArrayList<String> mFoods;
    private Double mTotal;
    private ArrayList<Integer> mQuantity;
    private long mPurchaseOrderId;

    public CartTaskHelper() {
        mFoods = new ArrayList<>();
        mTotal = 0.00;
        mQuantity = new ArrayList<>();
        mPurchaseOrderId = -1;
    }


    public void setFoods(ArrayList<String> foods) {this.mFoods = foods;}
    public ArrayList<String> getFoods() {return mFoods;}
    public void setQuantities(ArrayList<Integer> quantities) {this.mQuantity = quantities;}
    public ArrayList<Integer> getQuantities() {return mQuantity;}
    public void setTotal(Double totals){this.mTotal = totals;}
    public Double getTotal(){return mTotal;}
    public void setPurchaseOrderId(long purchaseOrderId){this.mPurchaseOrderId = purchaseOrderId;}
    public long getPurchaseOrderId(){return mPurchaseOrderId;}
}
