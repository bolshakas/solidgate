package org.example;

import io.restassured.response.Response;
import org.example.base.BaseTest;
import org.example.pages.PaymentPage;
import org.example.pages.PaymentSuccessPage;
import org.example.services.PaymentPageService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest extends BaseTest {

    public static final int EXPECTED_STATUS_CODE = 200;
    public static final int PRICE = 1020;
    public static final String EUR_CURRENCY = "EUR";
    public static final String EXPECTED_STATUS = "approved";

    @Test
    void testCreatePaymentAndCheckStatus() {
        PaymentPageService service = new PaymentPageService(publicKey, secretKey);
        PaymentSuccessPage successPage = new PaymentSuccessPage();
        String orderId = generateOrderId();

        String paymentPageUrl = createPaymentPage(service, orderId);
        performPayment(paymentPageUrl);
        successPage.verifyPaymentSuccessOnUI();
        checkOrderStatus(service, orderId);
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    private String createPaymentPage(PaymentPageService service, String orderId) {
        String requestBody = buildRequestBody(orderId);
        Response createResponse = service.sendCreatePaymentRequest(requestBody);
        assertEquals(EXPECTED_STATUS_CODE, createResponse.getStatusCode(), "Status code is not 200");

        String paymentPageUrl = createResponse.jsonPath().getString("url");
        assertNotNull(paymentPageUrl, "Payment page URL is null");
        assertFalse(paymentPageUrl.isEmpty(), "Payment page URL is empty");

        return paymentPageUrl;
    }

    private String buildRequestBody(String orderId) {
        JSONObject orderDetails = new JSONObject();
        orderDetails.put("order_id", orderId);
        orderDetails.put("amount", PRICE);
        orderDetails.put("currency", EUR_CURRENCY);
        orderDetails.put("order_description", "Premium package");

        JSONObject pageCustomization = new JSONObject();
        pageCustomization.put("public_name", "Public Name");

        JSONObject requestBody = new JSONObject();
        requestBody.put("order", orderDetails);
        requestBody.put("page_customization", pageCustomization);

        return requestBody.toString();
    }

    private void performPayment(String paymentPageUrl) {
        open(paymentPageUrl);
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.enterCardNumber("4067429974719265");
        paymentPage.enterCardExpiryDate("12/34");
        paymentPage.enterCardCvv("123");
        paymentPage.enterEmail("example@example.com");
        paymentPage.clickPayButton();
    }

    private void checkOrderStatus(PaymentPageService service, String orderId) {
        Response statusResponse = service.sendStatusCheckRequest(orderId);
        assertEquals(EXPECTED_STATUS_CODE, statusResponse.getStatusCode(), "Status code is not 200");

        int actualAmount = statusResponse.jsonPath().getInt("order.amount");
        String actualCurrency = statusResponse.jsonPath().getString("order.currency");
        String actualStatus = statusResponse.jsonPath().getString("order.status");

        assertEquals(PRICE, actualAmount, "Order amount does not match");
        assertEquals(EUR_CURRENCY, actualCurrency, "Order currency does not match");
        assertEquals(EXPECTED_STATUS, actualStatus, "Order status is not approved");
    }
}

