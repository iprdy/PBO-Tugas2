package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.ResponseController;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static String API = "UNKNOWN";
    private HttpServer server;

    private class RequestHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            Server.processHttpExchange(httpExchange);
        }
    }

    public Server(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 128);
        server.createContext("/", new RequestHandler());
        server.start();
        System.out.println("Server started at http://localhost:" + port);

        // Log lokasi file DB
        System.out.println("DB path: " + new java.io.File("villa_booking.db").getAbsolutePath());
    }

    public static void processHttpExchange(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String method = req.getRequestMethod();


        try {
            apiKeyCheck(req);

            if (method.equals("GET")) {
                Router.handleGetRequest(path,res);
            }

            else if (method.equals("POST")) {
                Router.handlePostRequest(path,res, req);
            }

            else if (method.equals("PUT")) {
                Router.handlePutRequest(path,res, req);
            }

            else if (method.equals("DELETE")) {
                Router.handleDeleteRequest(path,res);
            }

        } catch (UnauthorizedException e) {
            ResponseController.sendErrorResponse(res, "Unauthorized", e.getMessage(), HttpURLConnection.HTTP_UNAUTHORIZED);;
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        if (res.isSent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resJsonMap = new HashMap<>();
            resJsonMap.put("message", "Request Success");

            String resJson = "";
            try {
                resJson = objectMapper.writeValueAsString(resJsonMap);
            } catch(Exception e) {
                System.out.println("Serialization error: " + e.getMessage());
            }
            res.setBody(resJson);
            res.send(HttpURLConnection.HTTP_OK);
        }

        httpExchange.close();
    }

    public static void apiKeyCheck(Request req) {
        String apiKey = req.getHeader("x-api-key");

        if (apiKey == null || !(apiKey.equals(API))) {
            throw new UnauthorizedException("Invalid API Key");
        }
    }
}

