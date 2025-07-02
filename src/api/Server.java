package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.ResponseController;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;

public class Server {

    private static class RequestHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            Server.processHttpExchange(httpExchange);
        }
    }

    public Server(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 128);
        server.createContext("/", new RequestHandler());
        server.start();
        System.out.println("Server started at http://localhost:" + port);
    }

    public static void processHttpExchange(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String method = req.getRequestMethod();


        try {
            apiKeyCheck(req);

            switch (method) {
                case "GET" -> Router.handleGetRequest(path, res, req);
                case "POST" -> Router.handlePostRequest(path, res, req);
                case "PUT" -> Router.handlePutRequest(path, res, req);
                case "DELETE" -> Router.handleDeleteRequest(path, res);
                default -> throw new BadRequestException("Method '" + method + "' tidak didukung di project API ini");
            }

        } catch (UnauthorizedException e) {
            ResponseController.sendErrorResponse(res, "Unauthorized", e.getMessage(), HttpURLConnection.HTTP_UNAUTHORIZED);
        } catch (BadRequestException e) {
            ResponseController.sendErrorResponse(res, "Method tidak didukung", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void apiKeyCheck(Request req) {
        String apiKey = req.getHeader("x-api-key");
        String API = "UNKNOWN";

        if (apiKey == null || !(apiKey.equals(API))) {
            throw new UnauthorizedException("Invalid API Key");
        }
    }
}

