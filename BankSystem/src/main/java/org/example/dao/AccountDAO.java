package org.example.dao;

import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AccountDAO {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountDAO(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public synchronized Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public synchronized Account createAccount(String owner, double initialBalance) throws Exception{
        return accountRepository.save(new Account(owner, initialBalance));
    }

    public synchronized void updateBalance(long accountId, double updatedBalance) throws Exception{
        Account account = getAccount(accountId);
        account.deposit(updatedBalance);
        accountRepository.save(account);
    }

    public synchronized void deposit(Long accountId, double amount) {
        Account account = getAccount(accountId);
        account.deposit(amount);
        accountRepository.save(account);
    }

    public synchronized void withdraw(Long accountId, double amount) {
        Account account = getAccount(accountId);
        account.withdraw(amount);
        accountRepository.save(account);
    }

    @Transactional
    public synchronized void transfer(Long fromAccountId, Long toAccountId, double amount) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
