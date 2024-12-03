package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.concurrent.atomic.AtomicReference;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private AtomicReference<Double> balance = new AtomicReference<>(0.0);

    public Account() {}

    public Account(String owner, double initialBalance) {
        this.owner = owner;
        this.balance.set(initialBalance);
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance.get();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance.updateAndGet(current -> current + amount);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        balance.updateAndGet(current -> {
            if (current < amount) {
                throw new IllegalArgumentException("Insufficient balance.");
            }
            return current - amount;
        });
    }
}