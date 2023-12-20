package com.coviscon.orderservice.service;

import java.io.IOException;

public interface RefundService {

    void refundRequest(String access_token, String merchant_uid, String reason) throws IOException;

    String getToken(String apiKey, String secretKey) throws IOException;
}
