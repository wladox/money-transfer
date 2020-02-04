package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.Server;
import com.github.wladox.moneytransfer.config.AppConfig;
import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Transfer;
import com.github.wladox.moneytransfer.service.MoneyTransferService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class TransfersControllerTest {

    static final String TEST_ACCOUNT_A = "test_account_a";
    static final String TEST_ACCOUNT_B = "test_account_b";

    private Server server;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    MoneyTransferService transferService;

    @Before
    public void setup() {

        AccountsController accountsController = Mockito.mock(AccountsController.class);

        TransfersController transfersController  = new TransfersController(transferService);

        AppConfig config = AppConfig.builder()
                .host("localhost")
                .port(8080)
                .numberOfThreads(10)
                .build();

        server = new Server();
        server.buildAndStartServer(config, accountsController, transfersController);

    }

    @After
    public void tearDown() {
        server.shutDown();
    }

    @Test
    public void givenTransferToNonExistingAccount_ThenReturnErrorResponse() throws AccountNotFoundException, TransactionException {

        Transfer t = Transfer.builder()
                .from(TEST_ACCOUNT_A)
                .to(TEST_ACCOUNT_B)
                .amount(BigDecimal.ONE)
                .build();

        doThrow(TransactionException.class).when(transferService).process(t);

        given()
                .body(t)
                .when()
                .post(Server.ENDPOINT_TRANSFERS)
                .then()
                .statusCode(400);
    }

    @Test
    public void givenSenderWithInsufficientBalance_whenTransfer_thenShouldReturnError() throws AccountNotFoundException, TransactionException {
        Transfer testTransfer = Transfer.builder().build();

        doThrow(TransactionException.class).when(transferService).process(testTransfer);

        given()
                .body(testTransfer)
                .when()
                .post(Server.ENDPOINT_TRANSFERS)
                .then()
                .statusCode(400);
    }

    @Test
    public void givenValidTransfer_whenCreate_thenShouldReturnTransactionId() throws AccountNotFoundException, TransactionException {
        Transfer testTransfer = Transfer.builder().build();

        doReturn(new Random().nextLong()).when(transferService).process(testTransfer);

        String txId = given()
                .body(testTransfer)
                .when()
                .post(Server.ENDPOINT_TRANSFERS)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(String.class);

        assertThat(txId, is(notNullValue()));
    }
}
