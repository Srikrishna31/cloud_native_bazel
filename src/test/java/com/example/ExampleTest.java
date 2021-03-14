package com.example;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class ExampleTest {
    @Mock private HttpRequest request;
    @Mock private HttpResponse response;

    private BufferedWriter writerOut;
    private StringWriter responseOut;

    @Before
    public void beforeTest() throws IOException {
        MockitoAnnotations.initMocks(this);

        //use an empty string as the default request content
        var reader  = new BufferedReader(new StringReader(""));
        when(request.getReader()).thenReturn(reader);

        responseOut = new StringWriter();
        writerOut = new BufferedWriter(responseOut);
        when(response.getWriter()).thenReturn(writerOut);
    }

    @Test
    public void simpleExampleTest() throws Exception {
        new Example().service(request, response);

        writerOut.flush();

        assertEquals(responseOut.toString(), "Hello world!");
    }
}
