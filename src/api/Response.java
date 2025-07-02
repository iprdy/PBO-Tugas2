package api;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {

    private final HttpExchange httpExchange;
    private final Headers headers;
    private final StringBuilder stringBuilder = new StringBuilder();

    public Response(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headers = httpExchange.getResponseHeaders();
    }

    public void setBody(String string) {
        stringBuilder.setLength(0);
        stringBuilder.append(string);
    }

    public void send(int status) {
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            String method = httpExchange.getRequestMethod();
            byte[] responseBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

            if (method.equalsIgnoreCase("HEAD")) {
                httpExchange.sendResponseHeaders(status, -1);
            } else {
                httpExchange.sendResponseHeaders(status, responseBytes.length);

                OutputStream out = httpExchange.getResponseBody();
                out.write(responseBytes);
                out.flush();
            }
        } catch (IOException ioe) {
            System.err.println("Problem encountered when sending response.");
            ioe.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }
}
