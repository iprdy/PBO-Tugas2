package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.VillasController;
import controllers.ReviewController;
import models.RoomTypes;
import models.Villas;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class Server {
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

        // Tambahkan log lokasi file DB
        System.out.println("DB path: " + new java.io.File("villa_booking.db").getAbsolutePath());
    }

    public static void processHttpExchange(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String method = req.getRequestMethod();

        System.out.printf("method: %s\npath: %s\n", method, path);

        try {
            if (method.equals("GET") && path.matches("/villas/\\d+/reviews")) {
                String[] split = path.split("/");
                int villaId = Integer.parseInt(split[2]);

                String dbPath = "../villa_booking.db"; // relatif dari folder src
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                ReviewController reviewController = new ReviewController(conn);
                reviewController.getReviewsByVillaId(httpExchange, villaId);
                return;
            }

            // Endpoint: villas/{id}/bookings
            if (method.equals("GET") && path.matches("/villas/\\d+/bookings")) {
                String[] split = path.split("/");
                int villaId = Integer.parseInt(split[2]);

                Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
                VillasController vc = new VillasController(conn);
                vc.getBookingsByVillaId(httpExchange, villaId);
                return;
            }


            if (method.equals("POST")) {
                if (path.equals("/villas")) {
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    Villas villas = mapper.readValue(is, Villas.class);
                    VillasController vc = new VillasController();
                    vc.createVilla(villas);

                    // Tambahkan log untuk debug
                    System.out.println("Received POST /villas with data: " + villas.getName());
                } else if (path.matches("/villas/\\d+/rooms$")) {
                    String[] split = path.split("/");
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    RoomTypes roomtypes = mapper.readValue(is, RoomTypes.class);
                    roomtypes.setId(Integer.parseInt(split[2]));
                    VillasController vc = new VillasController();
                    vc.createVillasRooms(roomtypes);
                }
            }

        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        if (!res.isSent()) {
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

    public static void main(String[] args) {
        try {
            new Server(8080); // Jalankan server di port 8080
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

