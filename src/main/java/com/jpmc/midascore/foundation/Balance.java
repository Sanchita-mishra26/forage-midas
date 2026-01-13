package com.jpmc.midascore.foundation;

public class Balance {
    private float amount;

    public Balance() {
        this.amount = 0;
    }

    public Balance(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    // THIS IS THE MISSING PIECE!
    @Override
    public String toString() {
        return "Balance { amount=" + amount + " }";
    }
}