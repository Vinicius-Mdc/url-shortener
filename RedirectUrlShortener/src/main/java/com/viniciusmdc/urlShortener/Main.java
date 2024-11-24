package com.viniciusmdc.urlShortener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final S3Client s3Client = S3Client.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String s3BucketName = System.getenv("S3_BUCKET_NAME");

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String pathParameters = input.get("rawPath").toString();
        String shortUrlCode = pathParameters.replace("/", "");

        if (shortUrlCode.equals("")) {
            throw new IllegalArgumentException("Invalid input: shortUrlCode is required.");
        }

        GetObjectRequest request = GetObjectRequest
                .builder()
                .bucket(s3BucketName)
                .key(shortUrlCode + ".json")
                .build();

        InputStream inputStream;
        try {
            inputStream = s3Client.getObject(request);
        } catch (Exception e) {
            throw new RuntimeException("Error fetcing data from S3: " + e.getMessage(), e);
        }

        UrlData urlData;

        try {
            urlData = objectMapper.readValue(inputStream, UrlData.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing URL data: " + e.getMessage(), e);
        }

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        Map<String, Object> response = new HashMap<>();
        if (currentTimeInSeconds >= urlData.getExpirationTime()) {
            response.put("statusCode", 410);
            response.put("body", "This URL has expired.");
            return response;
        }

        response.put("statusCode", 302);
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", urlData.getOriginalUrl());
        response.put("headers", headers);

        return response;
    }
}