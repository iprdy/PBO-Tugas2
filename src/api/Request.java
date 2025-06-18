package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Request {

    private final HttpExchange httpExchange;
    private Headers headers;
    private String rawBody;

    private String jsonBody;
    private Map<String, String> params = new HashMap<>();

    public void setParam(String key, String value) {
        this.params.put(key, value);
    }

    public Request(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headers = httpExchange.getRequestHeaders();
    }

    public String getHeader (String key) {
        return httpExchange.getRequestHeaders().getFirst(key);
    }

    public String getParam(String key) {
        return this.params.getOrDefault(key, null);
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

    public String getContentType() {
        return headers.getFirst("Content-Type");
    }

    public Map<String, Object> getJSON() throws JsonProcessingException {
        if (!getContentType().equalsIgnoreCase("application/json")) {
            return null;
        }

        Map<String, Object> jsonMap = new HashMap<>();
        if (jsonBody == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonMap = objectMapper.readValue(this.getBody(), new TypeReference<>(){});
        }

        return jsonMap;
    }


}

