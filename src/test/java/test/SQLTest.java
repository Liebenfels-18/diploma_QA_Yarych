package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import dataCard.CardInformation;
import helper.DBHelper;
import helper.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.Home;
import pages.Payment;

import static com.codeborne.selenide.Selenide.open;
import static helper.DataHelper.getApprovedCard;

public class SQLTest {
    CardInformation data;
    Home home;

    @BeforeEach
    public void connect() {
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
    @DisplayName("APPROVED Кредит")
    public void checkCreditPaymentApprovedStatus() {
        home.payment();
        Payment payment = new Payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        DBHelper.checkStatusCreditApproved();
    }

    @Test
    @DisplayName("DECLINED Кредит")
    public void checkCreditPaymentDeclinedStatus() {
        home.payment();
        Payment payment = new Payment();
        payment.checkFullCardInfo(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        DBHelper.checkStatusCreditDeclined();
    }
    @Test
    @DisplayName("APPROVED")
    public void checkPaymentApprovedStatus() {
        home.payment();
        Payment payment = new Payment();
        payment.checkFullCardInfo(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        DBHelper.checkStatusPaymentApproved();
    }

    @Test
    @DisplayName("DECLINED")
    public void checkPaymentDeclinedStatus() {
        home.payment();
        Payment payment = new Payment();
        payment.checkFullCardInfo(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        DBHelper.checkStatusPaymentDeclined();
    }

}