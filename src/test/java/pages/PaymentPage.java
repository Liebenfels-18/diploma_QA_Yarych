package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    private SelenideElement numberCard = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement monthRelease = $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement yearRelease = $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement owner = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvc = $("input[type=\"text\"][placeholder=\"999\"]");
    private SelenideElement button = $$(".button").find(exactText("Продолжить"));
    private SelenideElement errorCardNumber = $x("//*[@id=\"root\"]/div/form/fieldset/div[1]/span/span/span[3]");
    private SelenideElement errorMonth = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[3]");
    private SelenideElement errorYear = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[2]/span/span/span[3]");
    private SelenideElement errorOwner = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[1]/span/span/span[3]");
    private SelenideElement errorCVC = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[2]/span/span/span[3]");
    private SelenideElement inputInvalid = $(".input__sub");
    private SelenideElement btnSend = $x("//*[@id=\"root\"]/div/form/fieldset/div[4]/button/span/span/span");
    private SelenideElement notificationWaitSend = $x("//*[text()=\"Отправляем запрос в Банк...\"]");
    private SelenideElement notificationStatusOk = $x("//div[@class = \"notification notification_visible notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white\"]//div[@class = \"notification__content\"]");
    private SelenideElement snapCloseReportApproved = $x("//*[@id=\"root\"]/div/div[2]/button/span/span");
    private SelenideElement notificationDeclined = $x("//*[@id=\"root\"]/div/div[3]/div[3]");
    private SelenideElement snapCloseDeclined = $x("//*[@id=\"root\"]/div/div[3]/button");

    public void enterValidCardData(String numberCard, String month, String year, String ownerName, String cvc) {
        this.numberCard.val(String.valueOf(numberCard));
        this.monthRelease.val(String.valueOf(month));
        this.yearRelease.val(String.valueOf(year));
        this.owner.val(ownerName);
        this.cvc.val(String.valueOf(cvc));
        button.click();
    }

    public void checkAcceptedCardData() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
        btnSend.should(visible);
        notificationWaitSend.should(text("Отправляем запрос в Банк..."));
        notificationStatusOk.should(text("Операция одобрена Банком."), Duration.ofSeconds(15));
        snapCloseReportApproved.click();
    }

    public void checkDeclinedCardData() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
        btnSend.should(visible);
        notificationWaitSend.should(text("Отправляем запрос в Банк..."));
        notificationDeclined.should(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(15));
        snapCloseDeclined.click();
    }

    public void checkAllFormsEmpty() {
        button.click();
        errorCardNumber.should(visible);
        errorMonth.should(visible);
        errorYear.should(visible);
        errorOwner.should(visible);
        errorCVC.should(visible);
    }

    public void checkWrongFieldMonth() {
        errorCardNumber.should(hidden);
        errorMonth.should(visible);
        errorMonth.should(text("Неверно указан срок действия карты"));
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }

    public void checkWrongFieldYear() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(visible);
        errorYear.should(text("Истёк срок действия карты"));
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }

    public void checkEmptyFieldMonth() {
        errorCardNumber.should(hidden);
        errorMonth.should(visible);
        errorMonth.should(text("Неверный формат"));
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }

    public void checkEmptyFieldYear() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(visible);
        errorYear.should(text("Неверный формат"));
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }

    public void checkEmptyFieldOwner() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(visible);
        errorOwner.should(text("Поле обязательно для заполнения"));
        errorCVC.should(hidden);
    }

    public void checkEmptyFieldCVC() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(visible);
        errorCVC.should(text("Неверный формат"));
    }

    public void checkEmptyNumberFieldCard() {
        errorCardNumber.should(visible);
        errorCardNumber.should(text("Неверный формат"));
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }
    public void checkWrongNumberFieldCard() {
        errorCardNumber.should(visible);
        errorCardNumber.should(text("Неверный формат"));
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(hidden);
        errorCVC.should(hidden);
    }

    public void checkWrongOwnerField() {
        errorCardNumber.should(hidden);
        errorMonth.should(hidden);
        errorYear.should(hidden);
        errorOwner.should(visible);
        errorOwner.should(text("Неверный формат данных"));
        errorCVC.should(hidden);
    }
}