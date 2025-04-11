package com.festimap.tiketing.infra.sms.naver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import com.festimap.tiketing.infra.sms.naver.dto.NaverMessageReqDto;
import com.festimap.tiketing.infra.sms.naver.dto.NaverSmsReqDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class NaverRequestBuilder {

    private final ObjectMapper objectMapper;
    private final String accessKey;
    private final String secretKey;
    private final String senderPhone;

    private String defaultMessage;
    private HttpHeaders headers;
    private NaverMessageReqDto messageReqDto;

    private NaverRequestBuilder(ObjectMapper objectMapper, String accessKey, String secretKey, String senderPhone) {
        this.objectMapper = objectMapper;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.senderPhone = senderPhone;
    }

    public static NaverRequestBuilder builder(ObjectMapper objectMapper,
                                              String accessKey,
                                              String secretKey,
                                              String senderPhone) {
        return new NaverRequestBuilder(objectMapper, accessKey, secretKey, senderPhone);
    }

    public NaverRequestBuilder header(String endpoint) {
        try {
            this.headers = createHeaders(endpoint);
        }
        catch (Exception e) {
            throw new BaseException("Failed to build SMS body", ErrorCode.SMS_SEND_FAILED);
        }
        return this;
    }

    public NaverRequestBuilder body(SmsSendRequest smsSendRequest) {
        this.defaultMessage = smsSendRequest.getMessage();
        this.messageReqDto = NaverMessageReqDto.builder()
                .to(smsSendRequest.getTo())
                .build();
        return this;
    }

    public HttpEntity<String> build() {
        NaverSmsReqDto naverSmsReqDto = NaverSmsReqDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderPhone)
                .content(defaultMessage)
                .messages(List.of(messageReqDto))
                .build();

        try {
            String bodyStr = objectMapper.writeValueAsString(naverSmsReqDto);
            return new HttpEntity<>(bodyStr, headers);
        } catch (JsonProcessingException e) {
            throw new BaseException("Failed to build SMS message",ErrorCode.SMS_SEND_FAILED);
        }
    }

    private HttpHeaders createHeaders(String endpoint)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        final long currentTime = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", String.valueOf(currentTime));
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(currentTime, endpoint));
        return headers;
    }

    private String makeSignature(long time, String endpoint)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String timestamp = String.valueOf(time);

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(endpoint)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeBase64String(rawHmac);
    }
}
