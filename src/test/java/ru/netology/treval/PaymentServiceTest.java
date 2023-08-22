package ru.netology.treval;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentServiceTest {
    private PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        paymentPage = open("http://localhost:8080/", PaymentPage.class);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SqlHelper.clearPaymentTable();
        SqlHelper.clearOrderTable();
    }

    @Test
    public void testDebitCardPaymentApproved() {
        paymentPage.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusApproved(), "Статус платежа не является 'APPROVED'");
    }

    @Test
    public void testDebitCardPaymentDeclined() {
        paymentPage.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusDeclined(), "Статус платежа не является 'DECLINED'");
    }

    @Test
    public void testIncorrectCardNumber() {
        paymentPage.clickBuyButton();

        paymentPage.enterCardNumber("3333333333333333");
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed(), "Всплывающее окно с ошибкой");
    }


    @Test
    public void testBugDebitCardPaymentDeclined() {

        paymentPage.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed(), "Всплывающее окно с ошибкой не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusDeclined(), "Статус платежа не является 'DECLINED'");
    }

}