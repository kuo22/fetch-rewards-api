package com.fetchreward.pointsservice.controllers;

import com.fetchreward.pointsservice.models.Points;
import com.fetchreward.pointsservice.models.Spending;
import com.fetchreward.pointsservice.models.Transaction;
import com.fetchreward.pointsservice.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class RewardController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping(path = "/balance/{id}")
    public Map<String, Integer> getBalance(@PathVariable int id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User does not exist.");
        }

        return users.get(id).getBalance();
    }

    @PostMapping(path = "/transaction/{id}", consumes = "application/json")
    public String postTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        validateTransaction(id, transaction);

        addTransaction(id, transaction);

        return "Successfully added!";
    }

    @PostMapping(path = "/spend/{id}", consumes = "application/json")
    public List<Spending> postSpend(@PathVariable int id, @RequestBody Points points) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User does not exist.");
        }

        if (points.points <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot spend a negative amount.");
        }

        User user = users.get(id);
        int totalBalance = user.getTotalBalance();

        if (points.points > totalBalance) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot have negative balance.");
        }

        return spendPoints(id, points.points);
    }

    public List<Spending> spendPoints(int id, int pointsToSpend) {
        User user = users.get(id);

        // Sort by oldest transactions to be spent first
        List<Transaction> transactions = user.getTransactions();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        Iterator<Transaction> iterator = transactions.iterator();

        List<Spending> spendings = new ArrayList<>();
        Map<String, Integer> spendingsMap = new HashMap<>();

        while (pointsToSpend > 0) {
            Transaction transaction = iterator.next();
            String payer = transaction.getPayer();
            int transactionPoints = transaction.getPoints();
            int payerSpending = spendingsMap.getOrDefault(payer, 0);
            
            if (pointsToSpend >= transactionPoints) {
                spendingsMap.put(payer, payerSpending + transactionPoints);

                transaction.setPoints(0);
                
                pointsToSpend -= transactionPoints;
            } else {
                spendingsMap.put(payer, payerSpending + pointsToSpend);

                transaction.setPoints(transactionPoints - pointsToSpend);
                
                pointsToSpend = 0;
            }
        }

        for (String key : spendingsMap.keySet()) {
            Spending spending = new Spending(key, -1 * spendingsMap.get(key));
            spendings.add(spending);
        }

        return spendings;
    }

    private void validateTransaction(int id, Transaction transaction) {
        if (transaction.getPoints() == 0) {
            throw new IllegalArgumentException("Transaction should not contain 0 points");
        }

        if (transaction.getPoints() < 0) {
            if (!users.containsKey(id)) {
                throw new IllegalArgumentException("Payer cannot have negative balance: new user");
            } else {
                User user = users.get(id);
                Map<String, Integer> balance = user.getBalance();
                if (!balance.containsKey(transaction.getPayer())) {
                    throw new IllegalArgumentException("Payer cannot have negative balance: no existing payer points");
                }

                int payerPoints = balance.get(transaction.getPayer());
                if (payerPoints - transaction.getPoints() < 0) {
                    throw new IllegalArgumentException("Payer cannot have negative balance");
                }
            }
        }
    }

    private void addTransaction(int id, Transaction transaction) {
        if (!users.containsKey(id)) {
            users.put(id, new User(id));
        } 
        
        User user = users.get(id);
        user.getTransactions().add(transaction);
    }
}
