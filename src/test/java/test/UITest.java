package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import dataCard.CardInformation;
import helper.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.PaymentOnCredit;
import pages.Home;
import pages.Payment;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static helper.DataHelper.getApprovedCard;

public class UITest {

    CardInformation data;
    Home home;

    @BeforeEach
    public void prepare() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities=options;
        open("http://localhost:8080/");
        data = getApprovedCard();
        home = new Home();
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
    @DisplayName("Отправка пустой формы")
    public void testEmptyInfo() {
        Payment payment = home.payment();
        payment.checkAllFormsEmpty();
    }
    @Test
    @DisplayName("Отправка формы с валидными данными")
    public void testValidInfo() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы с картой со статусом DECLINED")
    public void testDeclinedCard() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getDeclinedCardNumber(), data.getMonth(),data.getYear(), data.getName(), data.getCvc());
        payment.checkDeclinedCardData();
    }

    @Test
    @DisplayName("Отправка формы с коротким номером карты")
    public void testShortCardNumber() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getShortCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkWrongNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Номер карты")
    public void testEmptyNumberCard() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Месяц")
    public void testEmptyMonth() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), null, data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с неверно заполненным полем Месяц")
    public void testWrongMonth() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkWrongFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с неверным форматом в поле Месяц")
    public void testWrongFormatMonth() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidFormatMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Год")
    public void testEmptyYear() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
        payment.checkEmptyFieldYear();
    }

    @Test
    @DisplayName("Отправка формы с неверно заполненным полем Год")
    public void testWrongYear() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), DataHelper.getInvalidYear(), data.getName(), data.getCvc());
        payment.checkWrongFieldYear();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Имя")
    public void testEmptyOwner() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
        payment.checkEmptyFieldOwner();
    }

    @Test
    @DisplayName("Отправка формы с тире в поле Имя")
    public void testNameWithDash() {
        Payment payment = home.payment();
        String name = "Petrova Anna-Maria";
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы с полем Имя на кирилице")
    public void testCyrillikSymbolsInName() {
        Payment payment = home.payment();
        String name = "Никита Ярыч";
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с цифрами в поле Имя")
    public void testNameWithNumbers() {
        Payment payment = home.payment();
        String name = "Yarych9553";
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы со спец.символами в поле Имя")
    public void testNameWithSpecSymbols() {
        Payment payment = home.payment();
        String name = "|/''#$%^&*(@#!_)(+=*";
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем CVC")
    public void testEmptyCVC() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
        payment.checkEmptyFieldCVC();
    }

    @Test
    @DisplayName("Отправка формы с неверным значением в поле CVC")
    public void testInvalidCVC() {
        Payment payment = home.payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        payment.checkEmptyFieldCVC();
    }

//Кредит
    @Test
    @DisplayName("Отправка пустой формы Кредит")
    public void testEmptyCreditData() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkAllFormsEmpty();
    }

    @Test
    @DisplayName("Отправка формы Кредит с валидными данными")
    public void testValidCreditData() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с картой со статусом DECLINED")
    public void testInvalidCreditData() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkDeclinedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с коротким номером карты")
    public void testShortCardNumberInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Номер карты")
    public void testEmptyCardNumberFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyNumberFieldCard();
    }


    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Месяц")
    public void testEmptyMonthFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), null, data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверно заполненным полем Месяц")
    public void testWrongMonthInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверным форматом в поле Месяц")
    public void testWrongFormatMonthInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidFormatMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldMonth();
    }
    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Год")
    public void testEmptyYearFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
        creditRequest.checkEmptyFieldYear();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверно заполненным полем Год")
    public void testWrongYearInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), DataHelper.getInvalidYear(), data.getName(), data.getCvc());
        creditRequest.checkWrongFieldYear();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем Имя")
    public void testEmptyOwnerFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
        creditRequest.checkEmptyFieldOwner();
    }

    @Test
    @DisplayName("Отправка формы Кредит с тире в поле Имя")
    public void testNameWithDashInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        String name = "Petrova Anna-Maria";
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы Кредит с полем Имя на кирилице")
    public void testCyrillikNameInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        String name = "Никита Ярыч";
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит с цифрами в поле Имя")
    public void testNameWithNumberInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        String name = "Yarych1243";
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит со спец.символами в поле Имя")
    public void testNameWithSpecSymbolsInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        String name = "|/''#$%^&*(@#!_)(+=*";
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы Кредит с незаполненным полем CVC")
    public void testEmptyCVCFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
        creditRequest.checkEmptyFieldCVC();
    }

    @Test
    @DisplayName("Отправка формы Кредит с неверным значением в поле CVC")
    public void testInvalidCVCFieldInCredit() {
        PaymentOnCredit creditRequest = home.creditPayment();
        creditRequest.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        creditRequest.checkEmptyFieldCVC();
    }








}