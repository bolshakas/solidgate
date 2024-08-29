package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

@Getter
public class PaymentSuccessPage {

    @FindBy(css = ".StatusSuccess_title__QcNOC")
    private SelenideElement successTitle;

    @FindBy(css = ".OrderView_root__6ZASv")
    private SelenideElement paymentAmount;

    @FindBy(css = ".StatusPaymentMethod_cardNumber__0JnVb")
    private SelenideElement cardNumber;

    @FindBy(css = ".StatusSuccess_descriptor__GDVX8")
    private SelenideElement paymentDescriptor;

    public PaymentSuccessPage() {
        page(this);
    }

    public void verifyPaymentSuccessOnUI() {
        successTitle.shouldBe(visible)
                .shouldHave(text("Оплата пройшла успішно!"));
    }

    public void verifyPaymentAmount(String expectedAmount) {
        paymentAmount.shouldBe(visible)
                .shouldHave(text(expectedAmount));
    }

    public void verifyLastFourDigitsOfCard(String expectedLastFourDigits) {
        cardNumber.shouldBe(visible)
                .shouldHave(text(expectedLastFourDigits));
    }

    public void verifyPaymentDescriptor(String expectedDescriptor) {
        paymentDescriptor.shouldBe(visible)
                .shouldHave(text(expectedDescriptor));
    }
}