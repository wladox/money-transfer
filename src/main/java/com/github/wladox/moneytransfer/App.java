package com.github.wladox.moneytransfer;

import com.github.wladox.moneytransfer.config.AppConfig;
import com.github.wladox.moneytransfer.controller.AccountsController;
import com.github.wladox.moneytransfer.controller.TransfersController;
import com.github.wladox.moneytransfer.repository.AccountRepository;
import com.github.wladox.moneytransfer.repository.TransactionRepository;
import com.github.wladox.moneytransfer.repository.impl.InMemoryAccountRepository;
import com.github.wladox.moneytransfer.repository.impl.InMemoryTransactionRepository;
import com.github.wladox.moneytransfer.service.AccountService;
import com.github.wladox.moneytransfer.service.MoneyTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {


    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args){

        log.info("Wiring up components...");

        // Data storage
        final AccountRepository accountRepository = InMemoryAccountRepository.getInstance();
        final TransactionRepository transactionRepository = InMemoryTransactionRepository.getInstance();

        // Services
        final MoneyTransferService service = new MoneyTransferService(accountRepository, transactionRepository);
        final AccountService accountService = new AccountService(accountRepository);

        // Controllers
        final TransfersController transfersHandler = new TransfersController(service);
        final AccountsController accountsController = new AccountsController(accountService);

        log.info("Starting server...");

        Server server = new Server();
        server.buildAndStartServer(AppConfig.getDefault(), accountsController, transfersHandler);


    }
}
