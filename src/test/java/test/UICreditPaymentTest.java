package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import dataCard.CardInformation;
import helper.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.PaymentOnCreditPage;
import pages.HomePage;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static helper.DataHelper.getApprovedCard;

public class UICreditPaymentTest {

    CardInformation data;
    HomePage home;

    @BeforeEach
    public void prepare() throws ParseException {
        open("http://localhost:8080/");
        data = getApprovedCard();
        home = new HomePage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Отправка пустой формы Кредит")
    public void testEmptyCreditData() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.checkAllFormsEmpty();
    }

    @Test
    @DisplayName("Отправка формы Кредит с валидными данными")
    public void testValidCreditData() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с картой со статусом DECLINED")
    public void testInvalidCreditData() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkDeclinedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с коротким номером карты")
    public void testShortCardNumberInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Номер карты")
    public void testEmptyCardNumberFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyNumberFieldCard();
    }


    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Месяц")
    public void testEmptyMonthFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), null, data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверно заполненным полем Месяц")
    public void testWrongMonthInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверным форматом в поле Месяц")
    public void testWrongFormatMonthInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidFormatMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldMonth();
    }
    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Год")
    public void testEmptyYearFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldYear();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверно заполненным полем Год")
    public void testWrongYearInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), DataHelper.getInvalidYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongFieldYear();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Имя")
    public void testEmptyOwnerFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
        creditRequest.checkEmptyFieldOwner();
    }

    @Test
    @DisplayName("Отправка формы Кредит с тире в поле Имя")
    public void testNameWithDashInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        String name = "Petrova Anna-Maria";
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с полем Имя на кирилице")
    public void testCyrillikNameInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        String name = "Никита Ярыч";
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит с цифрами в поле Имя")
    public void testNameWithNumberInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        String name = "Yarych1243";
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит со спец.символами в поле Имя")
    public void testNameWithSpecSymbolsInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        String name = "|/''#$%^&*(@#!_)(+=*";
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем CVC")
    public void testEmptyCVCFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
        creditRequest.checkEmptyFieldCVC();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверным значением в поле CVC")
    public void testInvalidCVCFieldInCredit() {
        PaymentOnCreditPage creditRequest = home.creditPayment();
        creditRequest.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        creditRequest.checkEmptyFieldCVC();
    }








}