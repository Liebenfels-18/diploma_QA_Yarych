package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    private final SelenideElement payment = $(byText("Купить"));
    private final SelenideElement creditPayment = $(byText("Купить в кредит"));

    public PaymentPage payment() {
        payment.click();
        return new PaymentPage();
    }

    public PaymentOnCreditPage creditPayment() {
        creditPayment.click();
        return new PaymentOnCreditPage();
    }
}