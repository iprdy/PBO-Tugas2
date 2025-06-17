package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.RouterController;
import controllers.VillasController;
import controllers.ReviewController;
import controllers.CustomerController;
import models.RoomTypes;
import models.Villas;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
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

            }
//            } else if(method.equals("PUT")) {
//                if(path.matches("/villas/\\d+")) {
//                    String[] split = path.split("/");
//                    ObjectMapper mapper = new ObjectMapper();
//                    InputStream is = httpExchange.getRequestBody();
//                    Villas villas = mapper.readValue(is, Villas.class);
//                    villas.setId(Integer.parseInt(split[2]));
//                    Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
//                    VillasController vc = new VillasController();
//                    vc.updateVilla(villas);
//                } else if (path.matches("/villas/\\d+/rooms/\\d+$")) {
//                    String[] split = path.split("/");
//                    ObjectMapper mapper = new ObjectMapper();
//                    InputStream is = httpExchange.getRequestBody();
//                    RoomTypes roomtypes = mapper.readValue(is, RoomTypes.class);
//                    roomtypes.setId(Integer.parseInt(split[2]));
//                    roomtypes.setVilla_id(Integer.parseInt(split[4]));
//                    Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
//                    VillasController vc = new VillasController();
//                    vc.updateVillasRoomTypes(roomtypes);
//                } else if (path.matches("/customers/\\d+")) {
//                    Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
//                    CustomerController cc = new CustomerController(conn);
//                    cc.updateCustomer(httpExchange);
//                    return;
//                }
//            } else if(method.equals("DELETE")) {
//                if(path.matches("/villas/\\d+/rooms/\\d+$")) {
//                    String[] split = path.split("/");
//                    Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
//                    VillasController vc = new VillasController();
//                    vc.deleteVillaRoomTypes(Integer.parseInt(split[4]));
//                } else if (path.matches("/villas/\\d")) {
//                    String[] split = path.split("/");
//                    Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
//                    VillasController vc = new VillasController();
//                    vc.deleteVilla(Integer.parseInt(split[2]));
//                }
//            }

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


}

