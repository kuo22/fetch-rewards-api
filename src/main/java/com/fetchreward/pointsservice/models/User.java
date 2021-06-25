package com.fetchreward.pointsservice.models;

import java.util.*;

public class User {
    private final int id;
    //private final Map<String, Integer> balance = new HashMap<>();
    private final Queue<Transaction> transactions = new PriorityQueue<>(Comparator.comparing(Transaction::getTimestamp));

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Map<String, Integer> getBalance() {
        Map<String, Integer> balance = new HashMap<>();
        transactions.forEach(transaction -> {
            String payer = transaction.getPayer();
            int points = balance.getOrDefault(payer, 0);
            balance.put(payer, points + transaction.getPoints());
        });

        return balance;
    }

    public Queue<Transaction> getTransactions() {
        return transactions;
    }

    public int getTotalBalance() {
        int total = 0;
        Map<String, Integer> balance = getBalance();
        for (String key : balance.keySet()) {
            total += balance.get(key);
        }

        return total;
    }
}
