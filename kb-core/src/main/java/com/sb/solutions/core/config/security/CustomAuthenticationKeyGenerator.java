package com.sb.solutions.core.config.security;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationKeyGenerator implements AuthenticationKeyGenerator, Serializable {

    private static final String CLIENT_ID = "client_id";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
            values.put("principal", authentication.getPrincipal().toString());
        }
        values.put(CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(authorizationRequest.getScope()));
        }

        /*String deviceId = "111";
        values.put("device_id", deviceId);*/
        String deviceId = authorizationRequest.getRequestParameters().get("device_id");
        if (deviceId != null && !deviceId.isEmpty()) {
            values.put("device_id", deviceId);
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                "MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                "UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }
}