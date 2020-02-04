package com.github.wladox.controller;

import com.github.wladox.Server;
import com.github.wladox.config.AppConfig;
import com.github.wladox.exceptions.AccountNotFoundException;
import com.github.wladox.model.Account;
import com.github.wladox.model.BalanceOperation;
import com.github.wladox.model.BalanceUpdateRequest;
import com.github.wladox.repository.AccountRepository;
import com.github.wladox.service.AccountService;
import org.junit.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class AccountsControllerTest {

    private static final String TEST_ACCOUNT_ID         = "e9552088-aaaa-bbbb-cccc-a60eef96caec";
    private static final String TEST_ACCOUNT_NUMBER     = "TEST_ACCOUNT_1234";
    private static final String TEST_ACCOUNT_DUPLICATE  = "TEST_ACCOUNT_4321";
    private static final String TEST_ACCOUNT_NON_EXIST  = "TEST_ACCOUNT_0000";
    private static final BigDecimal DEFAULT_BALANCE     = BigDecimal.valueOf(100);

    private static final Account TEST_ACCOUNT;

    static {
        TEST_ACCOUNT = new Account()
                .setId(TEST_ACCOUNT_ID)
                .setNumber(TEST_ACCOUNT_NUMBER);
    }


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static Server server;

    @BeforeClass
    public static void setup() {

        AccountRepository accountRepository     = Mockito.mock(AccountRepository.class);
        TransfersController transfersController = Mockito.mock(TransfersController.class);
        AccountService accountService           = Mockito.mock(AccountService.class);

        /*Mockito
                .when(accountRepository.create(ArgumentMatchers.any(Account.class)))
                .thenReturn(UUID.randomUUID().toString());*/

        Mockito
                .when(accountRepository.findById(TEST_ACCOUNT_ID))
                .thenReturn(Optional.of(TEST_ACCOUNT));

        AccountsController accountsController = new AccountsController(accountService);

        AppConfig config = AppConfig.builder()
                .host("localhost")
                .port(8080)
                .numberOfThreads(10)
                .build();

        server = new Server();
        server.buildAndStartServer(config, accountsController, transfersController);

    }

    @AfterClass
    public static void tearDown() {
        server.shutDown();
    }

    @Test
    public void whenCreateNewAccount_ThenReturnNewAccountId() {

        Account account = new Account()
            .setNumber(TEST_ACCOUNT_NUMBER)
            .setBalance(DEFAULT_BALANCE);

        given()
            .body(account)
            .when()
            .post(Server.ENDPOINT_ACCOUNTS)
            .then()
            .statusCode(201);
    }

    /*@Test
    public void whenUpdateAccount_ThenReturnUpdatedObject() {

        BigDecimal depositAmount = BigDecimal.valueOf(200);

        BalanceUpdateRequest request = BalanceUpdateRequest.builder()
                .operation(BalanceOperation.DEPOSIT)
                .amount(depositAmount)
                .build();

        Account updated = given()
                .pathParam("accountId", TEST_ACCOUNT_ID)
                .body(request)
                .when()
                .put(Server.ENDPOINT_ACCOUNTS + "/{accountId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Account.class);

        assertThat(updated.getBalance(), equalTo(depositAmount));
    }*/

    @Test
    public void whenGetNonExistingAccount_ThenThrowNotFoundException() {
        given()
                .pathParam("accountId", TEST_ACCOUNT_NON_EXIST)
                .when()
                .get(Server.ENDPOINT_ACCOUNTS + "{accountId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void whenGetAllAccounts_ThenReturnEmptyList(){
        Account[] accounts = given()
                .when()
                .get(Server.ENDPOINT_ACCOUNTS)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account[].class);

        assertThat(accounts, notNullValue());
    }

    /*@Test
    public void whenCreateNewAccountWithExistingNumber_ThenThrowDuplicateException() {
        given()
                .pathParam("accountId", TEST_ACCOUNT_DUPLICATE)
                .when()
                .get(Server.ENDPOINT_ACCOUNTS)
                .then()
                .statusCode(409);
    }*/
}
