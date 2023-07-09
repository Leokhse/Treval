package ru.netology.treval;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        SqlHelper.clearCreditRequestTable();
        SqlHelper.clearOrderTable();
    }

    @Test
    @Order(1)
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

        assertTrue(paymentPage.isStatusOkNotificationDisplayed());

        assertTrue(SqlHelper.isPaymentStatusApproved());
    }

    @Test
    @Order(2)
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

        assertTrue(paymentPage.isStatusOkNotificationDisplayed());

        assertTrue(SqlHelper.isPaymentStatusDeclined());
    }


    @Test
    @Order(3)
    public void testCreditCardPaymentDeclined() {

        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed());

        assertTrue(SqlHelper.isCreditRequestStatusDeclined());
    }

    @Test
    @Order(4)
    public void testCreditCardPaymentApproved() {

        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed());

        assertTrue(SqlHelper.isCreditRequestStatusApproved());
    }

    @Test
    @Order(5)
    public void testInvalidCardNumberLessThan16Digits() {

        paymentPage.clickBuyOnCreditButton();
        paymentPage.enterCardNumber("123456789012345");
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(6)
    public void testEmptyCardNumberField() {

        paymentPage.clickBuyOnCreditButton();
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(7)
    public void testMonthFieldFilledWithDigitFrom0To9() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth("9");
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(8)
    public void testMonthFieldFilledWithNumberGreaterThan12() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth("13");
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    @Order(9)
    public void testMonthFieldFilledWithValueLessThanCurrentMonth() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth("06");
        paymentPage.enterYear("23");
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    @Order(10)
    public void testEmptyMonthField() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(11)
    public void testYearFieldFilledWithDigitFrom0To9() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear("6");
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(12)
    public void testYearFieldFilledWithValueLessThanCurrentYear() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear("22");
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Истёк срок действия карты", errorMessage);
    }

    @Test
    @Order(13)
    public void testYearFieldFilledWithValueGreaterThan6YearsFromCurrentYear() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear("29");
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    @Order(14)
    public void testEmptyYearField() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(15)
    public void testCvcFieldFilledWithDigitFrom0To9() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc("4");

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(16)
    public void testCvcFieldFilledWithTwoDigitNumber() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc("66");

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }


    @Test
    @Order(17)
    public void testIncorrectCardNumber() {

        paymentPage.clickBuyButton();

        paymentPage.enterCardNumber("3333333333333333");
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed());
    }

    @Test
    @Order(18)
    public void testOwnerFieldFilledWithSpecialCharacters() {

        paymentPage.clickBuyOnCreditButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner("!№;%:?*");
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        String errorMessage = paymentPage.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    @Order(19)
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

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed());

        assertTrue(SqlHelper.isPaymentStatusDeclined());
    }

    @Test
    @Order(20)
    public void testBugCreditCardPaymentDeclined() {

        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed());

        assertTrue(SqlHelper.isCreditRequestStatusDeclined());
    }

}