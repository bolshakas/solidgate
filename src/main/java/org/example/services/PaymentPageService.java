package org.example.services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class PaymentPageService {

    private final String publicKey;
    private final String secretKey;

    public PaymentPageService(String publicKey, String secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public Response sendCreatePaymentRequest(String requestBody) {
        String signature = SignatureGenerator.generateSignature(publicKey, requestBody, secretKey);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("merchant", publicKey)
                .header("signature", signature)
                .body(requestBody)
                .log().all()
                .post("https://payment-page.solidgate.com/api/v1/init");

        response.then().log().all();

        return response;
    }

    public Response sendStatusCheckRequest(String orderId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("order_id", orderId);

        String signature = SignatureGenerator.generateSignature(publicKey, requestBody.toString(), secretKey);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("merchant", publicKey)
                .header("signature", signature)
                .body(requestBody.toString())
                .log().all()
                .post("https://pay.solidgate.com/api/v1/status");

        response.then().log().all();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to get status: " + response.getStatusCode()
                    + "\n" + response.getBody().asString());
        }

        return response;
    }
}
