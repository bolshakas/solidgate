package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.page;

public class PaymentPage {

    @FindBy(name = "cardNumber")
    private SelenideElement cardNumberInput;

    @FindBy(name = "cardExpiryDate")
    private SelenideElement cardExpiryDateInput;

    @FindBy(name = "cardCvv")
    private SelenideElement cardCvvInput;

    @FindBy(name = "email")
    private SelenideElement emailInput;

    @FindBy(css = "[data-testid='submit']")
    private SelenideElement payButton;

    public PaymentPage() {
        page(this);
    }

    public void enterCardNumber(String cardNumber) {
        cardNumberInput.setValue(cardNumber);
    }

    public void enterCardExpiryDate(String expiryDate) {
        cardExpiryDateInput.setValue(expiryDate);
    }

    public void enterCardCvv(String cvv) {
        cardCvvInput.setValue(cvv);
    }

    public void enterEmail(String email) {
        emailInput.setValue(email);
    }

    public void clickPayButton() {
        payButton.click();
    }
}

