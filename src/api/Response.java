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
    private boolean isSent;

    public Response(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headers = httpExchange.getResponseHeaders();
        this.isSent = false;
    }

    public void setBody(String string) {
        stringBuilder.setLength(0);
        stringBuilder.append(string);
    }

    public void send(int status) {
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            byte[] responseBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

            httpExchange.sendResponseHeaders(status, responseBytes.length);

            OutputStream out = httpExchange.getResponseBody();
            out.write(responseBytes);
            out.flush();
        } catch (IOException ioe) {
            System.err.println("Problem encountered when sending response.");
            ioe.printStackTrace();
        } finally {
            httpExchange.close();
        }
        this.isSent = true;
    }

    public void send(String body) {
        setBody(body);
        send(200); // default ke 200 OK
    }

    public void json(String jsonString) {
        this.setBody(jsonString);
        send(200); // status OK
    }

    public void error(String message) {
        this.setBody("{\"error\": \"" + message.replace("\"", "\\\"") + "\"}");
        send(500); // status 500 Internal Server Error
    }

    public boolean isSent() {
        return this.isSent;
    }
}
