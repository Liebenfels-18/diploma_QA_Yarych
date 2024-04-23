package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import dataCard.CardInformation;
import helper.DBHelper;
import helper.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.HomePage;
import pages.PaymentPage;

import java.sql.SQLException;
import java.text.ParseException;

import static com.codeborne.selenide.Selenide.open;
import static helper.DataHelper.getApprovedCard;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLTest {
    CardInformation data;
    HomePage home;

    @BeforeEach
    public void connect() throws ParseException {
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
    @DisplayName("APPROVED Кредит")
    public void checkCreditPaymentApprovedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        checkStatusCreditApproved();
    }

    @Test
    @DisplayName("DECLINED Кредит")
    public void checkCreditPaymentDeclinedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardData(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        checkStatusCreditDeclined();
    }
    @Test
    @DisplayName("APPROVED")
    public void checkPaymentApprovedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardData(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        checkStatusPaymentApproved();
    }

    @Test
    @DisplayName("DECLINED")
    public void checkPaymentDeclinedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardData(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.checkAcceptedCardData();
        checkStatusPaymentDeclined();
    }

    private void checkStatusPaymentApproved() {
        String id = DBHelper.getOrderEntityData().getPayment_id();
        String actual = DBHelper.getStatus(id);
        String expected = "APPROVED";
        assertEquals(expected, actual);
    }

    private void checkStatusPaymentDeclined() {
        String id = DBHelper.getOrderEntityData().getPayment_id();
        String actual = DBHelper.getStatus(id);
        String expected = "DECLINED";
        assertEquals(expected, actual);
    }

    private void checkStatusCreditApproved() {
        String id = DBHelper.getOrderEntityData().getCredit_id();
        String actual = DBHelper.getCreditStatus(id);
        String expected = "APPROVED";
        assertEquals(expected, actual);
    }

    private void checkStatusCreditDeclined() {
        String id = DBHelper.getOrderEntityData().getCredit_id();
        String actual = DBHelper.getCreditStatus(id);
        String expected = "DECLINED";
        assertEquals(expected, actual);
    }

}