package org.example.controller;

import org.example.model.Account;
import org.example.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return bankService.getAllAccounts();
    }

    @PostMapping
    public Account createAccount(@RequestParam String owner, @RequestParam double initialBalance) {
        return bankService.createAccount(owner, initialBalance);
    }

    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam double amount) {
        bankService.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam double amount) {
        bankService.withdraw(id, amount);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        bankService.transfer(fromId, toId, amount);
    }
}