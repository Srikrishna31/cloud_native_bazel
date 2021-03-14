package com.example;

import java.io.IOException;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.StringReader;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.google.cloud.functions.HttpResponse;
import com.google.cloud.functions.HttpRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ExampleIntegrationTest {
    //Root URL pointing to the locally hosted function
    private static final String BASE_URL = "http://localhost:8080";

    private static HttpClient client = HttpClient.newHttpClient();
    private static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);

    private static HttpServer server;

    @BeforeClass
    public static void setUp() throws IOException {

        server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        server.createContext("/", new HttpHandler() {
            private Example example = new Example();
            private StringWriter responseOut;
            private BufferedWriter writerOut;

            @Mock private HttpRequest request;
            @Mock private HttpResponse response;
            {
                MockitoAnnotations.initMocks(this);

                //use an empty string as the default request content
                var reader  = new BufferedReader(new StringReader(""));
                when(request.getReader()).thenReturn(reader);

                responseOut = new StringWriter();
                writerOut = new BufferedWriter(responseOut);
                when(response.getWriter()).thenReturn(writerOut);
            }

            @Override
            public void handle(HttpExchange httpExchange) {
                try {
                    example.service(request, response);
                    writerOut.flush();
                    String response = responseOut.toString();
                    httpExchange.sendResponseHeaders(200, response.length());
                    var outStream = httpExchange.getResponseBody();
                    outStream.write(response.getBytes());
                    outStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        });

        server.setExecutor(threadPoolExecutor);
        server.start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop(0);
        threadPoolExecutor.shutdown();
    }

    @Test
    public void integrationTest() throws Throwable {
        var functionUrl = BASE_URL;

        var getRequest = java.net.http.HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(functionUrl))
                .headers("Content-Type", "application/json;charset=UTF-8")
                .build();

        String body = client.send(
                getRequest,
                java.net.http.HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();

        assertEquals("Hello world!", body);
    }
}
