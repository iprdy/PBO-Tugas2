package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.VillasController;
import controllers.ReviewController;
import models.RoomTypes;
import models.Booking;
import models.Villas;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            if (method.equals("GET") && path.equals("/villas")) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                VillasController vc = new VillasController(conn);
                List<Villas> villas = new ArrayList<>();
                villas = vc.getAllVillas();

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resJsonMap = new HashMap<>();
                resJsonMap.put("message", villas);
                String resJson = "";

                try {
                    resJson = objectMapper.writeValueAsString(resJsonMap);
                } catch(Exception e) {
                    System.out.println("Serialization error: " + e.getMessage());
                }
                res.setBody(resJson);
                res.send(HttpURLConnection.HTTP_OK);
            }

            if (method.equals("GET") && path.matches("/villas/\\d+$")) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                String[] split = path.split("/");
                VillasController vc = new VillasController(conn);
                Villas villas = vc.getVillaById(Integer.parseInt(split[2]));

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resJsonMap = new HashMap<>();
                resJsonMap.put("message", villas);
                String resJson = "";

                try {
                    resJson = objectMapper.writeValueAsString(resJsonMap);
                } catch(Exception e) {
                    System.out.println("Serialization error: " + e.getMessage());
                }
                res.setBody(resJson);
                res.send(HttpURLConnection.HTTP_OK);
            }

            if (method.equals("GET") && path.matches("/villas/\\d+/rooms$")) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                String[] split = path.split("/");
                VillasController vc = new VillasController(conn);
                List<RoomTypes> villaRoomTypes = vc.getRoomsByVillaId(Integer.parseInt(split[2]));

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resJsonMap = new HashMap<>();
                resJsonMap.put("message", villaRoomTypes);
                String resJson = "";

                try {
                    resJson = objectMapper.writeValueAsString(resJsonMap);
                } catch(Exception e) {
                    System.out.println("Serialization error: " + e.getMessage());
                }
                res.setBody(resJson);
                res.send(HttpURLConnection.HTTP_OK);
            }

            if (method.equals("GET") && path.matches("/villas/\\d+/reviews")) {
                String[] split = path.split("/");
                int villaId = Integer.parseInt(split[2]);

                Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                ReviewController reviewController = new ReviewController(conn);
                reviewController.getReviewsByVillaId(httpExchange, villaId);
                return;
            }

            // Endpoint: GET villas/{id}/bookings
            if (method.equals("GET") && path.matches("/villas/\\d+/bookings")) {
                String[] split = path.split("/");
                int villaId = Integer.parseInt(split[2]);
                Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                VillasController vc = new VillasController(conn);
                vc.getBookingsByVillaId(httpExchange, villaId);
                return;
            }

            // Endpoint: POST booking
//            if (method.equals("POST") && path.equals("/bookings")) {
//                ObjectMapper mapper = new ObjectMapper();
//                InputStream is = httpExchange.getRequestBody();
//                Booking booking = mapper.readValue(is, Booking.class);
//
//                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
//                    VillasController vc = new VillasController(conn);
//                    vc.createBooking(booking);
//                }
//
//                res.setBody("{\"message\": \"Booking added successfully\"}");
//                res.send(HttpURLConnection.HTTP_OK);
//                return;
//            }

            if(method.equals("POST")) {
                if (path.equals("/villas")) {
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    Villas villas = mapper.readValue(is, Villas.class);
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.createVilla(villas);
                } else if (path.matches("/villas/\\d+/rooms$")) {
                    String[] split = path.split("/");
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    RoomTypes roomtypes = mapper.readValue(is, RoomTypes.class);
                    roomtypes.setVilla_id(Integer.parseInt(split[2]));
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.createVillasRooms(roomtypes);
                }
            } else if(method.equals("PUT")) {
                if(path.matches("/villas/\\d+")) {
                    String[] split = path.split("/");
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    Villas villas = mapper.readValue(is, Villas.class);
                    villas.setId(Integer.parseInt(split[2]));
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.updateVilla(villas);
                } else if (path.matches("/villas/\\d+/rooms/\\d+$")) {
                    String[] split = path.split("/");
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = httpExchange.getRequestBody();
                    RoomTypes roomtypes = mapper.readValue(is, RoomTypes.class);
                    roomtypes.setId(Integer.parseInt(split[2]));
                    roomtypes.setVilla_id(Integer.parseInt(split[4]));
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.updateVillasRoomTypes(roomtypes);
                }
            } else if(method.equals("DELETE")) {
                if(path.matches("/villas/\\d+/rooms/\\d+$")) {
                    String[] split = path.split("/");
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.deleteVillaRoomTypes(Integer.parseInt(split[4]));
                } else if (path.matches("/villas/\\d")) {
                    String[] split = path.split("/");
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db");
                    VillasController vc = new VillasController(conn);
                    vc.deleteVilla(Integer.parseInt(split[2]));
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

