package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl  implements BankService{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public synchronized Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Override
    public synchronized Account createAccount(String owner, double initialBalance) {
        return accountRepository.save(new Account(owner, initialBalance));
    }

    @Override
    public synchronized void deposit(Long accountId, double amount) {
        Account account = getAccount(accountId);
        account.deposit(amount);
        accountRepository.save(account);
        broadcastAccountUpdate(account);
    }

    @Override
    public synchronized void withdraw(Long accountId, double amount) {
        Account account = getAccount(accountId);
        account.withdraw(amount);
        accountRepository.save(account);
        broadcastAccountUpdate(account);
    }

    @Transactional
    @Override
    public synchronized void transfer(Long fromAccountId, Long toAccountId, double amount) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        broadcastAccountUpdate(fromAccount);
        broadcastAccountUpdate(toAccount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return  accountRepository.findAll();
    }

    @Override
    public Double getBalance(Long accountId) {
           Optional<Account> account = accountRepository.findById(accountId);
           return account.map(Account::getBalance).orElse(0.0);

    }

    private void broadcastAccountUpdate(Account account) {
        messagingTemplate.convertAndSend("/topic/account-updates", account);
    }
}
