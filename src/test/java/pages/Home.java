package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Home {

    private final SelenideElement payment = $(byText("Купить"));
    private final SelenideElement creditPayment = $(byText("Купить в кредит"));

    public Payment payment() {
        payment.click();
        return new Payment();
    }

    public PaymentOnCredit creditPayment() {
        creditPayment.click();
        return new PaymentOnCredit();
    }
}