package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import dataCard.CardInformation;
import helper.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.HomePage;
import pages.PaymentPage;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static helper.DataHelper.getApprovedCard;

public class UIPaymentTest {

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
    @DisplayName("Отправка пустой формы")
    public void testEmptyInfo() {
        PaymentPage payment = home.payment();
        payment.checkAllFormsEmpty();
    }
    @Test
    @DisplayName("Отправка формы с валидными данными")
    public void testValidInfo() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        sleep(5000);
        payment.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы с картой со статусом DECLINED")
    public void testDeclinedCard() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getDeclinedCardNumber(), data.getMonth(),data.getYear(), data.getName(), data.getCvc());
        payment.checkDeclinedCardData();
    }

    @Test
    @DisplayName("Отправка формы с коротким номером карты")
    public void testShortCardNumber() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getShortCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkWrongNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Номер карты")
    public void testEmptyNumberCard() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyNumberFieldCard();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Месяц")
    public void testEmptyMonth() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), null, data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с неверно заполненным полем Месяц")
    public void testWrongMonth() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkWrongFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с неверным форматом в поле Месяц")
    public void testWrongFormatMonth() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), DataHelper.getInvalidFormatMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkEmptyFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Год")
    public void testEmptyYear() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
        payment.checkEmptyFieldYear();
    }

    @Test
    @DisplayName("Отправка формы с неверно заполненным полем Год")
    public void testWrongYear() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), DataHelper.getInvalidYear(), data.getName(), data.getCvc());
        payment.checkWrongFieldYear();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Имя")
    public void testEmptyOwner() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
        payment.checkEmptyFieldOwner();
    }

    @Test
    @DisplayName("Отправка формы с тире в поле Имя")
    public void testNameWithDash() {
        PaymentPage payment = home.payment();
        String name = "Petrova Anna-Maria";
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkAcceptedCardData();
    }

    @Test
    @DisplayName("Отправка формы с полем Имя на кирилице")
    public void testCyrillikSymbolsInName() {
        PaymentPage payment = home.payment();
        String name = "Никита Ярыч";
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с цифрами в поле Имя")
    public void testNameWithNumbers() {
        PaymentPage payment = home.payment();
        String name = "Yarych9553";
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы со спец.символами в поле Имя")
    public void testNameWithSpecSymbols() {
        PaymentPage payment = home.payment();
        String name = "|/''#$%^&*(@#!_)(+=*";
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.checkWrongOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем CVC")
    public void testEmptyCVC() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
        payment.checkEmptyFieldCVC();
    }

    @Test
    @DisplayName("Отправка формы с неверным значением в поле CVC")
    public void testInvalidCVC() {
        PaymentPage payment = home.payment();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        payment.checkEmptyFieldCVC();
    }
}
