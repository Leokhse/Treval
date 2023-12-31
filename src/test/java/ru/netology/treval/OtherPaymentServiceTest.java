package ru.netology.treval;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OtherPaymentServiceTest {
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
        SqlHelper.clearOrderTable();
    }

    @Test
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

}