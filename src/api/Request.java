package api;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {

    private final HttpExchange httpExchange;
    private String rawBody;

    public Request(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public String getHeader (String key) {
        return httpExchange.getRequestHeaders().getFirst(key);
    }

    public String getBody() {
        if (this.rawBody == null) {
            this.rawBody = new BufferedReader(
                    new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8)
            )
                    .lines()
                    .collect(Collectors.joining("\n"));
        }

        return this.rawBody;
    }

    public String getRequestMethod() {
        return httpExchange.getRequestMethod();
    }

    public Map<String, String> getQueryParams() {
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> queryMap = new HashMap<>();

        if (query != null) {
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    queryMap.put(parts[0], parts[1]);
                }
            }
        }

        return queryMap;
    }

}

