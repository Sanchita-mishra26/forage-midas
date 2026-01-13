package com.jpmc.midascore.entity;

public class Transaction {
    private long senderId;
    private long receiverId;
    private float amount;

    // Default constructor is needed for Kafka to work
    public Transaction() {}

    public Transaction(long senderId, long receiverId, float amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    // Getters and Setters for the other fields
    public long getSenderId() { return senderId; }
    public void setSenderId(long senderId) { this.senderId = senderId; }
    public long getReceiverId() { return receiverId; }
    public void setReceiverId(long receiverId) { this.receiverId = receiverId; }

    @Override
    public String toString() {
        return "Transaction{sender=" + senderId + ", receiver=" + receiverId + ", amount=" + amount + "}";
    }
}