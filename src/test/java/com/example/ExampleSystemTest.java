package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ExampleSystemTest {
    @Test
    public void shouldRunWithDeployedFunction() throws IOException, InterruptedException {
        //Root URL pointing to the Cloud Functions deployment
        String functionUrl = System.getenv("FUNCTIONS_BASE_URL");

        var getRequest = HttpRequest.newBuilder().uri(URI.create(functionUrl)).build();
        var response = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals("Hello world!", response.body());
    }
}
