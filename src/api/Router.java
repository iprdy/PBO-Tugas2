package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.RouterController;
import controllers.VillasController;
import models.Villas;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void handleGetRequest(String path, Response res) throws SQLException {
        if (path.equals("/villas")) {
            RouterController.handleGetAllVilla(res);
        }

        else if (path.matches("/villas/\\d+$")) {
            RouterController.handleGetVillaById(path, res);
        }

        else if (path.matches("/villas/\\d+/rooms$")) {

        }

        else if (path.matches("/villas/\\d+/bookings$")) {

        }

        else if (path.matches("/villas/\\d+/reviews")) {

        }

        else if (path.matches("/villas?ci_date=\\d+&co_date=\\d+")) {

        }

        else if (path.equals("/customers")) {

        }

        else if (path.matches("/customers/\\d+")) {

        }

        else if (path.matches("/customers/\\d+/bookings")) {

        }

        else if (path.matches("/customers/\\d+/reviews")) {

        }

        else if (path.equals("/vouchers")) {

        }

        else if (path.matches("/vouchers/\\d+")) {

        }
    }
}
