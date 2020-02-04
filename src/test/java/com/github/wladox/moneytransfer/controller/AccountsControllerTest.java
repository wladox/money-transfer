package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.Server;
import com.github.wladox.moneytransfer.config.AppConfig;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.repository.AccountRepository;
import com.github.wladox.moneytransfer.service.AccountService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


public class AccountsControllerTest {

    private static final String TEST_ACCOUNT_ID         = "e9552088-aaaa-bbbb-cccc-a60eef96caec";
    private static final String TEST_ACCOUNT_NUMBER     = "TEST_ACCOUNT_1234";
    private static final String TEST_ACCOUNT_DUPLICATE  = "TEST_ACCOUNT_4321";
    private static final String TEST_ACCOUNT_NON_EXIST  = "TEST_ACCOUNT_0000";
    private static final BigDecimal DEFAULT_BALANCE     = BigDecimal.valueOf(100);

    private static final Account TEST_ACCOUNT;

    static {
        TEST_ACCOUNT = Account.builder()
                .id(TEST_ACCOUNT_ID)
                .number(TEST_ACCOUNT_NUMBER)
                .build();
    }


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static Server server;

    @BeforeClass
    public static void setup() {

        AccountRepository accountRepository     = Mockito.mock(AccountRepository.class);
        TransfersController transfersController = Mockito.mock(TransfersController.class);
        AccountService accountService           = Mockito.mock(AccountService.class);

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

        Account account = Account.builder()
                .number(TEST_ACCOUNT_NUMBER)
                .balance(DEFAULT_BALANCE)
                .build();

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
