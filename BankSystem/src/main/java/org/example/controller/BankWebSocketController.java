package org.example.controller;

import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BankWebSocketController {
    @Autowired
    private AccountRepository accountRepository;

    @MessageMapping("/accounts")
    @SendTo("/topic/updates")
    public List<Account> getAccountUpdates() {
        return accountRepository.findAll();
    }
}