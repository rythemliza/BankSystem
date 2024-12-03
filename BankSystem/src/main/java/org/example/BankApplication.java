package org.example;

import org.example.model.Account;
import org.example.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class BankApplication implements CommandLineRunner {

    @Autowired
    private BankService bankService;

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Create accounts
        Account acc1 = bankService.createAccount("Alice", 1000);
        Account acc2 = bankService.createAccount("Bob", 2000);

        // Multithreaded simulation
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.submit(() -> bankService.deposit(acc1.getId(), 100 * finalI));
            executor.submit(() -> bankService.withdraw(acc2.getId(), 5 * finalI));
        }
        try{
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(bankService.getBalance(acc1.getId()));
        System.out.println(bankService.getBalance(acc2.getId()));

        executor.submit(() -> bankService.transfer(acc1.getId(), acc2.getId(), 500));

        executor.shutdown();
        try{
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println(bankService.getBalance(acc1.getId()));
        System.out.println(bankService.getBalance(acc2.getId()));

        assert (bankService.getBalance(acc1.getId()) == (5000)) : "Account1 balance updated correctly";
        assert (bankService.getBalance(acc2.getId()) == (2275)): "Account2 balance updated correctly";
    }
}