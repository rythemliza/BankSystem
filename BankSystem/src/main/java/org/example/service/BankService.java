package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dao.AccountDAO;
import org.example.model.Account;

import java.sql.SQLException;
import java.util.List;

public interface BankService {

    Account getAccount(Long id);

    Account createAccount(String owner, double initialBalance);

    void deposit(Long accountId, double amount);

    void withdraw(Long accountId, double amount);

    @Transactional
    void transfer(Long fromAccountId, Long toAccountId, double amount);

    List<Account> getAllAccounts();

    Double getBalance(Long accountId);
}
