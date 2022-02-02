package com.marcosoft.jiraforadmins.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class JiraClient {

    @Value("${jira.end-point}")
    public String jiraEndPointAddress;
    @Value("${jira.auth-token}")
    private String jiraAuthToken;

    private final HttpClient httpClient = HttpClient.newBuilder().build();


    public HttpRequest buildJiraRequest(String method, String endPointSulfix, String requestbody){
        URI jiraEndPoint = URI.create(jiraEndPointAddress + endPointSulfix);
        return HttpRequest.newBuilder()
                .uri(jiraEndPoint)
                .method(method, HttpRequest.BodyPublishers.ofString(requestbody))
                .setHeader("Authorization", jiraAuthToken)
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .build();
    }

    public HttpResponse<String> sendJiraRequest(HttpRequest httpRequest){
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("Request Method: {}", httpRequest.method());
            log.info("Request End Point: {}",  httpRequest.uri());
            log.info("Request Headers: {}",  httpRequest.headers());
            log.info("Response Status: {}", httpResponse.statusCode());
            log.info("Response Body: {}", httpResponse.body());

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        return httpResponse;
    }


}
